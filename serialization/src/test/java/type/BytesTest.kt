package type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.type.dynamic.Bytes
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class BytesTest {

  @Test
  fun toBytesTest() {
    val bytes = Bytes("3954acece65096bfa81258983ddb83915fc56bd8")
    val expected = Numeric.hexStringToByteArray("140000003954acece65096bfa81258983ddb83915fc56bd8")
    Assertions.assertArrayEquals(expected, bytes.toBytes())
  }

  @Test
  fun getLengthTest() {
    val bytes = Bytes("3954acece65096bfa81258983ddb83915fc56bd8")
    Assertions.assertEquals(24, bytes.length)
  }
}
