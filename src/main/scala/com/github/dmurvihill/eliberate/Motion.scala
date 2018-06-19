package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.form.MotionToAdoptForm
import scala.collection.mutable


case class Motion(id: Int, title: String, text: String)

object Motion {

  val demo_motion_id = 1
  val demo_motion = Motion(
    demo_motion_id,
    "An Act to disband the ministry of Silly Walks",
    "This started out as a nice sketch about walks but it has become far too silly."
  )
  val motions = mutable.Map(demo_motion_id -> demo_motion)
  var next_motion_id = demo_motion_id + 1

  def get(id: Int): Option[Motion] = {
    if (id < next_motion_id) Some(motions(id))
    else None
  }

  def getAll(): Iterable[Motion] = motions.values

  def create(form: MotionToAdoptForm): Motion = {
    val motion = Motion(id=next_motion_id, title=form.title, text=form.text)
    motions += (next_motion_id -> motion)
    next_motion_id += 1
    motion
  }
}
