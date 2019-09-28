package org.nervos.ckb.type.cell

import com.google.gson.annotations.SerializedName
import org.nervos.ckb.type.OutPoint

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class CellDep(
    @SerializedName("out_point")
    var outPoint: OutPoint,

    @SerializedName("dep_type")
    var depType: String = CODE
) {

    companion object {
        const val CODE = "code"
        const val DEP_GROUP = "dep_group"
    }
}
