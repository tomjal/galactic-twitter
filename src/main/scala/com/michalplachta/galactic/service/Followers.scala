package com.michalplachta.galactic.service

import com.michalplachta.galactic.db.DbClient
import com.michalplachta.galactic.values.Citizen

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object Followers {
  private var cachedFollowers: Map[String, Int] = Map.empty
  private var cachedTriedFollowers: Map[String, Try[Int]] = Map.empty

  // PROBLEM #1: treating 0 as "no value yet"
  def getFollowers(name: String): Int = {
    updateFollowersCacheAsync(name)
    val cachedResult: Option[Int] = cachedFollowers.get(name)
    cachedResult.getOrElse(0)
  }

  // SOLUTION #1: explicit return type
  // PROBLEM #2: not handling failures
  def getCachedFollowers(name: String): Option[Int] = {
    updateFollowersCacheAsync(name)
    cachedFollowers.get(name)
  }

  // SOLUTION #2: explicit return type
  def getCachedTriedFollowers(name: String): Option[Try[Int]] = {
    updateFollowersCacheAsync(name)
    cachedTriedFollowers.get(name)
  }

  private def updateFollowersCacheAsync(name: String): Unit = {
    val futureCitizen: Future[Citizen] = DbClient.findCitizenByName(name)
    val futureResult: Future[Int] = futureCitizen.flatMap {
      citizen ⇒ DbClient.getFollowers(citizen).mapTo[List[Citizen]].map(_.length)
    }
    futureResult.foreach { result ⇒
      cachedFollowers += (name → result)
    }
    futureResult.onComplete { result ⇒
      cachedTriedFollowers += (name → result)
    }
  }
}