package com.github.dmurvihill.eliberate.auth

import com.github.dmurvihill.eliberate.User
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.scalatra.auth.strategy.{BasicAuthStrategy, BasicAuthSupport}
import org.scalatra.auth.{ScentrySupport, ScentryConfig}
import org.scalatra.{ScalatraBase}

class ELiberateAuthStrategy(protected override val app: ScalatraBase, realm: String)
  extends BasicAuthStrategy[User](app, realm) {

  protected def validate(username: String, password: String)(implicit request: HttpServletRequest, response: HttpServletResponse): Option[User] = {
    if(password == "opensesame") Some(User(username))
    else None
  }

  protected def getUserId(user: User)(implicit request: HttpServletRequest, response: HttpServletResponse): String = user.username
}
