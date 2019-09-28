package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class BlockchainInfo (
    @SerializedName("is_initial_block_download")
    var isInitialBlockDownload: Boolean,

    var epoch: String,
    var difficulty: String,

    @SerializedName("median_time")
    var medianTime: String,

    var chain: String,
    var alerts: List<AlertMessage>
)
