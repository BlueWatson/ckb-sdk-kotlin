package org.nervos.ckb.type.cell

import com.google.gson.annotations.SerializedName
import org.nervos.ckb.type.OutPoint
import org.nervos.ckb.type.Script

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class CellOutputWithOutPoint(
    @SerializedName("block_hash") var blockHash: String,
    var capacity: String,
    var lock: Script,
    @SerializedName("out_point") var outPoint: OutPoint)
