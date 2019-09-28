package org.nervos.ckb

import org.nervos.ckb.type.base.Type

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
object Encoder {

    fun encode(type: Type<*>): ByteArray {
        return type.toBytes()
    }
}
