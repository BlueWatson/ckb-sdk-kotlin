package org.nervos.ckb.utils

import java.math.BigDecimal
import java.math.BigInteger
import java.util.Arrays
import org.nervos.ckb.exceptions.MessageDecodingException
import org.nervos.ckb.exceptions.MessageEncodingException

/** Message codec functions.  */
object Numeric {

    private val HEX_PREFIX = "0x"

    @JvmStatic
    fun encodeQuantity(value: BigInteger): String {
        return if (value.signum() != -1) {
            HEX_PREFIX + value.toString(16)
        } else {
            throw MessageEncodingException("Negative values are not supported")
        }
    }

    @JvmStatic
    fun decodeQuantity(value: String): BigInteger {
        if (!isValidHexQuantity(value)) {
            throw MessageDecodingException("Value must be in format 0x[1-9]+[0-9]* or 0x0")
        }
        try {
            return BigInteger(value.substring(2), 16)
        } catch (e: NumberFormatException) {
            throw MessageDecodingException("Negative ", e)
        }
    }

    @JvmStatic
    private fun isValidHexQuantity(value: String?): Boolean {
        if (value == null) {
            return false
        }

        return if (value.length < 3) {
            false
        } else value.startsWith(HEX_PREFIX)
    }

    @JvmStatic
    fun cleanHexPrefix(input: String): String {
        return if (containsHexPrefix(input)) {
            input.substring(2)
        } else {
            input
        }
    }

    @JvmStatic
    fun prependHexPrefix(input: String): String {
        return if (!containsHexPrefix(input)) {
            HEX_PREFIX + input
        } else {
            input
        }
    }

    @JvmStatic
    fun containsHexPrefix(input: String): Boolean {
        return input.length > 1 && input[0] == '0' && input[1] == 'x'
    }

    @JvmStatic
    fun toBigInt(value: ByteArray, offset: Int, length: Int): BigInteger {
        return toBigInt(Arrays.copyOfRange(value, offset, offset + length))
    }

    @JvmStatic
    fun toBigInt(value: ByteArray): BigInteger {
        return BigInteger(1, value)
    }

    @JvmStatic
    fun toBigInt(hexValue: String): BigInteger {
        val cleanValue = cleanHexPrefix(hexValue)
        return toBigIntNoPrefix(cleanValue)
    }

    @JvmStatic
    fun toBigIntNoPrefix(hexValue: String): BigInteger {
        return BigInteger(hexValue, 16)
    }

    @JvmStatic
    fun toHexStringWithPrefix(value: BigInteger): String {
        return HEX_PREFIX + value.toString(16)
    }

    @JvmStatic
    fun toHexStringNoPrefix(value: BigInteger): String {
        return value.toString(16)
    }

    @JvmStatic
    fun toHexStringNoPrefix(input: ByteArray): String {
        return toHexString(input, 0, input.size, false)
    }

    @JvmStatic
    fun toHexStringWithPrefixZeroPadded(value: BigInteger, size: Int): String {
        return toHexStringZeroPadded(value, size, true)
    }

    @JvmStatic
    fun toHexStringNoPrefixZeroPadded(value: BigInteger, size: Int): String {
        return toHexStringZeroPadded(value, size, false)
    }

    @JvmStatic
    private fun toHexStringZeroPadded(value: BigInteger, size: Int, withPrefix: Boolean): String {
        var result = toHexStringNoPrefix(value)

        val length = result.length
        if (length > size) {
            throw UnsupportedOperationException("Value " + result + "is larger then length " + size)
        } else if (value.signum() < 0) {
            throw UnsupportedOperationException("Value cannot be negative")
        }

        if (length < size) {
            result = Strings.zeros(size - length) + result
        }

        return if (withPrefix) {
            HEX_PREFIX + result
        } else {
            result
        }
    }

    @JvmStatic
    fun toBytesPadded(value: BigInteger, length: Int): ByteArray {
        val result = ByteArray(length)
        val bytes = value.toByteArray()

        val bytesLength: Int
        val srcOffset: Int
        if (bytes[0].toInt() == 0) {
            bytesLength = bytes.size - 1
            srcOffset = 1
        } else {
            bytesLength = bytes.size
            srcOffset = 0
        }

        if (bytesLength > length) {
            throw RuntimeException("Input is too large to put in byte array of size $length")
        }

        val destOffset = length - bytesLength
        System.arraycopy(bytes, srcOffset, result, destOffset, bytesLength)
        return result
    }

    @JvmStatic
    fun littleEndian(number: Long): String {
        val bytes = Numeric.toBytesPadded(BigInteger.valueOf(number), 8)
        for (i in 0 until bytes.size / 2) {
            val temp = bytes[i]
            bytes[i] = bytes[bytes.size - 1 - i]
            bytes[bytes.size - 1 - i] = temp
        }
        return toHexString(bytes)
    }

    @JvmStatic
    fun hexStringToByteArray(input: String): ByteArray {
        val cleanInput = cleanHexPrefix(input)

        val len = cleanInput.length

        if (len == 0) {
            return byteArrayOf()
        }

        val data: ByteArray
        val startIdx: Int
        if (len % 2 != 0) {
            data = ByteArray(len / 2 + 1)
            data[0] = Character.digit(cleanInput[0], 16).toByte()
            startIdx = 1
        } else {
            data = ByteArray(len / 2)
            startIdx = 0
        }

        var i = startIdx
        while (i < len) {
            data[(i + 1) / 2] =
                ((Character.digit(cleanInput[i], 16) shl 4) + Character.digit(cleanInput[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    @JvmOverloads
    fun toHexString(input: ByteArray, offset: Int = 0, length: Int = input.size, withPrefix: Boolean = true): String {
        val stringBuilder = StringBuilder()
        if (withPrefix) {
            stringBuilder.append("0x")
        }
        for (i in offset until offset + length) {
            stringBuilder.append(String.format("%02x", input[i].toInt() and 0xFF))
        }

        return stringBuilder.toString()
    }


    @JvmStatic
    fun toHexString(input: String): String {
        try {
            return if (Numeric.containsHexPrefix(input))
                input
            else
                Numeric.toHexStringWithPrefix(BigInteger(input))
        } catch (e: NumberFormatException) {
            throw NumberFormatException(
                "Input parameter format error, please input integer or hex string"
            )
        }
    }

    @JvmStatic
    fun asByte(m: Int, n: Int): Byte {
        return (m shl 4 or n).toByte()
    }

    @JvmStatic
    fun isIntegerValue(value: BigDecimal): Boolean {
        return value.signum() == 0 || value.scale() <= 0 || value.stripTrailingZeros().scale() <= 0
    }

}
