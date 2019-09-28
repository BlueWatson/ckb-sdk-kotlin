package org.nervos.ckb.type.fixed

import java.math.BigInteger
import org.nervos.ckb.type.base.FixedType
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class UInt64 : FixedType<BigInteger> {

    override var value: BigInteger
        private set

    override val length: Int
        get() = 8

    constructor(value: Long) {
        this.value = BigInteger.valueOf(value)
    }

    constructor(value: String) {
        this.value = Numeric.toBigInt(value)
    }

    // generate int value from little endian bytes
    internal constructor(bytes: ByteArray) {
        var result: Long = 0
        for (i in 7 downTo 0) {
            result += bytes[i].toLong() and 0xff shl (8 * i)
        }
        this.value = BigInteger.valueOf(result)
    }

    /*
   * @return little endian bytes
   * */
    override fun toBytes(): ByteArray {
        val result = ByteArray(8)
        val valueLong = value.toLong()
        result[7] = (valueLong shr 56 and 0xff).toByte()
        result[6] = (valueLong shr 48 and 0xff).toByte()
        result[5] = (valueLong shr 40 and 0xff).toByte()
        result[4] = (valueLong shr 32 and 0xff).toByte()
        result[3] = (valueLong shr 24 and 0xff).toByte()
        result[2] = (valueLong shr 16 and 0xff).toByte()
        result[1] = (valueLong shr 8 and 0xff).toByte()
        result[0] = (valueLong and 0xff).toByte()
        return result
    }
}
