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

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class CellGatherer(private val api: Api) {

  fun getCellInputs(lockHash: String, needCapacities: BigInteger): CollectedCells {
    val cellInputs = ArrayList<CellInput>()
    var inputsCapacities = BigInteger.ZERO
    val toBlockNumber = api.tipBlockNumber.toLong()
    var fromBlockNumber: Long = 1

    while (fromBlockNumber <= toBlockNumber && inputsCapacities.compareTo(needCapacities) < 0) {
      val currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber)
      val cellOutputs = api.getCellsByLockHash(
        lockHash,
        BigInteger.valueOf(fromBlockNumber).toString(),
        BigInteger.valueOf(currentToBlockNumber).toString()
      )

      if (cellOutputs != null && cellOutputs.size > 0) {
        for (cellOutputWithOutPoint in cellOutputs) {
          val cellInput = CellInput(cellOutputWithOutPoint.outPoint, "0x0")
          inputsCapacities = inputsCapacities.add(Numeric.toBigInt(cellOutputWithOutPoint.capacity))
          cellInputs.add(cellInput)
          if (inputsCapacities.compareTo(needCapacities) > 0) {
            break
          }
        }
      }
      fromBlockNumber = currentToBlockNumber + 1
    }
    return CollectedCells(cellInputs, inputsCapacities)
  }

  @Throws(IOException::class)
  fun getCapacitiesWithAddress(address: String): BigInteger {
    val systemScriptCell = Utils.getSystemScriptCell(api)
    val lockScript = Utils.generateLockScriptWithAddress(address, systemScriptCell.cellHash)
    return getCapacitiesWithLockHash(lockScript.computeHash())
  }

  fun getCapacitiesWithLockHash(lockHash: String): BigInteger {
    var capacity = BigInteger.ZERO
    val toBlockNumber = api.tipBlockNumber.toLong()
    var fromBlockNumber: Long = 1

    while (fromBlockNumber <= toBlockNumber) {
      val currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber)
      val cellOutputs = api.getCellsByLockHash(
        lockHash,
        BigInteger.valueOf(fromBlockNumber).toString(),
        BigInteger.valueOf(currentToBlockNumber).toString()
      )

      if (cellOutputs != null && cellOutputs.size > 0) {
        for (output in cellOutputs) {
          capacity = capacity.add(Numeric.toBigInt(output.capacity))
        }
      }
      fromBlockNumber = currentToBlockNumber + 1
    }
    return capacity
  }
}
