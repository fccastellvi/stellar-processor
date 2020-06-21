package com.facc.projects.stellar.rpc

import cats.effect.IO
import com.facc.projects.stellar.model.VTE
import fs2.{Chunk, Stream}

trait BlockchainRpc {
  def getVTEPerBlock(blockNum: Long): IO[List[VTE]]
  def getStreamFrom(blockNum: Long): Stream[IO, VTE]
}