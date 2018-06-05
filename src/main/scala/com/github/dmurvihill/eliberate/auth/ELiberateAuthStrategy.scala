package com.github.dmurvihill.eliberate.auth

import com.github.dmurvihill.eliberate.User
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.scalatra.auth.strategy.{BasicAuthStrategy, BasicAuthSupport}
import org.scalatra.auth.{ScentrySupport, ScentryConfig}
import org.scalatra.{ScalatraBase}

class ELiberateAuthStrategy(protected override val app: ScalatraBase, realm: String)
  extends BasicAuthStrategy[User](app, realm) {
  // Untested -- thin wrapper that delegates all method calls to its companion object.

  protected def validate(username: String, password: String)(implicit request: HttpServletRequest, response: HttpServletResponse): Option[User] = ELiberateAuthStrategy.checkPassword(username, password)

  protected def getUserId(user: User)(implicit request: HttpServletRequest, response: HttpServletResponse): String = ELiberateAuthStrategy.getUserId(user)
}

object ELiberateAuthStrategy {

  def checkPassword(username: String, password: String): Option[User] = {
    if(password == "opensesame") Some(User(username))
    else None
  }

  def getUserId(user: User): String = user.username
}
