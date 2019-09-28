package type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.nervos.ckb.type.fixed.Byte1

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Byte1Test {

  @Test
  fun toByte1InitEmptyTest() {
    Assertions.assertThrows(
      UnsupportedOperationException::class.java
    ) {
      val bytes = byteArrayOf()
      val byte1 = Byte1(bytes)
      byte1.toBytes()
    }
  }

  @Test
  fun toByte1InitTest() {
    Assertions.assertThrows(
      UnsupportedOperationException::class.java
    ) {
      val bytes = byteArrayOf(0x01, 0x02)
      val byte1 = Byte1(bytes)
      byte1.toBytes()
    }
  }

  @Test
  fun toByte1Test() {
    val byte1 = Byte1("1")
    val byte2 = Byte1("01")
    val expected = byteArrayOf(0x01)
    Assertions.assertArrayEquals(expected, byte1.toBytes())
    Assertions.assertArrayEquals(expected, byte2.toBytes())
  }
}
