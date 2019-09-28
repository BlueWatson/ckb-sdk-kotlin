package org.nervos.ckb.type.fixed

import org.nervos.ckb.type.base.FixedType
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Byte1 : FixedType<ByteArray> {

    override var value: ByteArray
        private set

    override val length: Int
        get() = 1

    constructor(value: ByteArray) {
        if (value.size != 1) {
            throw UnsupportedOperationException("Byte1 length error")
        }
        this.value = value
    }

    constructor(value: String) {
        this.value = Numeric.hexStringToByteArray(value)
    }

    override fun toBytes(): ByteArray {
        return value
    }
}
