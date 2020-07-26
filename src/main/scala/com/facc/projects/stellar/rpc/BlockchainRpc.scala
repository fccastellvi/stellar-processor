package com.facc.projects.stellar.rpc

import fs2.Stream
import org.http4s.client.Client

trait BlockchainRpc[F[_], VTE] {
  val client: Client[F]
  def getVTEPerBlock(blockNum: Long): F[List[VTE]]
  def getStreamFrom(blockNum: Long): Stream[F, VTE]
}