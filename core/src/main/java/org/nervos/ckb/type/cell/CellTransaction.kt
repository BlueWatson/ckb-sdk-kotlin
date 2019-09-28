package org.nervos.ckb.type.cell

import com.google.gson.annotations.SerializedName
import org.nervos.ckb.type.transaction.TransactionPoint

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class CellTransaction(
    @SerializedName("created_by") var createdBy: TransactionPoint,
    @SerializedName("consumed_by") var consumedBy: TransactionPoint)

