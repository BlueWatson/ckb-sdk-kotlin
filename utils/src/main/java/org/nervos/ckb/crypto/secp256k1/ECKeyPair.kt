package org.nervos.ckb.crypto.secp256k1

import org.nervos.ckb.crypto.secp256k1.Sign.CURVE

import java.math.BigInteger
import java.security.KeyPair
import java.util.Arrays
import java.util.Objects
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.math.ec.ECPoint
import org.bouncycastle.math.ec.FixedPointCombMultiplier
import org.nervos.ckb.utils.Numeric

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class ECKeyPair(val privateKey: BigInteger?, val publicKey: BigInteger?) {

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }

        val ecKeyPair = o as ECKeyPair?

        return if (privateKey != ecKeyPair!!.privateKey) {
            false
        } else publicKey == ecKeyPair.publicKey
    }

    override fun hashCode(): Int {
        var result = privateKey?.hashCode() ?: 0
        result = 31 * result + (publicKey?.hashCode() ?: 0)
        return result
    }

    companion object {

        fun createWithKeyPair(keyPair: KeyPair): ECKeyPair {
            val privateKey = keyPair.private as BCECPrivateKey
            val publicKey = keyPair.public as BCECPublicKey

            val privateKeyValue = privateKey.d

            val publicKeyBytes = publicKey.q.getEncoded(false)
            val publicKeyValue = BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size))

            return ECKeyPair(privateKeyValue, publicKeyValue)
        }

        fun createWithPrivateKey(privateKey: BigInteger, compressed: Boolean): ECKeyPair {
            return ECKeyPair(privateKey, publicKeyFromPrivate(privateKey, compressed))
        }

        fun createWithPrivateKey(privateKeyHex: String, compressed: Boolean): ECKeyPair {
            return ECKeyPair(
                Numeric.toBigInt(privateKeyHex),
                publicKeyFromPrivate(Numeric.toBigInt(privateKeyHex), compressed)
            )
        }

        fun createWithPrivateKey(privateKey: BigInteger): ECKeyPair {
            return ECKeyPair(privateKey, publicKeyFromPrivate(privateKey))
        }

        fun createWithPrivateKey(privateKey: ByteArray): ECKeyPair {
            return createWithPrivateKey(Numeric.toBigInt(privateKey))
        }

        fun publicKeyFromPrivate(privateKeyHex: String): String {
            return publicKeyFromPrivate(Numeric.toBigInt(privateKeyHex), true).toString(16)
        }

        /**
         * Returns public key from the given private key.
         *
         * @param privateKey the private key to derive the public key from
         * @return BigInteger encoded public key
         */
        @JvmOverloads
        fun publicKeyFromPrivate(privateKey: BigInteger, compressed: Boolean = true): BigInteger {
            val point = publicPointFromPrivate(privateKey)

            val encoded = point.getEncoded(compressed)
            return BigInteger(1, Arrays.copyOfRange(encoded, if (compressed) 0 else 1, encoded.size))
        }

        /** Returns public key point from the given private key.  */
        private fun publicPointFromPrivate(privateKey: BigInteger): ECPoint {
            var privateKey = privateKey
            if (privateKey.bitLength() > CURVE.n.bitLength()) {
                privateKey = privateKey.mod(CURVE.n)
            }
            return FixedPointCombMultiplier().multiply(CURVE.g, privateKey)
        }
    }
}
/**
 * Returns public key from the given private key.
 *
 * @param privateKey the private key to derive the public key from
 * @return BigInteger encoded public key
 */
