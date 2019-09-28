package org.nervos.ckb.crypto.secp256k1

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.nervos.ckb.utils.Numeric

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SignTest {

  private lateinit var ecKeyPair: ECKeyPair
  private lateinit var message: ByteArray

  @BeforeAll
  fun init() {
    val privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"
    ecKeyPair = ECKeyPair.createWithPrivateKey(Numeric.toBigInt(privateKey), false)
    message = Numeric.hexStringToByteArray(
      "403676bd85716a1e16b415093cee88c07d7cf2c2199aaf82320d354cb571f0d9"
    )
  }

  @Test
  fun compressedPublicKeyTest() {
    val privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"
    val publicKey = Numeric.toHexStringNoPrefix(
      ECKeyPair.publicKeyFromPrivate(Numeric.toBigInt(privateKey), true)
    )
    Assertions.assertEquals(
      "24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01", publicKey
    )
  }

  @Test
  fun signMessageTest() {
    val signResult =
      "0xc795b2b3c48d370324e5053f4509d4f1f18f80aec4a8cba68ebae922b9f882d845ae312bd84e25eed818ef84e7ed61a774f208fe2b2fe3588b60b4686086208200"
    val signature = Numeric.toHexString(Sign.signMessage(message, ecKeyPair).signature)
    Assertions.assertEquals(signResult, signature)
  }

  @Test
  fun signMessageForDerFormatTest() {
    val signResult =
      "0x3045022100c795b2b3c48d370324e5053f4509d4f1f18f80aec4a8cba68ebae922b9f882d8022045ae312bd84e25eed818ef84e7ed61a774f208fe2b2fe3588b60b46860862082"
    val signature = Numeric.toHexString(Sign.signMessage(message, ecKeyPair).derSignature)
    Assertions.assertEquals(signResult, signature)
  }
}
