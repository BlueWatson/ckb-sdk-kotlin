package org.nervos.ckb.type.base

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
interface Type<T> {

    val value: T

    val length: Int

    fun toBytes(): ByteArray
}
