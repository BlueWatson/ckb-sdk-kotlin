package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class PeerState (
    @SerializedName("last_updated")
    var lastUpdated: String,

    @SerializedName("blocks_in_flight")
    var blocksInFlight: String,

    var peer: String
)
