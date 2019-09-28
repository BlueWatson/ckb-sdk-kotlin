package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class NodeInfo (
    @SerializedName("node_id")
    var nodeId: String,

    var version: String,
    var addresses: List<Address>)

class Address (var address: String, var score: String)

