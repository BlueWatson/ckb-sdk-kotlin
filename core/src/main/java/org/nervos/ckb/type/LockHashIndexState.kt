package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class LockHashIndexState (
    @SerializedName("lock_hash")
    var lockHash: String,

    @SerializedName("block_number")
    var blockNumber: String,

    @SerializedName("block_hash")
    var blockHash: String
)
