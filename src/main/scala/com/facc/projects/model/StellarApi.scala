package com.facc.projects.model

case class Payment(paging_token: String,
                   transaction_successful: Boolean,
                   source_account: String,
                   `type`: String,
                   created_at: String,
                   transaction_hash: String,
                   asset_type: String,
                   from: String,
                   to: String,
                   amount: String)

case class Records(records: List[Payment])

case class StellarPaymentResponse(_embedded: Records)

