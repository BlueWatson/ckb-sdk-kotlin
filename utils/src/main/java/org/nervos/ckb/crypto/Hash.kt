package org.nervos.ckb.crypto

import java.nio.charset.StandardCharsets
import org.bouncycastle.crypto.digests.Blake2bDigest
import org.nervos.ckb.utils.Numeric

/** Cryptographic hash functions.  */
object Hash {
    internal val CKB_HASH_PERSONALIZATION = "ckb-default-hash".toByteArray(StandardCharsets.UTF_8)

    /**
     * Blake2b-256 hash function.
     *
     * @param hexInput hex encoded input data with optional 0x prefix
     * @return hash value as hex encoded string
     */
    @JvmStatic
    fun blake2b(hexInput: String): String {
        val bytes = Numeric.hexStringToByteArray(hexInput)
        val result = blake2b(bytes)
        return Numeric.toHexString(result)
    }

    /**
     * Blake2b-256 hash function.
     *
     * @param input binary encoded input data
     * @param offset of start of data
     * @param length of data
     * @return hash value
     */
    @JvmStatic
    fun blake2b(input: ByteArray, offset: Int = 0, length: Int = input.size): ByteArray {
        val blake2b = Blake2bDigest(null, 32, null, CKB_HASH_PERSONALIZATION)
        blake2b.update(input, offset, length)
        val out = ByteArray(32)
        blake2b.doFinal(out, 0)
        return out
    }

    /**
     * Blake2b-256 hash function that operates on a UTF-8 encoded String.
     *
     * @param utf8String UTF-8 encoded string
     * @return hash value as hex encoded string
     */
    @JvmStatic
    fun blake2bString(utf8String: String): String {
        return Numeric.toHexString(blake2b(utf8String.toByteArray(StandardCharsets.UTF_8)))
    }
}
/**
 * Blake2b-256 hash function.
 *
 * @param input binary encoded input data
 * @return hash value
 */
