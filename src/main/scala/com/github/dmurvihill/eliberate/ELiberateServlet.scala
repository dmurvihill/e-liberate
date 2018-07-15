package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.auth.AuthenticationSupport
import com.github.dmurvihill.eliberate.form.MotionToAdoptForm
import com.github.dmurvihill.eliberate.form.VoteForm
import java.time.Instant
import org.scalatra._
import org.scalatra.forms.FormSupport
import org.scalatra.i18n.I18nSupport
import scala.collection.mutable
import scala.util.matching.Regex

class ELiberateServlet extends ScalatraServlet with AuthenticationSupport with FormSupport with I18nSupport{

  val IntMatch = new Regex("\\d+")

  get("/") {
    val user: User = basicAuth.get
    val motions = Motion.getAll()
    views.html.home(user, motions)
  }

  get("/motion/:id") {
    val user: User = basicAuth.get
    params("id") match {
      case IntMatch() => {
        val id = params("id").toInt
        getMotion(id)
      }
      case _ => NotFound()
    }
  }

  post("/motion") {
    val createdAt: Instant = Instant.now
    val user: User = basicAuth.get
    validate(MotionToAdoptForm.form)(
      (errors: Seq[(String, String)]) => {
        BadRequest(views.html.error(errors, "/"))
      },
      (form: MotionToAdoptForm) => {
        val motion = Motion.create(form, createdAt)
        redirect("/motion/"+motion.id)
      }
    )
  }

  post("/motion/:id/vote") {
    val user: User = basicAuth.get
    params("id") match {
      case IntMatch() => {
        val motionId = params("id").toInt
        tryToVoteOn(motionId)
      }
      case _ => NotFound()
    }
  }

  def getMotion(id: Int) =
    Motion.get(id) match {
      case Some(motion) => views.html.motion(user, motion, Instant.now)
      case None => NotFound()
    }

  def tryToVoteOn(motionId: Int) = {
    validate(VoteForm.form)(
      (errors: Seq[(String, String)]) => {
        BadRequest(views.html.error(errors, "/motion/" + motionId))
      },
      postVote(motionId)
    )
  }

  def postVote(motionId: Int)(form: VoteForm) = {
    val vote = Vote.withName(form.vote)
    val now = Instant.now
    val parentUri = "/motion/" + motionId
    try {
      val motion = Motion.vote(motionId, user, vote, now)
      views.html.motion(user, motion, now)
    }
    catch {
      case e: IllegalStateException => views.html.error(List(("", e.getMessage)), parentUri)
    }
  }
}
