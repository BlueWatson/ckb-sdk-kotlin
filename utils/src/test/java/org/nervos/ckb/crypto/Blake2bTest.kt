package org.nervos.ckb.crypto

import java.nio.charset.StandardCharsets
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class Blake2bTest {

  @Test
  fun testBlake2bUpdate() {
    val blake2b = Blake2b()
    blake2b.update("".toByteArray(StandardCharsets.UTF_8))
    Assertions.assertEquals(
      blake2b.doFinalString(),
      "0x44f4c69744d5f8c55d642062949dcae49bc4e7ef43d388c5a12f42b5633d163e"
    )

    blake2b.update("The quick brown fox jumps over the lazy dog".toByteArray(StandardCharsets.UTF_8))
    Assertions.assertEquals(
      blake2b.doFinalString(),
      "0xabfa2c08d62f6f567d088d6ba41d3bbbb9a45c241a8e3789ef39700060b5cee2"
    )
  }

  @Test
  fun testBlake2b() {
    val blake2b = Blake2b()
    blake2b.update("The quick brown fox jumps over the lazy dog".toByteArray(StandardCharsets.UTF_8))

    val anotherBlake2b = Blake2b()
    anotherBlake2b.update(
      "The quick brown fox jumps over the lazy dog".toByteArray(StandardCharsets.UTF_8)
    )
    Assertions.assertEquals(
      blake2b.doFinalString(),
      "0xabfa2c08d62f6f567d088d6ba41d3bbbb9a45c241a8e3789ef39700060b5cee2"
    )
  }
}
