package org.nervos.ckb.type.fixed

import org.nervos.ckb.type.base.FixedType
import org.nervos.ckb.type.base.Type

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Struct(_value: List<Type<*>>) : FixedType<List<Type<*>>>() {

    override val value: List<Type<*>> = _value

    override val length: Int
        get() {
            var length = 0
            for (type in value) {
                length += type.length
            }
            return length
        }

    override fun toBytes(): ByteArray {
        val length = length
        val dest = ByteArray(length)
        var offset = 0
        for (type in value) {
            System.arraycopy(type.toBytes(), 0, dest, offset, type.length)
            offset += type.length
        }
        return dest
    }

}
