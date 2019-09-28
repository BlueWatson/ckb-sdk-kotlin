package org.nervos.ckb.crypto

import org.bouncycastle.crypto.digests.Blake2bDigest
import org.nervos.ckb.utils.Numeric

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class Blake2b {

    private val blake2bDigest: Blake2bDigest?

    init {
        blake2bDigest = Blake2bDigest(null, 32, null, Hash.CKB_HASH_PERSONALIZATION)
    }

    fun update(input: ByteArray) {
        blake2bDigest?.update(input, 0, input.size)
    }

    fun doFinalBytes(): ByteArray {
        val out = ByteArray(32)
        blake2bDigest?.doFinal(out, 0)
        return out
    }

    fun doFinalString(): String {
        return Numeric.toHexString(doFinalBytes())
    }
}
