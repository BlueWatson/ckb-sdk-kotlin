package org.nervos.ckb.type.fixed

import org.nervos.ckb.type.base.FixedType

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Empty : FixedType<Any?>() {

    override val value: Any?
        get() = null

    override val length: Int
        get() = 0

    override fun toBytes(): ByteArray {
        return ByteArray(0)
    }
}
