package com.github.dmurvihill.eliberate

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.scalatra.auth.strategy.{BasicAuthStrategy, BasicAuthSupport}
import org.scalatra.auth.{ScentrySupport, ScentryConfig}
import org.scalatra.{ScalatraBase}

class OurBasicAuthStrategy(protected override val app: ScalatraBase, realm: String)
  extends BasicAuthStrategy[User](app, realm) {

  protected def validate(userName: String, password: String)(implicit request: HttpServletRequest, response: HttpServletResponse): Option[User] = {
    if(userName == "scalatra" && password == "scalatra") Some(User("scalatra"))
    else None
  }

  protected def getUserId(user: User)(implicit request: HttpServletRequest, response: HttpServletResponse): String = user.id

}

case class User(userName: String) {
    val id = "scalatra"
}
