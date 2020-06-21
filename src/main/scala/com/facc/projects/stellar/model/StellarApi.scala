package com.facc.projects.stellar.model

case class VTE(paging_token: String,
               transaction_successful: Boolean,
               source_account: String,
               `type`: String,
               created_at: String,
               transaction_hash: String,
               asset_type: String,
               from: String,
               to: String,
               amount: String)

case class Records(records: List[VTE])

case class PaymentResponse(_embedded: Records)

