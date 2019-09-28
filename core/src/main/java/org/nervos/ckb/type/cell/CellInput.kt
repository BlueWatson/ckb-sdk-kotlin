package org.nervos.ckb.type.cell

import com.google.gson.annotations.SerializedName
import org.nervos.ckb.type.OutPoint

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class CellInput(@SerializedName("previous_output") var previousOutput: OutPoint, var since: String)
