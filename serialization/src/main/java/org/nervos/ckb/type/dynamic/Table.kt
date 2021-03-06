package org.nervos.ckb.type.dynamic

import org.nervos.ckb.type.base.DynType
import org.nervos.ckb.type.base.Type
import org.nervos.ckb.type.fixed.UInt32

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Table(_value: List<Type<*>>) : DynType<List<Type<*>>>() {

    override val value: List<Type<*>> = _value

    override val length: Int
        get() {
            var length = 0
            for (type in value) {
                length += type.length
            }
            return length + (1 + value.size) * UInt32.BYTE_SIZE
        }

    override fun toBytes(): ByteArray {
        val length = length
        val dest = ByteArray(length)
        System.arraycopy(UInt32(length).toBytes(), 0, dest, 0, UInt32.BYTE_SIZE)

        var offset = UInt32.BYTE_SIZE
        var bytesOffset = UInt32.BYTE_SIZE * (1 + value.size)

        for (type in value) {
            // offset of every Bytes
            val offsetBytes = UInt32(bytesOffset).toBytes()
            System.arraycopy(offsetBytes, 0, dest, offset, UInt32.BYTE_SIZE)

            // Bytes through offset
            System.arraycopy(type.toBytes(), 0, dest, bytesOffset, type.length)

            offset += UInt32.BYTE_SIZE
            bytesOffset += type.length
        }

        return dest
    }
}
