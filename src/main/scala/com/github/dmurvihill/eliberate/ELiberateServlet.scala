package com.github.dmurvihill.eliberate

import org.scalatra._

class ELiberateServlet extends ScalatraServlet with AuthenticationSupport {

  get("/") {
    val user: User = basicAuth.get
    views.html.hello(user)
  }

}
