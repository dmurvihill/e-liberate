package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.form.MotionToAdoptForm
import org.scalatest.FunSuite


class MotionTests extends FunSuite {

  val testMotion = Motion(1, "test motion", "this is the text of the test motion")
  val aladdin = User("Aladdin", "opensesame")
  val jafar = User("Jafar", "opencaraway")

  test("roll call defaults to empty") {
    assert(testMotion.rollCall == Map.empty[User, Vote.Value])
  }

  test("vote records roll call") {
    val newMotion = testMotion.vote(aladdin, Vote.Yes)
    assert(newMotion.rollCall(aladdin) == Vote.Yes)
  }

  test("vote records accurate roll call") {
    val newMotion = testMotion.vote(aladdin, Vote.No)
    assert(newMotion.rollCall(aladdin) == Vote.No)
  }

  test("vote records roll call for correct user") {
    val newMotion = testMotion.vote(jafar, Vote.Yes)
    assert(newMotion.rollCall(jafar) == Vote.Yes)
  }

  test("voting again with another user updates the vote") {
    val rollCall = Map[User, Vote.Value](aladdin -> Vote.No)
    val m = Motion(1, "test motion", "test text", rollCall)
    val newMotion = m.vote(aladdin, Vote.Yes)
    assert(newMotion.rollCall(aladdin) == Vote.Yes)
  }

  test("votersByVote returns a set for each possible vote") {
    val usersWhoVoted = testMotion.votersByVote
    for (vote <- Vote.values) {
      assert(usersWhoVoted(vote) == Set.empty[User])
    }
  }

  test("votersByVote groups users by their vote") {
    val rollCall = Map[User, Vote.Value](aladdin -> Vote.Yes, jafar -> Vote.No)
    val m = Motion(1, "test motion", "test text", rollCall)

    assert(m.votersByVote == Map[Vote.Value, Set[User]](
      Vote.Yes -> Set[User](aladdin),
      Vote.No -> Set[User](jafar)
    ))
  }
}

class MotionObjectTests extends FunSuite {

  test("get with ID 1 returns the demo motion") {
    assert(Motion.get(1) == Some(Motion.demo_motion))
  }

  test("get with invalid ID returns None") {
    assert(Motion.get(0) == None)
  }

  test("create creates motion") {
    val motionTitle = "test motion title"
    val motionText = "test motion body"
    val form = MotionToAdoptForm(
      title=motionTitle,
      text=motionText
    )
    assert(Motion.create(form) == Motion(2, motionTitle, motionText))
    assert(Motion.get(2) == Some(Motion(2, motionTitle, motionText)))
  }

  test("vote updates motion") {
    val aladdin = User("Aladdin", "opensesame")
    val expMotion = Motion.demo_motion.vote(aladdin, Vote.Yes)
    assert(Motion.vote(1, aladdin, Vote.Yes) == expMotion)
    assert(Motion.get(1) == Some(expMotion))
  }

  test("vote on bad motion ID raises NoSuchElementException") {
    val aladdin = User("Aladdin", "opensesame")
    val caught = intercept[NoSuchElementException] {
      Motion.vote(0, aladdin, Vote.Yes)
    }
    assert(caught.getMessage == "No motion with ID: 0")
  }

}
