package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class Header (
    var dao: String,
    var hash: String,
    var nonce: String,
    var number: String,
    var epoch: String,

    @SerializedName("compact_target")
    var compactTarget: String,

    @SerializedName("parent_hash")
    var parentHash: String,

    var timestamp: String,

    @SerializedName("transactions_root")
    var transactionsRoot: String,

    @SerializedName("proposals_hash")
    var proposalsHash: String,

    @SerializedName("uncles_hash")
    var unclesHash: String,

    var version: String
)
