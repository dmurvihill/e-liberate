package com.github.dmurvihill.eliberate.form

import org.scalatra.forms._


case class MotionToAdoptForm(
  title: String,
  text: String
)

object MotionToAdoptForm {
  val form = mapping(
    "title" -> label("Title", text(required, maxlength(64))),
    "text" -> label("Text", text(required))
  )(MotionToAdoptForm.apply)
}
