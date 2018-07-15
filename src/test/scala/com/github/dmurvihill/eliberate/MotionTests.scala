package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.form.MotionToAdoptForm
import java.time.temporal.ChronoUnit
import java.time.ZonedDateTime
import java.time.ZoneId
import org.scalatest.FunSuite


class MotionTests extends FunSuite {

  val theYear2001 = ZonedDateTime.of(2001, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant
  val late2000 = ZonedDateTime.of(2000, 12, 31, 23, 59, 59, 0, ZoneId.of("UTC")).toInstant
  val dickClarkSigningOff = ZonedDateTime.of(2001, 1, 1, 0, 10, 0, 0, ZoneId.of("UTC")).toInstant

  val testMotion = Motion(1, "test motion", "this is the text of the test motion", theYear2001)
  val aladdin = User("Aladdin", "opensesame")
  val jafar = User("Jafar", "opencaraway")

  test("roll call defaults to empty") {
    assert(testMotion.rollCall == Map.empty[User, Vote.Value])
  }

  test("is not expired before the deadline") {
    assert(!(testMotion isExpiredAt late2000))
  }

  test("is expired after the deadline") {
    assert((testMotion isExpiredAt dickClarkSigningOff))
  }

  test("vote records roll call") {
    val newMotion = testMotion.vote(aladdin, Vote.Yes, late2000)
    assert(newMotion.rollCall(aladdin) == Vote.Yes)
  }

  test("vote records accurate roll call") {
    val newMotion = testMotion.vote(aladdin, Vote.No, late2000)
    assert(newMotion.rollCall(aladdin) == Vote.No)
  }

  test("vote records roll call for correct user") {
    val newMotion = testMotion.vote(jafar, Vote.Yes, late2000)
    assert(newMotion.rollCall(jafar) == Vote.Yes)
  }

  test("voting again with another user updates the vote") {
    val rollCall = Map[User, Vote.Value](aladdin -> Vote.No)
    val m = Motion(1, "test motion", "test text", theYear2001, rollCall)
    val newMotion = m.vote(aladdin, Vote.Yes, late2000)
    assert(newMotion.rollCall(aladdin) == Vote.Yes)
  }

  test("vote raises IllegalArgumentException if we voted after the deadline") {
    val e = intercept[IllegalArgumentException] {
      testMotion.vote(aladdin, Vote.Yes, dickClarkSigningOff)
    }
    assert(e.getMessage.contains("ended at 2001-01-01T00:00:00Z (UTC)"))
  }

  test("votersByVote returns a set for each possible vote") {
    val usersWhoVoted = testMotion.votersByVote
    for (vote <- Vote.values) {
      assert(usersWhoVoted(vote) == Set.empty[User])
    }
  }

  test("votersByVote groups users by their vote") {
    val rollCall = Map[User, Vote.Value](aladdin -> Vote.Yes, jafar -> Vote.No)
    val m = Motion(1, "test motion", "test text", theYear2001, rollCall)

    assert(m.votersByVote == Map[Vote.Value, Set[User]](
      Vote.Yes -> Set[User](aladdin),
      Vote.No -> Set[User](jafar)
    ))
  }
}

class MotionObjectTests extends FunSuite {

  val theYear2001 = ZonedDateTime.of(2001, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant
  val aladdin = User("Aladdin", "opensesame")

  test("get with ID 1 returns the demo motion") {
    assert(Motion.get(1) == Some(Motion.demo_motion))
  }

  test("get with invalid ID returns None") {
    assert(Motion.get(0) == None)
  }

  test("create creates motion") {
    val motionTitle = "test motion title"
    val motionText = "test motion body"
    val deadline = theYear2001.plus(1, ChronoUnit.DAYS)
    val form = MotionToAdoptForm(
      title=motionTitle,
      text=motionText
    )
    assert(Motion.create(form, theYear2001) == Motion(2, motionTitle, motionText, deadline))
    assert(Motion.get(2) == Some(Motion(2, motionTitle, motionText, deadline)))
  }

  test("vote updates motion") {
    val expMotion = Motion.demo_motion.vote(aladdin, Vote.Yes, theYear2001)
    assert(Motion.vote(1, aladdin, Vote.Yes, theYear2001) == expMotion)
    assert(Motion.get(1) == Some(expMotion))
  }

  test("vote on bad motion ID raises NoSuchElementException") {
    val caught = intercept[NoSuchElementException] {
      Motion.vote(0, aladdin, Vote.Yes, theYear2001)
    }
    assert(caught.getMessage == "No motion with ID: 0")
  }

  test("vote on expired motion raises IllegalStateException") {
    val deadline = Motion.demo_motion.deadline
    val e = intercept[IllegalStateException] {
      Motion.vote(1, aladdin, Vote.Yes, deadline.plusMillis(1))
    }
    assert(e.getMessage.contains("ended at " + deadline.toString + " (UTC)"))
  }
}
