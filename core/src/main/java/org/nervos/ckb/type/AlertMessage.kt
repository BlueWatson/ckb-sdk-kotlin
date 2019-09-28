package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class AlertMessage (
    var id: String,
    var priority: String,

    @SerializedName("notice_until")
    var noticeUntil: String,

    var message: String? = null
)
