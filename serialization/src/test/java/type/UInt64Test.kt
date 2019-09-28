package type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.type.fixed.UInt64
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class UInt64Test {

  @Test
  internal fun toBytesTest() {
    val data = UInt64(25689834934789L)
    Assertions.assertEquals(
      "05527c615d170000", Numeric.toHexStringNoPrefix(data.toBytes())
    )
  }

  @Test
  internal fun getLengthTest() {
    val data = UInt64(25689834934789L)
    Assertions.assertEquals(8, data.toBytes().size)
  }
}
