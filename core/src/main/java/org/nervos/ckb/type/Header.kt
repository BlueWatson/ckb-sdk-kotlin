package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class Header (
    var dao: String,
    var difficulty: String,
    var hash: String,
    var nonce: String,
    var number: String,
    var epoch: String,

    @SerializedName("parent_hash")
    var parentHash: String,

    var timestamp: String,

    @SerializedName("transactions_root")
    var transactionsRoot: String,

    @SerializedName("proposals_hash")
    var proposalsHash: String,

    @SerializedName("witnesses_root")
    var witnessesRoot: String,

    @SerializedName("uncles_count")
    var unclesCount: String,

    @SerializedName("uncles_hash")
    var unclesHash: String,

    var version: String
)
