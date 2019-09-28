package org.nervos.ckb.utils

import java.util.Collections

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
object Strings {
    fun zeros(n: Int): String {
        return repeat('0', n)
    }

    fun repeat(value: Char, n: Int): String {
        return String(CharArray(n)).replace("\u0000", value.toString())
    }

    fun isEmpty(s: String?): Boolean {
        return s == null || s.isEmpty()
    }

    fun asciiToHex(asciiValue: String, length: Int): ByteArray {
        val chars = asciiValue.toCharArray()
        val hex = StringBuilder()
        for (i in chars.indices) {
            hex.append(Integer.toHexString(chars[i].toInt()))
        }

        val hexStr = hex.toString() + Collections.nCopies(length - hex.length / 2, "00").joinToString("")
        return Numeric.hexStringToByteArray(hexStr)
    }

    fun hexStringToAscii(hexStr: String): String {
        assert(hexStr.length % 2 == 0)
        val asciiStr = StringBuilder()
        var i = 0
        while (i < hexStr.length) {
            val str = hexStr.substring(i, i + 2)
            if (str == "00") {
                break
            }
            asciiStr.append(Integer.parseInt(str, 16).toChar())
            i += 2
        }
        return asciiStr.toString()
    }
}
