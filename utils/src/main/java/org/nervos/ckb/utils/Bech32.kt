/*
 * Copyright 2018 Coinomi Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nervos.ckb.utils

import java.util.Arrays
import java.util.Locale
import org.nervos.ckb.exceptions.AddressFormatException

object Bech32 {
    /** The Bech32 character set for encoding.  */
    private val CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l"

    /** The Bech32 character set for decoding.  */
    private val CHARSET_REV = byteArrayOf(
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        15,
        -1,
        10,
        17,
        21,
        20,
        26,
        30,
        7,
        5,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        29,
        -1,
        24,
        13,
        25,
        9,
        8,
        23,
        -1,
        18,
        22,
        31,
        27,
        19,
        -1,
        1,
        0,
        3,
        16,
        11,
        28,
        12,
        14,
        6,
        4,
        2,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        29,
        -1,
        24,
        13,
        25,
        9,
        8,
        23,
        -1,
        18,
        22,
        31,
        27,
        19,
        -1,
        1,
        0,
        3,
        16,
        11,
        28,
        12,
        14,
        6,
        4,
        2,
        -1,
        -1,
        -1,
        -1,
        -1
    )

    class Bech32Data(val hrp: String, val data: ByteArray)

    /** Find the polynomial with value coefficients mod the generator as 30-bit.  */
    private fun polymod(values: ByteArray): Int {
        var c = 1
        for (v_i in values) {
            val c0 = c.ushr(25) and 0xff
            c = c and 0x1ffffff shl 5 xor (v_i.toInt() and 0xff)
            if (c0 and 1 != 0) c = c xor 0x3b6a57b2
            if (c0 and 2 != 0) c = c xor 0x26508e6d
            if (c0 and 4 != 0) c = c xor 0x1ea119fa
            if (c0 and 8 != 0) c = c xor 0x3d4233dd
            if (c0 and 16 != 0) c = c xor 0x2a1462b3
        }
        return c
    }

    /** Expand a HRP for use in checksum computation.  */
    private fun expandHrp(hrp: String): ByteArray {
        val hrpLength = hrp.length
        val ret = ByteArray(hrpLength * 2 + 1)
        for (i in 0 until hrpLength) {
            val c = hrp[i].toInt() and 0x7f // Limit to standard 7-bit ASCII
            ret[i] = (c.ushr(5) and 0x07).toByte()
            ret[i + hrpLength + 1] = (c and 0x1f).toByte()
        }
        ret[hrpLength] = 0
        return ret
    }

    /** Verify a checksum.  */
    private fun verifyChecksum(hrp: String, values: ByteArray): Boolean {
        val hrpExpanded = expandHrp(hrp)
        val combined = ByteArray(hrpExpanded.size + values.size)
        System.arraycopy(hrpExpanded, 0, combined, 0, hrpExpanded.size)
        System.arraycopy(values, 0, combined, hrpExpanded.size, values.size)
        return polymod(combined) == 1
    }

    /** Create a checksum.  */
    private fun createChecksum(hrp: String, values: ByteArray): ByteArray {
        val hrpExpanded = expandHrp(hrp)
        val enc = ByteArray(hrpExpanded.size + values.size + 6)
        System.arraycopy(hrpExpanded, 0, enc, 0, hrpExpanded.size)
        System.arraycopy(values, 0, enc, hrpExpanded.size, values.size)
        val mod = polymod(enc) xor 1
        val ret = ByteArray(6)
        for (i in 0..5) {
            ret[i] = (mod.ushr(5 * (5 - i)) and 31).toByte()
        }
        return ret
    }

    /** Encode a Bech32 string.  */
    fun encode(bech32: Bech32Data): String {
        return encode(bech32.hrp, bech32.data)
    }

    /** Encode a Bech32 string.  */
    fun encode(hrp: String, values: ByteArray): String {
        var hrp = hrp
        if (hrp.isEmpty())
            throw AddressFormatException.InvalidDataLength(
                "Human-readable part is too short: " + hrp.length
            )
        if (hrp.length > 83)
            throw AddressFormatException.InvalidDataLength(
                "Human-readable part is too long: " + hrp.length
            )
        hrp = hrp.toLowerCase(Locale.ROOT)
        val checksum = createChecksum(hrp, values)
        val combined = ByteArray(values.size + checksum.size)
        System.arraycopy(values, 0, combined, 0, values.size)
        System.arraycopy(checksum, 0, combined, values.size, checksum.size)
        val sb = StringBuilder(hrp.length + 1 + combined.size)
        sb.append(hrp)
        sb.append('1')
        for (b in combined) {
            sb.append(CHARSET[b.toInt()])
        }
        return sb.toString()
    }

    /** Decode a Bech32 string.  */
    @Throws(AddressFormatException::class)
    fun decode(str: String): Bech32Data {
        var lower = false
        var upper = false
        if (str.length < 8)
            throw AddressFormatException.InvalidDataLength("Input too short: " + str.length)
        if (str.length > 90)
            throw AddressFormatException.InvalidDataLength("Input too long: " + str.length)
        for (i in 0 until str.length) {
            val c = str[i]
            if (c.toInt() < 33 || c.toInt() > 126) throw AddressFormatException.InvalidCharacter(c, i)
            if (c in 'a'..'z') {
                if (upper) throw AddressFormatException.InvalidCharacter(c, i)
                lower = true
            }
            if (c in 'A'..'Z') {
                if (lower) throw AddressFormatException.InvalidCharacter(c, i)
                upper = true
            }
        }
        val pos = str.lastIndexOf('1')
        if (pos < 1) throw AddressFormatException.InvalidPrefix("Missing human-readable part")
        val dataPartLength = str.length - 1 - pos
        if (dataPartLength < 6)
            throw AddressFormatException.InvalidDataLength("Data part too short: $dataPartLength")
        val values = ByteArray(dataPartLength)
        for (i in 0 until dataPartLength) {
            val c = str[i + pos + 1]
            if (CHARSET_REV[c.toInt()].toInt() == -1) throw AddressFormatException.InvalidCharacter(c, i + pos + 1)
            values[i] = CHARSET_REV[c.toInt()]
        }
        val hrp = str.substring(0, pos).toLowerCase(Locale.ROOT)
        if (!verifyChecksum(hrp, values)) throw AddressFormatException.InvalidChecksum()
        return Bech32Data(hrp, Arrays.copyOfRange(values, 0, values.size - 6))
    }
}
