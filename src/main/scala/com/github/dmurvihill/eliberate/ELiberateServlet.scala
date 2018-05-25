package com.github.dmurvihill.eliberate

import com.github.dmurvihill.eliberate.auth.AuthenticationSupport
import org.scalatra._

class ELiberateServlet extends ScalatraServlet with AuthenticationSupport {

  get("/") {
    val user: User = basicAuth.get
    views.html.hello(user)
  }

}
