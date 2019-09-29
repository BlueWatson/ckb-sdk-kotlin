package type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.type.fixed.Byte32
import org.nervos.ckb.type.fixed.Fixed
import org.nervos.ckb.type.fixed.Struct
import org.nervos.ckb.type.fixed.UInt32
import org.nervos.ckb.type.fixed.UInt64
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class FixedTest {

    @Test
    fun toBytesTest() {
        val txHash = Byte32("0x0000000000000000000000000000000000000000000000000000000000000000")
        val index = UInt32("4294967295")
        val sinceUInt64 = UInt64(1L)
        val inputs = Struct(listOf(sinceUInt64, Struct(listOf(txHash, index))))
        val structFixed = Fixed(listOf(inputs))
        Assertions.assertArrayEquals(
            Numeric.hexStringToByteArray(
                "0x010000000100000000000000000000000000000000000000000000000000000000000000000000000000000095729694"
            ),
            structFixed.toBytes()
        )
    }
}
