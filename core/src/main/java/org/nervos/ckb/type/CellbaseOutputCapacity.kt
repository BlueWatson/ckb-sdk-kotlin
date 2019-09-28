package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class CellbaseOutputCapacity (
    var primary: String,

    @SerializedName("proposal_reward")
    var proposalReward: String,

    var secondary: String,
    var total: String,

    @SerializedName("tx_fee")
    var txFee: String
)
