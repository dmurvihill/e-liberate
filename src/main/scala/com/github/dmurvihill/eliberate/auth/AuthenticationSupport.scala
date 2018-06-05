package com.github.dmurvihill.eliberate.auth

import com.github.dmurvihill.eliberate.User
import org.scalatra.auth.ScentrySupport
import org.scalatra.auth.ScentryConfig
import org.scalatra.auth.strategy.BasicAuthSupport
import org.scalatra.auth.strategy.BasicAuthStrategy
import org.scalatra.ScalatraBase


trait AuthenticationSupport extends ScentrySupport[User] with BasicAuthSupport[User]{
    self: ScalatraBase =>

    val realm = "E-Liberate"

    protected def fromSession = { case id: String => User(id) }
    protected def toSession = { case usr: User => "scalatra" }

    protected val scentryConfig = (new ScentryConfig {}).asInstanceOf[ScentryConfiguration]

    override protected def configureScentry = {
        scentry.unauthenticated {
            scentry.strategies("Basic").unauthenticated()
        }
    }

    override protected def registerAuthStrategies = {
        scentry.register("Basic", app => new ELiberateAuthStrategy(app, realm))
    }
}
