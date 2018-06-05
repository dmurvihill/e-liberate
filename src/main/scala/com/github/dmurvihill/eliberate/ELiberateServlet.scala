package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.auth.AuthenticationSupport
import org.scalatra._
import scala.collection.mutable

class ELiberateServlet extends ScalatraServlet with AuthenticationSupport {

  get("/") {
    val user: User = basicAuth.get
    val motions = Motion.getAll()
    views.html.home(user, motions)  // TODO pass an immutable view on motions
  }

  get("/motion/:id") {
    val user: User = basicAuth.get
    val motion_id = params("id").toInt
    Motion.get(motion_id) match {
      case Some(motion) => views.html.motion(user, motion)
      case None => NotFound
    }
  }
}
