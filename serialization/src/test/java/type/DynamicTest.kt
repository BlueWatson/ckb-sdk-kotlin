package type

import java.util.ArrayList
import java.util.Arrays
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.type.base.DynType
import org.nervos.ckb.type.dynamic.Bytes
import org.nervos.ckb.type.dynamic.Dynamic
import org.nervos.ckb.type.dynamic.Table
import org.nervos.ckb.type.fixed.Byte1
import org.nervos.ckb.type.fixed.Byte32
import org.nervos.ckb.type.fixed.Empty
import org.nervos.ckb.type.fixed.UInt64
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class DynamicTest {

  @Test
  fun toBytesTest() {
    val script = Table(
      listOf(
        Byte32("0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88"),
        Byte1("01"),
        Dynamic(
          listOf(Bytes("0xb2e61ff569acf041b3c2c17724e2379c581eeac3"))
        )
      )
    )

    val cellOutput = Table(listOf(UInt64(125000000000L), script, Empty()))
    val cellOutputs = ArrayList<DynType<*>>()
    cellOutputs.add(cellOutput)
    val outputs = Dynamic(cellOutputs)
    val expected = Numeric.hexStringToByteArray(
      "71000000080000006900000010000000180000006900000000a2941a1d0000005100000010000000300000003100000068d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e8801200000000800000014000000b2e61ff569acf041b3c2c17724e2379c581eeac3"
    )
    Assertions.assertArrayEquals(expected, outputs.toBytes())
  }
}
