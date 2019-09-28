package org.nervos.ckb.type.transaction

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class TransactionWithStatus (

    @SerializedName("tx_status")
    var txStatus: TxStatus,
    var transaction: Transaction
)

class TxStatus (
    // pending / proposed / committed
    var status: String,
    @SerializedName("block_hash")
    var blockHash: String
)
