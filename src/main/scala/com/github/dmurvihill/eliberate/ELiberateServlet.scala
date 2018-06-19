package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.auth.AuthenticationSupport
import com.github.dmurvihill.eliberate.form.MotionToAdoptForm
import org.scalatra._
import org.scalatra.forms.FormSupport
import org.scalatra.i18n.I18nSupport
import scala.collection.mutable

class ELiberateServlet extends ScalatraServlet with AuthenticationSupport with FormSupport with I18nSupport{

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
      case None => NotFound()
    }
  }

  post("/motion") {
    val user: User = basicAuth.get
    validate(MotionToAdoptForm.form)(
      (errors: Seq[(String, String)]) => {
        BadRequest(views.html.error(errors))
      },
      (form: MotionToAdoptForm) => {
        val motion = Motion.create(form)
        redirect("/motion/"+motion.id)
      }
    )
  }
}
