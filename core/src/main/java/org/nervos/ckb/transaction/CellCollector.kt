package org.nervos.ckb.transaction

import java.io.IOException
import java.math.BigInteger
import java.util.ArrayList
import org.nervos.ckb.service.Api
import org.nervos.ckb.system.type.SystemScriptCell
import org.nervos.ckb.type.Script
import org.nervos.ckb.type.cell.CellInput
import org.nervos.ckb.type.cell.CellOutputWithOutPoint
import org.nervos.ckb.utils.Numeric
import kotlin.math.min

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class CellCollector(private val api: Api) {

    fun getCellInputs(lockHash: String, needCapacity: BigInteger): CollectedCells {
        val cellInputs = ArrayList<CellInput>()
        var inputsCapacity = BigInteger.ZERO
        val toBlockNumber = api.getTipBlockNumber()!!.toLong()
        var fromBlockNumber: Long = 1

        while (fromBlockNumber <= toBlockNumber && inputsCapacity < needCapacity) {
            val currentToBlockNumber = min(fromBlockNumber + 100, toBlockNumber)
            val cellOutputs = api.getCellsByLockHash(
                lockHash,
                BigInteger.valueOf(fromBlockNumber).toString(),
                BigInteger.valueOf(currentToBlockNumber).toString()
            )

            if (cellOutputs != null && cellOutputs.isNotEmpty()) {
                for (cellOutputWithOutPoint in cellOutputs) {
                    val cellInput = CellInput(cellOutputWithOutPoint.outPoint, "0x0")
                    inputsCapacity = inputsCapacity.add(Numeric.toBigInt(cellOutputWithOutPoint.capacity))
                    cellInputs.add(cellInput)
                    if (inputsCapacity > needCapacity) {
                        break
                    }
                }
            }
            fromBlockNumber = currentToBlockNumber + 1
        }
        return CollectedCells(cellInputs, inputsCapacity)
    }

    @Throws(IOException::class)
    fun getCapacityWithAddress(address: String): BigInteger {
        val systemScriptCell = Utils.getSystemScriptCell(api)
        val lockScript = Utils.generateLockScriptWithAddress(address, systemScriptCell.cellHash)
        return getCapacityWithLockHash(lockScript.computeHash())
    }

    fun getCapacityWithLockHash(lockHash: String): BigInteger {
        var capacity = BigInteger.ZERO
        val toBlockNumber = api.getTipBlockNumber()!!.toLong()
        var fromBlockNumber: Long = 1

        while (fromBlockNumber <= toBlockNumber) {
            val currentToBlockNumber = min(fromBlockNumber + 100, toBlockNumber)
            val cellOutputs = api.getCellsByLockHash(
                lockHash,
                BigInteger.valueOf(fromBlockNumber).toString(),
                BigInteger.valueOf(currentToBlockNumber).toString()
            )

            if (cellOutputs != null && cellOutputs.isNotEmpty()) {
                for (output in cellOutputs) {
                    capacity = capacity.add(Numeric.toBigInt(output.capacity))
                }
            }
            fromBlockNumber = currentToBlockNumber + 1
        }
        return capacity
    }
}
