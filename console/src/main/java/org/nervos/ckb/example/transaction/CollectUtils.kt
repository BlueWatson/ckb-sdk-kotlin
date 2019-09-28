package org.nervos.ckb.example.transaction

import java.io.IOException
import java.math.BigInteger
import java.util.ArrayList
import java.util.Collections
import org.nervos.ckb.address.AddressUtils
import org.nervos.ckb.service.Api
import org.nervos.ckb.system.type.SystemScriptCell
import org.nervos.ckb.transaction.CellGatherer
import org.nervos.ckb.transaction.CollectedCells
import org.nervos.ckb.transaction.Utils
import org.nervos.ckb.type.Script
import org.nervos.ckb.type.cell.CellOutput
import org.nervos.ckb.utils.Network
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class CollectUtils(private val api: Api) {

  private var systemSecpCell: SystemScriptCell? = null
  private var collectedCapacity = BigInteger.ZERO

  init {
    try {
      systemSecpCell = Utils.getSystemScriptCell(api)
    } catch (e: IOException) {
      e.printStackTrace()
    }

  }

  @Throws(IOException::class)
  fun collectInputs(senders: List<Sender>): List<CellsWithPrivateKey> {
    val cellsWithPrivateKeys = ArrayList<CellsWithPrivateKey>()
    for (sender in senders) {
      val lockHash = Utils.generateLockScriptWithPrivateKey(sender.privateKey, systemSecpCell!!.cellHash)
        .computeHash()
      val collectedCells = CellGatherer(api).getCellInputs(lockHash, sender.capacity)
      if (collectedCells.capacity.compareTo(sender.capacity) < 0) {
        throw IOException("No enough Capacities with sender private key: " + sender.privateKey)
      }
      collectedCapacity = collectedCapacity.add(collectedCells.capacity)
      cellsWithPrivateKeys.add(CellsWithPrivateKey(collectedCells.inputs, sender.privateKey))
    }
    return cellsWithPrivateKeys
  }

  fun generateOutputs(receivers: List<Receiver>, changeAddress: String): List<CellOutput> {
    val cellOutputs = ArrayList<CellOutput>()
    val addressUtils = AddressUtils(Network.TESTNET)
    for (receiver in receivers) {
      val blake160 = addressUtils.getBlake160FromAddress(receiver.address)
      cellOutputs.add(
        CellOutput(
          receiver.capacity.toString(),
          Script(
            systemSecpCell!!.cellHash, blake160, Script.TYPE
          )
        )
      )
    }
    var needCapacity = BigInteger.ZERO
    for (receiver in receivers) {
      needCapacity = needCapacity.add(receiver.capacity)
    }
    if (collectedCapacity.compareTo(needCapacity) > 0) {
      val changeAddressBlake160 = addressUtils.getBlake160FromAddress(changeAddress)
      cellOutputs.add(
        CellOutput(
          collectedCapacity.subtract(needCapacity).toString(),
          Script(
            systemSecpCell!!.cellHash,
            Numeric.prependHexPrefix(changeAddressBlake160),
            Script.TYPE
          )
        )
      )
    }
    return cellOutputs
  }
}
