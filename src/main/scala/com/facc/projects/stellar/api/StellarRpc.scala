package com.facc.projects.stellar.api

import cats.effect.IO
import com.facc.projects.stellar.model.Payment
import org.http4s.client.Client


trait BlockchainRpc {
  def getPaymentsPerLedger(blockNum: Long): List[Payment]
  def getStreamFrom(blockNum: Long): List[Payment]
}

case class StellarRpc(client: Client[IO]) extends BlockchainRpc {

  def getPaymentsPerLedger(blockNum: Long): List[Payment] = ???

  def getStreamFrom(blockNum: Long): List[Payment] = ???

}
