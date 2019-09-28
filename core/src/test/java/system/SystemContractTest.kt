package system

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.nervos.ckb.service.Api
import org.nervos.ckb.system.SystemContract
import org.nervos.ckb.system.type.SystemScriptCell

@Disabled
class SystemContractTest {

    @Test
    @Throws(Exception::class)
    fun testGetSystemContract() {
        val systemScriptCell = SystemContract.getSystemScriptCell(Api("http://localhost:8114"))
        Assertions.assertNotNull(systemScriptCell)
    }
}
