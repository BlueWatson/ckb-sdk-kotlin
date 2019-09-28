package type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.type.fixed.UInt32
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class UInt32Test {

  @Test
  internal fun toBytesTest() {
    val data = UInt32(256)
    Assertions.assertEquals("00010000", Numeric.toHexStringNoPrefix(data.toBytes()))
  }

  @Test
  internal fun getLengthTest() {
    val data = UInt32(256L)
    Assertions.assertEquals(4, data.toBytes().size)
  }
}
