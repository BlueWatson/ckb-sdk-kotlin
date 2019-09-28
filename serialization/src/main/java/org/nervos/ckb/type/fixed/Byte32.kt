package org.nervos.ckb.type.fixed

import org.nervos.ckb.type.base.FixedType
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Byte32 : FixedType<ByteArray> {

    override var value: ByteArray
        private set

    override val length: Int
        get() = 32

    constructor(value: ByteArray) {
        if (value.size != 32) {
            throw UnsupportedOperationException("Byte32 length error")
        }
        this.value = value
    }

    constructor(value: String) {
        val bytes = Numeric.hexStringToByteArray(value)
        when {
            bytes.size > 32 -> throw UnsupportedOperationException("Byte32 length error")
            bytes.size < 32 -> {
                val dest = byteArrayOf(
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00,
                    0x00
                )
                System.arraycopy(bytes, 0, dest, 0, bytes.size)
                this.value = dest
            }
            else -> this.value = bytes
        }
    }

    override fun toBytes(): ByteArray {
        return value
    }
}
