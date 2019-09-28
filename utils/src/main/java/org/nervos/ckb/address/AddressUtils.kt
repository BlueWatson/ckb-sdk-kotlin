package org.nervos.ckb.address

import com.google.common.primitives.Bytes
import java.util.ArrayList
import org.nervos.ckb.crypto.Hash
import org.nervos.ckb.exceptions.AddressFormatException
import org.nervos.ckb.utils.Bech32
import org.nervos.ckb.utils.Network
import org.nervos.ckb.utils.Numeric

/**
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 *
 *
 * AddressUtils based on CKB Address Format
 * [RFC](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md),
 * and [Common Address Format](https://github.com/nervosnetwork/ckb/wiki/Common-Address-Format).
 * Currently we implement the predefined format for type 0x01 and code hash index 0x00.
 */
class AddressUtils(private val network: Network) {

    @Throws(AddressFormatException::class)
    fun generateFromPublicKey(publicKey: String): String {
        return generate(blake160(publicKey))
    }

    @Throws(AddressFormatException::class)
    fun generate(args: String): String {
        // Payload: type(01) | code hash index(00, P2PH) | args
        val payload = TYPE + CODE_HASH_IDX + Numeric.cleanHexPrefix(args)
        val data = Numeric.hexStringToByteArray(payload)
        return Bech32.encode(prefix(), convertBits(Bytes.asList(*data), 8, 5, true))
    }

    @Throws(AddressFormatException::class)
    fun parse(address: String): Bech32.Bech32Data? {
        val parsed = Bech32.decode(address)
        val data = convertBits(Bytes.asList(*parsed.data), 5, 8, false)
        return if (data.isEmpty()) null else Bech32.Bech32Data(parsed.hrp, data)
    }

    @Throws(AddressFormatException::class)
    fun getBlake160FromAddress(address: String): String {
        val bech32Data = parse(address)
        val payload = Numeric.toHexString(bech32Data!!.data)
        val prefix = TYPE + CODE_HASH_IDX
        return payload.replace(prefix, "")
    }

    private fun prefix(): String {
        return if (network == Network.MAINNET) "ckb" else "ckt"
    }

    fun blake160(value: String): String {
        return Numeric.cleanHexPrefix(Hash.blake2b(value)).substring(0, 40)
    }

    fun strToAscii(value: String): String {
        val sb = StringBuilder()
        val chars = value.toCharArray()
        for (i in chars.indices) {
            sb.append(Integer.toHexString(chars[i].toInt()))
        }
        return sb.toString()
    }

    @Throws(AddressFormatException::class)
    fun convertBits(data: List<Byte>, fromBits: Int, toBits: Int, pad: Boolean): ByteArray {
        var acc = 0
        var bits = 0
        val maxv = (1 shl toBits) - 1
        val ret = ArrayList<Byte>()
        for (value in data) {
            val b = (value.toInt() and 0xff)
            if (b shr fromBits > 0) {
                throw AddressFormatException()
            }
            acc = acc shl fromBits or b
            bits += fromBits
            while (bits >= toBits) {
                bits -= toBits
                ret.add((acc shr bits and maxv).toByte())
            }
        }
        if (pad && bits > 0) {
            ret.add((acc shl toBits - bits and maxv).toByte())
        } else if (bits >= fromBits || (acc shl toBits - bits and maxv).toByte().toInt() != 0) {
            throw AddressFormatException(
                "Strict mode was used but input couldn't be converted without padding"
            )
        }
        return Bytes.toArray(ret)
    }

    companion object {
        private val TYPE = "01"
        private val CODE_HASH_IDX = "00"
    }
}
