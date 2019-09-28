package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class BannedResultAddress (
    var address: String,

    @SerializedName("ban_reason")
    var banReason: String,

    @SerializedName("ban_until")
    var banUntil: String,

    @SerializedName("created_at")
    var createdAt: String
)
