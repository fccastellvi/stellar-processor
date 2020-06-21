package com.facc.projects.stellar.rpc

import cats.effect.IO
import com.facc.projects.stellar.model.Payment
import fs2.{Chunk, Stream}

trait BlockchainRpc {
  def getVTEPerBlock(blockNum: Long): IO[List[Payment]]
  def getStreamFrom(blockNum: Long): Stream[IO, Chunk[Payment]]
}