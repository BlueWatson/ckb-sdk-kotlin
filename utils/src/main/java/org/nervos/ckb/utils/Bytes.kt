package org.nervos.ckb.utils

import java.util.Arrays

/** Byte array utility functions.  */
object Bytes {

    fun trimLeadingBytes(bytes: ByteArray, b: Byte): ByteArray {
        var offset = 0
        while (offset < bytes.size - 1) {
            if (bytes[offset] != b) {
                break
            }
            offset++
        }
        return Arrays.copyOfRange(bytes, offset, bytes.size)
    }

    fun trimLeadingZeroes(bytes: ByteArray): ByteArray {
        return trimLeadingBytes(bytes, 0.toByte())
    }
}
