package org.nervos.ckb.utils

import java.math.BigDecimal
import java.math.BigInteger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.exceptions.MessageDecodingException

class NumericTest {

  @Test
  fun testQuantityDecode() {
    Assertions.assertEquals(Numeric.decodeQuantity("0x0"), BigInteger.valueOf(0L))
    Assertions.assertEquals(Numeric.decodeQuantity("0x400"), BigInteger.valueOf(1024L))
    Assertions.assertEquals(Numeric.decodeQuantity("0x0"), BigInteger.valueOf(0L))
    Assertions.assertEquals(
      Numeric.decodeQuantity("0x7fffffffffffffff"), BigInteger.valueOf(java.lang.Long.MAX_VALUE)
    )
    Assertions.assertEquals(
      Numeric.decodeQuantity("0x99dc848b94efc27edfad28def049810f"),
      BigInteger("204516877000845695339750056077105398031")
    )
  }

  @Test
  fun testQuantityDecodeLeadingZero() {
    Assertions.assertEquals(Numeric.decodeQuantity("0x0400"), BigInteger.valueOf(1024L))
    Assertions.assertEquals(Numeric.decodeQuantity("0x001"), BigInteger.valueOf(1L))
  }

  @Test
  fun testQuantityDecodeMissingPrefix() {
    Assertions.assertThrows(MessageDecodingException::class.java) { Numeric.decodeQuantity("ff") }
  }

  @Test
  fun testQuantityDecodeMissingValue() {
    Assertions.assertThrows(MessageDecodingException::class.java) { Numeric.decodeQuantity("0x") }
  }

  @Test
  fun testQuantityEncode() {
    Assertions.assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(0)), "0x0")
    Assertions.assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(1)), "0x1")
    Assertions.assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(1024)), "0x400")
    Assertions.assertEquals(
      Numeric.encodeQuantity(BigInteger.valueOf(java.lang.Long.MAX_VALUE)), "0x7fffffffffffffff"
    )
    Assertions.assertEquals(
      Numeric.encodeQuantity(BigInteger("204516877000845695339750056077105398031")),
      "0x99dc848b94efc27edfad28def049810f"
    )
  }

  @Test
  fun testCleanHexPrefix() {
    Assertions.assertEquals(Numeric.cleanHexPrefix(""), "")
    Assertions.assertEquals(Numeric.cleanHexPrefix("0123456789abcdef"), "0123456789abcdef")
    Assertions.assertEquals(Numeric.cleanHexPrefix("0x"), "")
    Assertions.assertEquals(Numeric.cleanHexPrefix("0x0123456789abcdef"), "0123456789abcdef")
  }

  @Test
  fun testPrependHexPrefix() {
    Assertions.assertEquals(Numeric.prependHexPrefix(""), "0x")
    Assertions.assertEquals(Numeric.prependHexPrefix("0x0123456789abcdef"), "0x0123456789abcdef")
    Assertions.assertEquals(Numeric.prependHexPrefix("0x"), "0x")
    Assertions.assertEquals(Numeric.prependHexPrefix("0123456789abcdef"), "0x0123456789abcdef")
  }

  @Test
  fun testToHexStringWithPrefix() {
    Assertions.assertEquals(Numeric.toHexStringWithPrefix(BigInteger.TEN), "0xa")
  }

  @Test
  fun testToHexStringNoPrefix() {
    Assertions.assertEquals(Numeric.toHexStringNoPrefix(BigInteger.TEN), "a")
  }

  @Test
  fun testToBytesPadded() {
    Assertions.assertArrayEquals(Numeric.toBytesPadded(BigInteger.TEN, 1), byteArrayOf(0xa))

    Assertions.assertArrayEquals(
      Numeric.toBytesPadded(BigInteger.TEN, 8), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0xa)
    )

    Assertions.assertArrayEquals(
      Numeric.toBytesPadded(BigInteger.valueOf(Integer.MAX_VALUE.toLong()), 4),
      byteArrayOf(0x7f, 0xff.toByte(), 0xff.toByte(), 0xff.toByte())
    )
  }

  @Test
  fun testHexStringToByteArray() {
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray(""), byteArrayOf())
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("0"), byteArrayOf(0))
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("1"), byteArrayOf(0x1))
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray(HEX_RANGE_STRING), HEX_RANGE_ARRAY)

    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("0x123"), byteArrayOf(0x1, 0x23))
  }

  @Test
  fun testToHexString() {
    Assertions.assertEquals(Numeric.toHexString(byteArrayOf()), "0x")
    Assertions.assertEquals(Numeric.toHexString(byteArrayOf(0x1)), "0x01")
    Assertions.assertEquals(Numeric.toHexString(HEX_RANGE_ARRAY), HEX_RANGE_STRING)
  }

  @Test
  fun testToHexStringNoPrefixZeroPadded() {
    Assertions.assertEquals(Numeric.toHexStringNoPrefixZeroPadded(BigInteger.ZERO, 5), "00000")

    Assertions.assertEquals(
      Numeric.toHexStringNoPrefixZeroPadded(
        BigInteger("11c52b08330e05d731e38c856c1043288f7d9744", 16), 40
      ),
      "11c52b08330e05d731e38c856c1043288f7d9744"
    )

    Assertions.assertEquals(
      Numeric.toHexStringNoPrefixZeroPadded(
        BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40
      ),
      "01c52b08330e05d731e38c856c1043288f7d9744"
    )
  }

  @Test
  fun testToHexStringWithPrefixZeroPadded() {
    Assertions.assertEquals(Numeric.toHexStringWithPrefixZeroPadded(BigInteger.ZERO, 5), "0x00000")

    Assertions.assertEquals(
      Numeric.toHexStringWithPrefixZeroPadded(
        BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40
      ),
      "0x01c52b08330e05d731e38c856c1043288f7d9744"
    )

    Assertions.assertEquals(
      Numeric.toHexStringWithPrefixZeroPadded(
        BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40
      ),
      "0x01c52b08330e05d731e38c856c1043288f7d9744"
    )
  }

  @Test
  fun testToHexStringZeroPaddedNegative() {
    Assertions.assertThrows(
      UnsupportedOperationException::class.java
    ) { Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(-1), 20) }
  }

  @Test
  fun testToHexStringZeroPaddedTooLargs() {
    Assertions.assertThrows(
      UnsupportedOperationException::class.java
    ) { Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(-1), 5) }
  }

  @Test
  fun testIsIntegerValue() {
    Assertions.assertTrue(Numeric.isIntegerValue(BigDecimal.ZERO))
    Assertions.assertTrue(Numeric.isIntegerValue(BigDecimal.ZERO))
    Assertions.assertTrue(Numeric.isIntegerValue(BigDecimal.valueOf(java.lang.Long.MAX_VALUE)))
    Assertions.assertTrue(Numeric.isIntegerValue(BigDecimal.valueOf(java.lang.Long.MIN_VALUE)))
    Assertions.assertTrue(
      Numeric.isIntegerValue(
        BigDecimal("9999999999999999999999999999999999999999999999999999999999999999.0")
      )
    )
    Assertions.assertTrue(
      Numeric.isIntegerValue(
        BigDecimal("-9999999999999999999999999999999999999999999999999999999999999999.0")
      )
    )

    Assertions.assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(0.1)))
    Assertions.assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(-0.1)))
    Assertions.assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(1.1)))
    Assertions.assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(-1.1)))
  }

  @Test
  fun testLittleEndian() {
    val littleEndian = Numeric.littleEndian(71)
    Assertions.assertEquals("0x4700000000000000", littleEndian)
  }

  companion object {

    private val HEX_RANGE_ARRAY = byteArrayOf(
      Numeric.asByte(0x0, 0x1),
      Numeric.asByte(0x2, 0x3),
      Numeric.asByte(0x4, 0x5),
      Numeric.asByte(0x6, 0x7),
      Numeric.asByte(0x8, 0x9),
      Numeric.asByte(0xa, 0xb),
      Numeric.asByte(0xc, 0xd),
      Numeric.asByte(0xe, 0xf)
    )

    private val HEX_RANGE_STRING = "0x0123456789abcdef"
  }
}
