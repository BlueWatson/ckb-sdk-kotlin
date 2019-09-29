package org.nervos.ckb.crypto

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.utils.Numeric

class HashTest {

    @Test
    fun testBlake2b() {
        Assertions.assertEquals(
            Hash.blake2b(""),
            "0x44f4c69744d5f8c55d642062949dcae49bc4e7ef43d388c5a12f42b5633d163e"
        )

        Assertions.assertEquals(
            Hash.blake2bString("The quick brown fox jumps over the lazy dog"),
            "0xabfa2c08d62f6f567d088d6ba41d3bbbb9a45c241a8e3789ef39700060b5cee2"
        )
    }

  
    @Test
    fun testByte() {
        Assertions.assertEquals(Numeric.asByte(0x0, 0x0), 0x0.toByte())
        Assertions.assertEquals(Numeric.asByte(0x1, 0x0), 0x10.toByte())
        Assertions.assertEquals(Numeric.asByte(0xf, 0xf), 0xff.toByte())
        Assertions.assertEquals(Numeric.asByte(0xc, 0x5), 0xc5.toByte())
    }
}
