package org.nervos.ckb.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.nervos.ckb.utils.Strings.isEmpty
import org.nervos.ckb.utils.Strings.zeros

class StringsTest {

    @Test
    fun testRepeat() {
        Assertions.assertEquals(Strings.repeat('0', 0), "")
        Assertions.assertEquals(Strings.repeat('1', 3), "111")
    }

    @Test
    fun testZeros() {
        Assertions.assertEquals(zeros(0), "")
        Assertions.assertEquals(zeros(3), "000")
    }

    @Test
    fun testEmptyString() {
        Assertions.assertTrue(isEmpty(null))
        Assertions.assertTrue(isEmpty(""))
        Assertions.assertFalse(isEmpty("hello world"))
    }
}
