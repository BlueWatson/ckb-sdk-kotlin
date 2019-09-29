package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Epoch (
    var number: String,

    @SerializedName("start_number")
    var startNumber: String,

    var length: String,

    @SerializedName("compact_target")
    var compactTarget: String
)
