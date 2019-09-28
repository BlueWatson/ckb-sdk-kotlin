package org.nervos.ckb.type.cell

import com.google.gson.annotations.SerializedName
import org.nervos.ckb.type.transaction.TransactionPoint

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class LiveCell (
    @SerializedName("created_by")
    var createdBy: TransactionPoint,

    var cellOutput: CellOutput
)
