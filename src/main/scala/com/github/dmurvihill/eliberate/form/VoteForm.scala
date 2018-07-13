package com.github.dmurvihill.eliberate.form

import com.github.dmurvihill.eliberate.Vote
import org.scalatra.forms._

case class VoteForm(vote: String)

object VoteForm {
  val validVotes = Vote.values.map(_.toString)
  val form = mapping(
    "vote" -> label("vote", text(required, oneOf(validVotes.toList)))
  )(VoteForm.apply)
}
