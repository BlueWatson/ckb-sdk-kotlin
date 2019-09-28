package org.nervos.ckb.address

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.crypto.secp256k1.ECKeyPair
import org.nervos.ckb.exceptions.AddressFormatException
import org.nervos.ckb.utils.Bech32
import org.nervos.ckb.utils.Network
import org.nervos.ckb.utils.Numeric

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class AddressTest {

  @Test
  fun testPublicKeyHash() {
    val utils = AddressUtils(Network.TESTNET)
    val hash = utils.blake160("0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01")
    Assertions.assertEquals("36c329ed630d6ce750712a477543672adab57f4c", hash)
  }

  @Test
  fun testAddressAscii() {
    val bin = "P2PH"
    val utils = AddressUtils(Network.TESTNET)
    val data = utils.strToAscii(bin)
    Assertions.assertEquals("50325048", data)
  }

  @Test
  fun testAddressParse() {
    val address = "ckt1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6v234ygf"
    val payload = "0x015032504836c329ed630d6ce750712a477543672adab57f4c"
    val utils = AddressUtils(Network.TESTNET)
    val bech32Data = utils.parse(address)
    Assertions.assertEquals(payload, Numeric.toHexString(bech32Data!!.data))
  }

  @Test
  @Throws(AddressFormatException::class)
  fun testArgToAddressTestnet() {
    val expected = "ckt1qyqz6824th6pekd6858nru9p4j3u783fttl4k3r0cp2lt7uxhx00fxcxpzeq8"
    val args = "0x2d1d555df41cd9ba3d0f31f0a1aca3cf1e295aff5b446fc055f5fb86b99ef49b"
    val utils = AddressUtils(Network.TESTNET)
    val actual = utils.generate(args)
    Assertions.assertEquals(expected, actual)
  }

  @Test
  fun testPublicKeyHashToAddressTestnet() {
    val utils = AddressUtils(Network.TESTNET)
    Assertions.assertEquals(
      "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83",
      utils.generateFromPublicKey(
        "0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"
      )
    )
  }

  @Test
  fun testPublicKeyHashToAddressMainnet() {
    val utils = AddressUtils(Network.MAINNET)
    Assertions.assertEquals(
      "ckb1qyqrdsefa43s6m882pcj53m4gdnj4k440axqdt9rtd",
      utils.generateFromPublicKey(
        "0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"
      )
    )
  }

  @Test
  fun testPrivateKeyHashToAddressTestnet() {
    val utils = AddressUtils(Network.TESTNET)
    val privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"
    val publicKey = ECKeyPair.publicKeyFromPrivate(privateKey)
    Assertions.assertEquals(
      "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83", utils.generateFromPublicKey(publicKey)
    )
  }

  @Test
  fun testBlake160FromAddressTestnet() {
    val utils = AddressUtils(Network.TESTNET)
    val blake160 = utils.getBlake160FromAddress("ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83")
    Assertions.assertEquals(blake160, "0x36c329ed630d6ce750712a477543672adab57f4c")
  }

  @Test
  fun testBlake160FromAddressMainnet() {
    val utils = AddressUtils(Network.MAINNET)
    val blake160 = utils.getBlake160FromAddress("ckb1qyqrdsefa43s6m882pcj53m4gdnj4k440axqdt9rtd")
    Assertions.assertEquals(blake160, "0x36c329ed630d6ce750712a477543672adab57f4c")
  }

  @Test
  fun testPubkeyHashToAddressMainnetRFC() {
    val utils = AddressUtils(Network.MAINNET)
    Assertions.assertEquals(
      "ckb1qyqp8eqad7ffy42ezmchkjyz54rhcqf8q9pqrn323p",
      utils.generate("0x13e41d6F9292555916f17B4882a5477C01270142")
    )
  }
}
