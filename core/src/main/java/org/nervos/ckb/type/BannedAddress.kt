package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class BannedAddress(
    var address: String,
    var command: String,
    @SerializedName("ban_time") var banTime: String,
    var absolute: Boolean,
    var reason: String
)
