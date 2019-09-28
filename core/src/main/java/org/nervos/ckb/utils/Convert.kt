package org.nervos.ckb.utils

import org.nervos.ckb.type.OutPoint
import org.nervos.ckb.type.cell.CellDep
import org.nervos.ckb.type.cell.CellInput
import org.nervos.ckb.type.cell.CellOutput
import org.nervos.ckb.type.transaction.Transaction

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
object Convert {

    @JvmStatic
    fun parseOutPoint(outPoint: OutPoint): OutPoint {
        return OutPoint(outPoint.txHash, Numeric.toHexString(outPoint.index))
    }

    @JvmStatic
    fun parseTransaction(transaction: Transaction): Transaction {
        val cellDeps = transaction.cellDeps.map {
                cellDep -> CellDep(OutPoint(cellDep.outPoint.txHash, Numeric.toHexString(cellDep.outPoint.index)), cellDep.depType) }

        val inputs = transaction.inputs.map {
                cellInput -> CellInput(OutPoint(cellInput.previousOutput.txHash, Numeric.toHexString(cellInput.previousOutput.index)), Numeric.toHexString(cellInput.since)) }

        val outputs = transaction.outputs.map {
                cellOutput -> CellOutput(Numeric.toHexString(cellOutput.capacity), cellOutput.lock, cellOutput.type) }

        return Transaction(
            Numeric.toHexString(transaction.version),
            cellDeps,
            transaction.headerDeps,
            inputs,
            outputs,
            transaction.outputsData,
            transaction.witnesses
        )
    }
}
