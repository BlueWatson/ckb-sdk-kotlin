package org.nervos.ckb.crypto.secp256k1

import org.nervos.ckb.utils.Assertions.verifyPrecondition

import java.math.BigInteger
import java.security.SignatureException
import java.util.Arrays
import org.bouncycastle.asn1.x9.X9IntegerConverter
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.ec.CustomNamedCurves
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.bouncycastle.math.ec.ECAlgorithms
import org.bouncycastle.math.ec.ECPoint
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve
import org.nervos.ckb.crypto.Hash
import org.nervos.ckb.utils.Numeric

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
object Sign {

    private val CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1")
    internal val CURVE = ECDomainParameters(
        CURVE_PARAMS.curve, CURVE_PARAMS.g, CURVE_PARAMS.n, CURVE_PARAMS.h
    )
    internal val HALF_CURVE_ORDER = CURVE_PARAMS.n.shiftRight(1)

    @JvmStatic
    @JvmOverloads
    fun signMessage(message: ByteArray, keyPair: ECKeyPair, isHash: Boolean = false): SignatureData {
        val publicKey = keyPair.publicKey

        val messageHash = if (isHash) Hash.blake2b(message) else message

        val sig = sign(keyPair, messageHash)
        // Now we have to work backwards to figure out the recId needed to recover the signature.
        var recId = -1
        for (i in 0..3) {
            val k = recoverFromSignature(i, sig, messageHash)
            if (k != null && k == publicKey) {
                recId = i
                break
            }
        }
        if (recId == -1) {
            throw RuntimeException(
                "Could not construct a recoverable key. This should never happen."
            )
        }

        val headerByte = recId

        // 1 header + 32 bytes for R + 32 bytes for S
        val v = headerByte.toByte()
        val r = Numeric.toBytesPadded(sig.r, 32)
        val s = Numeric.toBytesPadded(sig.s, 32)

        return SignatureData(v, r, s)
    }

    /**
     * Given the components of a signature and a selector value, recover and return the public key
     * that generated the signature according to the algorithm in SEC1v2 section 4.1.6.
     *
     *
     * The recId is an index from 0 to 3 which indicates which of the 4 possible keys is the
     * correct one. Because the key recovery operation yields multiple potential keys, the correct key
     * must either be stored alongside the signature, or you must be willing to try each recId in turn
     * until you find one that outputs the key you are expecting.
     *
     *
     * If this method returns null it means recovery was not possible and recId should be iterated.
     *
     *
     * Given the above two points, a correct usage of this method is inside a for loop from 0 to 3,
     * and if the output is null OR a key that is not the one you expect, you try again with the next
     * recId.
     *
     * @param recId Which possible key to recover.
     * @param sig the R and S components of the signature, wrapped.
     * @param message Hash of the data that was signed.
     * @return An ECKey containing only the public part, or null if recovery wasn't possible.
     */
    private fun recoverFromSignature(recId: Int, sig: ECDSASignature, message: ByteArray?): BigInteger? {
        verifyPrecondition(recId >= 0, "recId must be positive")
        verifyPrecondition(sig.r.signum() >= 0, "r must be positive")
        verifyPrecondition(sig.s.signum() >= 0, "s must be positive")
        verifyPrecondition(message != null, "message cannot be null")

        // 1.0 For j from 0 to h   (h == recId here and the loop is outside this function)
        //   1.1 Let x = r + jn
        val n = CURVE.n // Curve order.
        val i = BigInteger.valueOf(recId.toLong() / 2)
        val x = sig.r.add(i.multiply(n))
        //   1.2. Convert the integer x to an octet string X of length mlen using the conversion
        //        routine specified in Section 2.3.7, where mlen = ⌈(log2 p)/8⌉ or mlen = ⌈m/8⌉.
        //   1.3. Convert the octet string (16 set binary digits)||X to an elliptic curve point R
        //        using the conversion routine specified in Section 2.3.4. If this conversion
        //        routine outputs "invalid", then do another iteration of Step 1.
        //
        // More concisely, what these points mean is to use X as a compressed public key.
        val prime = SecP256K1Curve.q
        if (x.compareTo(prime) >= 0) {
            // Cannot have point co-ordinates larger than this as everything takes place modulo Q.
            return null
        }
        // Compressed keys require you to know an extra bit of data about the y-coord as there are
        // two possibilities. So it's encoded in the recId.
        val R = decompressKey(x, recId and 1 == 1)
        //   1.4. If nR != point at infinity, then do another iteration of Step 1 (callers
        //        responsibility).
        if (!R.multiply(n).isInfinity) {
            return null
        }
        //   1.5. Compute e from M using Steps 2 and 3 of ECDSA signature verification.
        val e = BigInteger(1, message!!)
        //   1.6. For k from 1 to 2 do the following.   (loop is outside this function via
        //        iterating recId)
        //   1.6.1. Compute a candidate public key as:
        //               Q = mi(r) * (sR - eG)
        //
        // Where mi(x) is the modular multiplicative inverse. We transform this into the following:
        //               Q = (mi(r) * s ** R) + (mi(r) * -e ** G)
        // Where -e is the modular additive inverse of e, that is z such that z + e = 0 (mod n).
        // In the above equation ** is point multiplication and + is point addition (the EC group
        // operator).
        //
        // We can find the additive inverse by subtracting e from zero then taking the mod. For
        // example the additive inverse of 3 modulo 11 is 8 because 3 + 8 mod 11 = 0, and
        // -3 mod 11 = 8.
        val eInv = BigInteger.ZERO.subtract(e).mod(n)
        val rInv = sig.r.modInverse(n)
        val srInv = rInv.multiply(sig.s).mod(n)
        val eInvrInv = rInv.multiply(eInv).mod(n)
        val q = ECAlgorithms.sumOfTwoMultiplies(CURVE.g, eInvrInv, R, srInv)

        val qBytes = q.getEncoded(false)
        // We remove the prefix
        return BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.size))
    }

    /** Decompress a compressed public key (x co-ord and low-bit of y-coord).  */
    private fun decompressKey(xBN: BigInteger, yBit: Boolean): ECPoint {
        val x9 = X9IntegerConverter()
        val compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.curve))
        compEnc[0] = (if (yBit) 0x03 else 0x02).toByte()
        return CURVE.curve.decodePoint(compEnc)
    }

    /**
     * Given an arbitrary piece of text and an message signature encoded in bytes, returns the public
     * key that was used to sign it. This can then be compared to the expected public key to determine
     * if the signature was correct.
     *
     * @param message RLP encoded message.
     * @param signatureData The message signature components
     * @return the public key used to sign the message
     * @throws SignatureException If the public key could not be recovered or if there was a signature
     * formatTx error.
     */
    @Throws(SignatureException::class)
    fun signedMessageToKey(message: ByteArray, signatureData: SignatureData): BigInteger {

        val r = signatureData.r
        val s = signatureData.s
        verifyPrecondition(r.size == 32, "r must be 32 bytes")
        verifyPrecondition(s.size == 32, "s must be 32 bytes")

        val header = signatureData.v.toInt() and 0xFF
        // The header byte: 0x1B = first key with even y, 0x1C = first key with odd y,
        //                  0x1D = second key with even y, 0x1E = second key with odd y
        if (header > 7) {
            throw SignatureException("Header byte out of range: $header")
        }

        val sig = ECDSASignature(
            BigInteger(1, signatureData.r), BigInteger(1, signatureData.s)
        )

        val messageHash = Hash.blake2b(message)
        return recoverFromSignature(header, sig, messageHash)
            ?: throw SignatureException("Could not recover public key from signature")
    }

    /**
     * Sign a hash with the private key of this key pair.
     *
     * @param transactionHash the hash to sign
     * @return An [ECDSASignature] of the hash
     */
    private fun sign(ecKeyPair: ECKeyPair, transactionHash: ByteArray): ECDSASignature {
        val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest()))

        val privKey = ECPrivateKeyParameters(ecKeyPair.privateKey, CURVE)
        signer.init(true, privKey)
        val components = signer.generateSignature(transactionHash)

        return ECDSASignature(components[0], components[1]).toCanonicalised()
    }

    class SignatureData(val v: Byte, val r: ByteArray, val s: ByteArray) {

        val signature: ByteArray
            get() {
                val sig = ByteArray(65)
                System.arraycopy(r, 0, sig, 0, 32)
                System.arraycopy(s, 0, sig, 32, 32)
                sig[64] = v
                return sig
            }

        // https://crypto.stackexchange.com/questions/1795/how-can-i-convert-a-der-ecdsa-signature-to-asn-1
        val derSignature: ByteArray
            get() {
                val rLen = if (r[0].toInt() and 0xFF > 0x7F) r.size + 1 else r.size
                val sLen = if (s[0].toInt() and 0xFF > 0x7F) s.size + 1 else s.size
                val len = 6 + rLen + sLen
                val sig = ByteArray(len)

                sig[0] = 0x30
                sig[1] = (len - 2).toByte()
                sig[2] = 0x02
                sig[3] = rLen.toByte()

                if (r[0].toInt() and 0xFF > 0x7F) {
                    sig[4] = 0x00
                    System.arraycopy(r, 0, sig, 5, rLen - 1)
                } else {
                    System.arraycopy(r, 0, sig, 4, rLen)
                }

                sig[4 + rLen] = 0x02
                sig[4 + rLen + 1] = sLen.toByte()

                if (s[0].toInt() and 0xFF > 0x7F) {
                    sig[4 + rLen + 2] = 0x00
                    System.arraycopy(s, 0, sig, 4 + rLen + 3, sLen - 1)
                } else {
                    System.arraycopy(s, 0, sig, 4 + rLen + 2, sLen)
                }

                return sig
            }

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }

            val that = other as SignatureData?

            if (v != that!!.v) {
                return false
            }
            return if (!Arrays.equals(r, that.r)) {
                false
            } else Arrays.equals(s, that.s)
        }

        override fun hashCode(): Int {
            var result = v.toInt()
            result = 31 * result + Arrays.hashCode(r)
            result = 31 * result + Arrays.hashCode(s)
            return result
        }
    }
}
