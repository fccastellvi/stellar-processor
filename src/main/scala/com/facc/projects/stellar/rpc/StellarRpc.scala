package com.facc.projects.stellar.rpc

import cats.effect.IO
import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.model.Config._
import com.facc.projects.stellar.model.{Payment, PaymentResponse}
import fs2.{Chunk, Stream}
import io.circe.generic.auto._
import org.http4s.circe.jsonOf
import org.http4s.client.Client

case class StellarRpc(client: Client[IO]) extends BlockchainRpc {

  def getVTEPerBlock(blockNum: Long): IO[List[Payment]] =
    client
      .expect[PaymentResponse](baseUrl.withPath(s"/ledgers/$blockNum/payments"))(jsonOf[IO, PaymentResponse])
      .map(_._embedded.records)
      .handleErrorWith{_ =>  IO(List.empty[Payment])}

  def getStreamFrom(blockNum: Long): Stream[IO, Chunk[Payment]] = {
    Stream
      .fromIterator[IO, Long](Iterator.iterate(blockNum)(_ + 1))
      .evalMap(getVTEPerBlock)
      .flatMap(x => fs2.Stream.apply(x: _*))
      .chunkLimit(1)
  }
}
