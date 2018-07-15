package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.form.MotionToAdoptForm
import java.time.temporal.ChronoUnit
import java.time.Instant
import scala.collection.mutable


case class Motion(id: Int,
                  title: String,
                  text: String,
                  deadline: Instant,
                  rollCall: Motion.RollCall = Map.empty[User, Vote.Value]) {

  lazy val votersByVote = {
    val groupedByVote = rollCall.toList.groupBy(_._2)
    val unpackedFromTuples = groupedByVote.mapValues(_.map(_._1))
    unpackedFromTuples.mapValues(_.toSet).withDefaultValue(Set.empty[User])
  }

  def isExpiredAt(t: Instant): Boolean = t isAfter deadline

  def vote(user: User, vote: Vote.Value, voteTime: Instant): Motion = {
    val expiredMsg = "Voting on this motion ended at " + deadline.toString + " (UTC). (motion ID: " + id + ")"
    require(!isExpiredAt(voteTime), expiredMsg)
    Motion(id, title, text, deadline, rollCall + (user -> vote))
  }

}

object Motion {

  type RollCall = Map[User, Vote.Value]

  val demo_motion_id = 1
  val demo_motion = Motion(
    demo_motion_id,
    "An Act to disband the ministry of Silly Walks",
    "This started out as a nice sketch about walks but it has become far too silly.",
    Instant.now().plus(1, ChronoUnit.DAYS)
  )
  val motions = mutable.Map(demo_motion_id -> demo_motion)
  var next_motion_id = demo_motion_id + 1

  def get(id: Int): Option[Motion] = motions.get(id)
  def getAll(): Iterable[Motion] = motions.values

  def create(form: MotionToAdoptForm, createdAt: Instant): Motion = {
    val motion = Motion(id=next_motion_id, title=form.title, text=form.text, createdAt.plus(1, ChronoUnit.DAYS))
    motions += (next_motion_id -> motion)
    next_motion_id += 1
    motion
  }

  def vote(id: Int, user: User, vote: Vote.Value, votedAt: Instant): Motion = {
    try {
      val updatedMotion = motions(id).vote(user, vote, votedAt)
      motions += (id -> updatedMotion)
      updatedMotion
    }
    catch {
      case e: NoSuchElementException => {
        val msg = "No motion with ID: " + id
        throw new NoSuchElementException(msg)
      }
      case e: IllegalArgumentException => {
        throw new IllegalStateException(e.getMessage, e)
      }
    }
  }
}

object Vote extends Enumeration {
  val Yes, No = Value
}
