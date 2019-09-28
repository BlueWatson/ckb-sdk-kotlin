package org.nervos.ckb.crypto

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.exceptions.AddressFormatException
import org.nervos.ckb.utils.Bech32
import org.nervos.ckb.utils.Numeric

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class Bech32Test {

  @Test
  fun testEncode() {
    val data =
      byteArrayOf(0, 14, 20, 15, 7, 13, 26, 0, 25, 18, 6, 11, 13, 8, 21, 4, 20, 3, 17, 2, 29, 3, 12, 29, 3, 4, 15, 24, 20, 6, 14, 30, 22)
    Assertions.assertEquals(
      "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4", Bech32.encode("bc", data)
    )
  }

  @Test
  fun testDecode() {
    val data =
      byteArrayOf(0, 14, 20, 15, 7, 13, 26, 0, 25, 18, 6, 11, 13, 8, 21, 4, 20, 3, 17, 2, 29, 3, 12, 29, 3, 4, 15, 24, 20, 6, 14, 30, 22)
    val bech32Data = Bech32.decode("bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4")
    Assertions.assertEquals(Numeric.toHexString(data), Numeric.toHexString(bech32Data.data))
  }

  @Test
  fun testDecodeValidAddresses() {
    val validAddresses = arrayOf(
      "BC1QW508D6QEJXTDG4Y5R3ZARVARY0C5XW7KV8F3T4",
      "tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3q0sl5k7",
      "bc1pw508d6qejxtdg4y5r3zarvary0c5xw7kw508d6qejxtdg4y5r3zarvary0c5xw7k7grplx",
      "BC1SW50QA3JX3S",
      "bc1zw508d6qejxtdg4y5r3zarvaryvg6kdaj",
      "tb1qqqqqp399et2xygdj5xreqhjjvcmzhxw4aywxecjdzew6hylgvsesrxh6hy"
    )
    for (address in validAddresses) {
      val bech32Data = Bech32.decode(address)
      Assertions.assertNotNull(bech32Data)
    }
  }

  @Test
  fun testValidChecksums() {
    val validChecksums = arrayOf(
      "A12UEL5L",
      "an83characterlonghumanreadablepartthatcontainsthenumber1andtheexcludedcharactersbio1tt5tgs",
      "abcdef1qpzry9x8gf2tvdw0s3jn54khce6mua7lmqqqxw",
      "11qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqc8247j",
      "split1checkupstagehandshakeupstreamerranterredcaperred2y9e3w"
    )
    for (address in validChecksums) {
      val bech32Data = Bech32.decode(address)
      Assertions.assertNotNull(bech32Data)
    }
  }

  @Test
  fun testInvalidChecksums() {
    val invalidChecksums = arrayOf(
      " 1nwldj5",
      "\u0000x7F1axkwrx",
      "an84characterslonghumanreadablepartthatcontainsthenumber1andtheexcludedcharactersbio1569pvx",
      "pzry9x0s0muk",
      "1pzry9x0s0muk",
      "x1b4n0q5v",
      "li1dgmt3",
      "de1lg7wt\u0000xff"
    )
    for (address in invalidChecksums) {
      Assertions.assertThrows(AddressFormatException::class.java) { Bech32.decode(address) }
    }
  }
}
