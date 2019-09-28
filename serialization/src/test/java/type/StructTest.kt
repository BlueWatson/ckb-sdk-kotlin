package type

import java.util.Arrays
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.type.fixed.Byte1
import org.nervos.ckb.type.fixed.Byte32
import org.nervos.ckb.type.fixed.Struct
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class StructTest {

  @Test
  internal fun toBytesTest() {
    val byte1 = Byte1("ab")
    val byte32 = Byte32("0102030405060708090001020304050607080900010203040506070809000102")

    val struct = Struct(Arrays.asList(byte1, byte32))
    Assertions.assertArrayEquals(
      Numeric.hexStringToByteArray(
        "0xab0102030405060708090001020304050607080900010203040506070809000102"
      ),
      struct.toBytes()
    )
  }
}
