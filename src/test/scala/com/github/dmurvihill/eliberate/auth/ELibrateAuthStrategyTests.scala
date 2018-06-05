package com.github.dmurvihill.eliberate.auth

import com.github.dmurvihill.eliberate.User
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.scalatra.ScalatraBase
import org.scalatra.test.scalatest.ScalatraFunSuite


class ELiberateAuthStrategyTests extends ScalatraFunSuite {
  test("checkPassword as Aladdin should return the Aladdin user") {
    val actualUser = ELiberateAuthStrategy.checkPassword("Aladdin", "opensesame")
    val expectedUser = Some(User("Aladdin"))
    assert(actualUser == expectedUser)
  }

  test("checkPassword as Jafar should return the Jafar user") {
    val actualUser = ELiberateAuthStrategy.checkPassword("Jafar", "opensesame")
    val expectedUser = Some(User("Jafar"))
    assert(actualUser == expectedUser)
  }

  test("checkPassword with incorrect password should return None") {
    assert(ELiberateAuthStrategy.checkPassword("Aladdin", "opencaraway") == None)
  }

  test("getUserId on Aladdin returns Aladdin's username") {
    assert(ELiberateAuthStrategy.getUserId(User("Aladdin")) == "Aladdin")
  }

  test("getUserId on Jasmine returns Jasmine's username") {
    assert(ELiberateAuthStrategy.getUserId(User("Jasmine")) == "Jasmine")
  }
}
