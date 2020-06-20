package com.facc.projects.stellar.api

import cats.effect.IO
import com.facc.projects.stellar.model.Payment
import org.http4s.client.Client

case class StellarRpc(client: Client[IO]) {

  def getPaymentsPerLedger(ledgerId: Long): List[Payment] = ???

  def getStreamFrom(ledgerId: Long): List[Payment] = ???

}
