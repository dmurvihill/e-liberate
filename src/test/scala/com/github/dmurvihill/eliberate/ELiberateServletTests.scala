package com.github.dmurvihill.eliberate

import java.util.Base64
import org.scalatra.test.scalatest._

class ELiberateServletTests extends ScalatraFunSuite {

  addServlet(classOf[ELiberateServlet], "/*")
  val aladdin_auth_headers = Seq[(String, String)](basicAuthCredentials("Aladdin", "opensesame"))

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
    get("/", Seq.empty, aladdin_auth_headers) {
      status should equal(200)
    }
  }

  test("GET on nonexsitent motion should return status 404") {
    get("/motion/3", None, aladdin_auth_headers) {
      status should equal(404)
    }
  }

  test("GET with non-integer motion ID should return status 404") {
    get("/motion/one", None, aladdin_auth_headers) {
      status should equal(404)
    }
  }

  test("POST vote with non-integer motion ID should return status 404") {
    post("/motion/one/vote", List(("vote", "Yes")), List.empty, aladdin_auth_headers) {
      status should equal(404)
    }
  }

  private def basicAuthCredentials(username: String, password: String): (String, String) = {
    val rawCredentials = username + ":" + password
    val encCredentials = Base64.getEncoder().encodeToString(rawCredentials)
    ("Authorization", "Basic " + encCredentials)
  }
}
