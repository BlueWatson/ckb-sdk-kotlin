package org.nervos.ckb.type.cell

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
data class CellWithStatus (var cell: CellInfo, var status: String)

data class CellInfo (var data: CellData, var output: CellOutput)
data class CellData (var content: String, var hash: String)


