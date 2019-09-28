package org.nervos.ckb.type.transaction

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class TransactionPoint (
    @SerializedName("block_number")
    var blockNumber: String,

    @SerializedName("tx_hash")
    var txHash: String,

    var index: String
)
