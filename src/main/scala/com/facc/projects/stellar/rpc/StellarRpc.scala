package com.facc.projects.stellar.rpc

import cats.effect.IO
import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.model.Config._
import com.facc.projects.stellar.model.{Payment, PaymentResponse}
import fs2.{Chunk, Stream}
import io.circe.generic.auto._
import org.http4s.InvalidMessageBodyFailure
import org.http4s.circe.{DecodingFailures, jsonOf}
import org.http4s.client.Client

case class StellarRpc(client: Client[IO]) extends BlockchainRpc {

  /**
   *
   * @param blockNum Block number or ledger id to get the payments from
   * @return
   */
  def getVTEPerBlock(blockNum: Long): IO[List[Payment]] =
    client
      .expect[PaymentResponse](baseUrl.withPath(s"/ledgers/$blockNum/payments"))(jsonOf[IO, PaymentResponse])
      .map(_._embedded.records)
      .handleErrorWith{case _:DecodingFailures | _:InvalidMessageBodyFailure=> IO(List.empty[Payment])}

  /**
   * Note: All the decoding exceptions caught in getVTEPerBlock will return an empty list which
   * will be handled when emitting the stream `Stream.emits`
   * @param blockNum Start block number to iterate over
   * @return An infinite stream containing all the payments from the specified blockNum onwards
   *
   */
  def getStreamFrom(blockNum: Long): Stream[IO, Chunk[Payment]] = {
    fs2.Stream
      .fromIterator[IO, Long](Iterator.iterate(blockNum)(_ + 1))
      .evalMap(getVTEPerBlock)
      .flatMap(fs2.Stream.emits(_))
      .chunkLimit(1)
  }
}