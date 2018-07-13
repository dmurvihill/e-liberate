package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.form.MotionToAdoptForm
import scala.collection.mutable


case class Motion(id: Int,
                  title: String,
                  text: String,
                  rollCall: Motion.RollCall = Map.empty[User, Vote.Value]) {
  def vote(user: User, vote: Vote.Value): Motion = {
    Motion(id, title, text, rollCall + (user -> vote))
  }
}

object Motion {

  type RollCall = Map[User, Vote.Value]

  val demo_motion_id = 1
  val demo_motion = Motion(
    demo_motion_id,
    "An Act to disband the ministry of Silly Walks",
    "This started out as a nice sketch about walks but it has become far too silly."
  )
  val motions = mutable.Map(demo_motion_id -> demo_motion)
  var next_motion_id = demo_motion_id + 1

  def get(id: Int): Option[Motion] = motions.get(id)
  def getAll(): Iterable[Motion] = motions.values

  def create(form: MotionToAdoptForm): Motion = {
    val motion = Motion(id=next_motion_id, title=form.title, text=form.text)
    motions += (next_motion_id -> motion)
    next_motion_id += 1
    motion
  }

  def vote(id: Int, user: User, vote: Vote.Value): Motion = {
    try {
      val updatedMotion = motions(id).vote(user, vote)
      motions += (id -> updatedMotion)
      updatedMotion
    }
    catch {
      case e: NoSuchElementException => {
        val msg = "No motion with ID: " + id
        throw new NoSuchElementException(msg)
      }
    }
  }
}

object Vote extends Enumeration {
  val Yes, No = Value
}
