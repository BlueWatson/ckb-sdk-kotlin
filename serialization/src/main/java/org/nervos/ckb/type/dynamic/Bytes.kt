package org.nervos.ckb.type.dynamic

import org.nervos.ckb.type.base.DynType
import org.nervos.ckb.type.fixed.UInt32
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Bytes : DynType<ByteArray?> {

    override var value: ByteArray? = null
        private set

    override val length: Int
        get() = UInt32.BYTE_SIZE + value!!.size

    constructor(value: ByteArray) {
        this.value = value
    }

    constructor(value: String) {
        this.value = Numeric.hexStringToByteArray(value)
    }

    override fun toBytes(): ByteArray {
        val dest = ByteArray(UInt32.BYTE_SIZE + value!!.size)
        System.arraycopy(UInt32(value!!.size).toBytes(), 0, dest, 0, UInt32.BYTE_SIZE)
        System.arraycopy(value!!, 0, dest, UInt32.BYTE_SIZE, value!!.size)
        return dest
    }

    companion object {

        fun fromBytes(bytes: ByteArray): Bytes {
            val dest = ByteArray(bytes.size - UInt32.BYTE_SIZE)
            System.arraycopy(bytes, UInt32.BYTE_SIZE, dest, 0, dest.size)
            return Bytes(dest)
        }
    }
}
