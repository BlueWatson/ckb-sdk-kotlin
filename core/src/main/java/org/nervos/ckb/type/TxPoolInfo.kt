package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class TxPoolInfo (
    var pending: String,
    var staging: String,
    var orphan: String,

    @SerializedName("last_txs_updated_at")
    var lastTxsUpdatedAt: String,

    @SerializedName("total_tx_cycles")
    var totalTxCycles: String,

    @SerializedName("total_tx_size")
    var totalTxSize: String
)
