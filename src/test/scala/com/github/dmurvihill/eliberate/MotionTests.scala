package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.form.MotionToAdoptForm
import org.scalatest.FunSuite


class MotionTests extends FunSuite {

  test("get with ID 1 returns the demo motion") {
    assert(Motion.get(1) == Some(Motion.demo_motion))
  }

  test("get with invalid ID returns None") {
    assert(Motion.get(2) == None)
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
}
