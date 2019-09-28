package org.nervos.ckb.type.fixed

import org.nervos.ckb.type.base.FixedType
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class UInt32 : FixedType<Long> {

    override var value: Long
        private set

    override val length: Int
        get() = BYTE_SIZE

    constructor(value: Long) {
        this.value = value
    }

    constructor(value: Int) {
        this.value = value.toLong()
    }

    constructor(hexValue: String) {
        this.value = Numeric.toBigInt(hexValue).toLong()
    }

    /*
   * @return little endian bytes
   * */
    override fun toBytes(): ByteArray {
        val result = ByteArray(4)
        result[3] = (value shr 24 and 0xff).toByte()
        result[2] = (value shr 16 and 0xff).toByte()
        result[1] = (value shr 8 and 0xff).toByte()
        result[0] = (value and 0xff).toByte()
        return result
    }

    companion object {
        const val BYTE_SIZE = 4
    }
}
