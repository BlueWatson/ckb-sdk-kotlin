package org.nervos.ckb.type.fixed

import org.nervos.ckb.type.base.FixedType
import org.nervos.ckb.type.base.Type

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Fixed<T : FixedType<*>>(_value: List<T>) : Type<List<T>> {

    override val value: List<T> = _value

    override val length: Int
        get() {
            var length = UInt32.BYTE_SIZE
            for (type in value) {
                length += type.length
            }
            return length
        }

    override fun toBytes(): ByteArray {

        val fullLength = length
        val dest = ByteArray(fullLength)

        // full length bytes
        System.arraycopy(UInt32(value.size).toBytes(), 0, dest, 0, UInt32.BYTE_SIZE)

        var offset = UInt32.BYTE_SIZE
        for (type in value) {
            // Bytes through offset
            System.arraycopy(type.toBytes(), 0, dest, offset, type.length)
            offset += type.length
        }
        return dest
    }
}
