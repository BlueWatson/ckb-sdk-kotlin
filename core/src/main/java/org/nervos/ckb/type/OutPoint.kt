package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class OutPoint(@SerializedName("tx_hash") var txHash: String, var index: String)
