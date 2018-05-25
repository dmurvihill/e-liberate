package com.github.dmurvihill.eliberate

import java.util.Base64
import org.scalatra.test.scalatest._

class ELiberateServletTests extends ScalatraFunSuite {

  addServlet(classOf[ELiberateServlet], "/*")

  test("GET / on ELiberateServlet with no authentication should return status 401") {
    get("/") {
      status should equal (401)
    }
  }

  test("GET / on ELiberateServlet with valid authentication should return status 401") {
    get("/", Seq.empty, Seq[(String, String)](basicAuthCredentials("Aladdin", "opencaraway"))) {
      status should equal (401)
    }
  }

  test("GET / on ELiberateServlet with valid authentication should return status 200") {
    get("/", Seq.empty, Seq[(String, String)](basicAuthCredentials("Aladdin", "opensesame"))) {
      status should equal(200)
    }
  }

  private def basicAuthCredentials(username: String, password: String): (String, String) = {
    val rawCredentials = username + ":" + password
    val encCredentials = Base64.getEncoder().encodeToString(rawCredentials)
    ("Authorization", "Basic " + encCredentials)
  }
}
