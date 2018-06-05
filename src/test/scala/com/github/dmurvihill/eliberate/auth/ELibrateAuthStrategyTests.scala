package com.github.dmurvihill.eliberate.auth

import com.github.dmurvihill.eliberate.User
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.scalatra.ScalatraBase
import org.scalatra.test.scalatest.ScalatraFunSuite



class ELiberateAuthStrategyTests extends ScalatraFunSuite {

  val aladdin_user = User("Aladdin", "opensesame")
  val jasmine_user = User("Jasmine", "showmetheworld")
  val jafar_user = User("Jafar", "open sesame")

  test("checkPassword as Aladdin should return the Aladdin user") {
    val actualUser = ELiberateAuthStrategy.checkPassword("Aladdin", "opensesame")
    assert(actualUser == Some(aladdin_user))
  }

  test("checkPassword as Jafar should return the Jafar user") {
    val actualUser = ELiberateAuthStrategy.checkPassword("Jafar", "open sesame")
    assert(actualUser == Some(jafar_user))
  }

  test("checkPassword with incorrect password should return None") {
    assert(ELiberateAuthStrategy.checkPassword("Aladdin", "opencaraway") == None)
  }

  test("getUserId on Aladdin returns Aladdin's username") {
    assert(ELiberateAuthStrategy.getUserId(aladdin_user) == "Aladdin")
  }

  test("getUserId on Jasmine returns Jasmine's username") {
    assert(ELiberateAuthStrategy.getUserId(jasmine_user) == "Jasmine")
  }
}
