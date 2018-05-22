package com.github.dmurvihill.eliberate

import org.scalatra.test.scalatest._

class ELiberateServletTests extends ScalatraFunSuite {

  addServlet(classOf[ELiberateServlet], "/*")

  test("GET / on ELiberateServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
