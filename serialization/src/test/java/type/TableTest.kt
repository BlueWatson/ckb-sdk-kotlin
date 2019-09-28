package type

import java.util.Arrays
import java.util.Collections
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.Encoder
import org.nervos.ckb.type.dynamic.Bytes
import org.nervos.ckb.type.dynamic.Dynamic
import org.nervos.ckb.type.dynamic.Table
import org.nervos.ckb.type.fixed.Byte1
import org.nervos.ckb.type.fixed.Byte32
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class TableTest {

  @Test
  internal fun toBytesTest() {
    val argList = listOf(Bytes("3954acece65096bfa81258983ddb83915fc56bd8"))
    val table = Table(
      Arrays.asList(
        Byte32("68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88"),
        Byte1("01"),
        Dynamic(argList)
      )
    )

    val result = Encoder.encode(table)
    Assertions.assertEquals(
      "5100000010000000300000003100000068d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88012000000008000000140000003954acece65096bfa81258983ddb83915fc56bd8",
      Numeric.toHexStringNoPrefix(result)
    )
  }

  @Test
  internal fun getLengthTest() {
    val argList = listOf(Bytes("3954acece65096bfa81258983ddb83915fc56bd8"))
    val table = Table(
      Arrays.asList(
        Byte32("68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88"),
        Byte1("01"),
        Dynamic(argList)
      )
    )

    val result = Encoder.encode(table)
    Assertions.assertEquals(81, result.size)
  }
}
