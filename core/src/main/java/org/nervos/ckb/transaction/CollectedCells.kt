package org.nervos.ckb.transaction

import java.math.BigInteger
import org.nervos.ckb.type.cell.CellInput

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class CollectedCells internal constructor(var inputs: List<CellInput>, var capacity: BigInteger)
