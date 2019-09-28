package type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.type.fixed.Byte32
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Byte32Test {

  @Test
  fun byte32InitEmptyErrorTest() {
    Assertions.assertThrows(
      UnsupportedOperationException::class.java
    ) {
      val bytes = byteArrayOf()
      val byte32 = Byte32(bytes)
      byte32.toBytes()
    }
  }

  @Test
  fun byte32InitErrorTest() {
    Assertions.assertThrows(
      UnsupportedOperationException::class.java
    ) {
      val bytes = byteArrayOf(0x01, 0x02, 0x02, 0x03)
      val byte32 = Byte32(bytes)
      byte32.toBytes()
    }
  }

  @Test
  fun byte32InitTest() {
    Assertions.assertDoesNotThrow {
      val bytes = byteArrayOf(
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01,
        0x01
      )
      val byte32 = Byte32(bytes)
      byte32.toBytes()
    }
  }

  @Test
  fun toBytesTest() {
    val byte32 = Byte32("68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88")
    val expected = Numeric.hexStringToByteArray(
      "68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88"
    )
    Assertions.assertArrayEquals(expected, byte32.toBytes())
  }
}
