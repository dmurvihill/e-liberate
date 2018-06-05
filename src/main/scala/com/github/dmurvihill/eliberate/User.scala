package com.github.dmurvihill.eliberate


case class User(username: String, password: String)

object User {

  val users = Map[String, User](
    "Aladdin" -> User("Aladdin", "opensesame"),
    "Jafar" -> User("Jafar", "open sesame")
  )

  def get(username: String): User = users(username)
}
