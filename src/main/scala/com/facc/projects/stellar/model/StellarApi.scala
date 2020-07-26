package com.facc.projects.stellar.model


trait GenericVTE extends Object{
  val from: String
  val to: String
  val amount: String
  val created_at: String
}

case class StellarVTE(paging_token: String,
                      transaction_successful: Boolean,
                      source_account: String,
                      `type`: String,
                      created_at: String,
                      transaction_hash: String,
                      asset_type: String,
                      from: String,
                      to: String,
                      amount: String) extends GenericVTE

case class Records(records: List[StellarVTE])

case class PaymentResponse(_embedded: Records)

