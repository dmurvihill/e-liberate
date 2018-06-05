package com.github.dmurvihill.eliberate


case class Motion(id: Int, title: String, text: String)

object Motion {

  val demo_motion_id = 1
  val demo_motion = Motion(
    demo_motion_id,
    "An Act to disband the ministry of Silly Walks",
    "This started out as a nice sketch about walks but it has become far too silly."
  )

  def get(id: Int): Option[Motion] = id match {
    case 1 => Some(demo_motion)
    case _ => None
  }

  def getAll(): List[Motion] = List[Motion](demo_motion)
}
