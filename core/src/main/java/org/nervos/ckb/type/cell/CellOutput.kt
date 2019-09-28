package org.nervos.ckb.type.cell

import org.nervos.ckb.type.Script

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class CellOutput @JvmOverloads constructor(var capacity: String, var lock: Script, var type: Script? = null)
