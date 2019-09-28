package org.nervos.ckb.utils

import org.nervos.ckb.type.cell.CellDep.Companion.CODE

import java.util.ArrayList
import org.nervos.ckb.type.OutPoint
import org.nervos.ckb.type.Script
import org.nervos.ckb.type.cell.CellDep
import org.nervos.ckb.type.cell.CellInput
import org.nervos.ckb.type.cell.CellOutput
import org.nervos.ckb.type.dynamic.Bytes
import org.nervos.ckb.type.dynamic.Dynamic
import org.nervos.ckb.type.dynamic.Table
import org.nervos.ckb.type.fixed.*
import org.nervos.ckb.type.transaction.Transaction

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
object Serializer {

    fun serializeOutPoint(outPoint: OutPoint): Struct {
        val txHash = Byte32(outPoint.txHash)
        val index = UInt32(outPoint.index)
        return Struct(listOf(txHash, index))
    }

    fun serializeScript(script: Script): Table {
        return Table(
            listOf(
                Byte32(script.codeHash),
                Byte1(if (Script.DATA == script.hashType) "00" else "01"),
                Bytes(script.args))
        )
    }

    fun serializeCellInput(cellInput: CellInput): Struct {
        val sinceUInt64 = UInt64(cellInput.since)
        val outPointStruct = serializeOutPoint(cellInput.previousOutput)
        return Struct(listOf(sinceUInt64, outPointStruct))
    }

    fun serializeCellOutput(cellOutput: CellOutput): Table {
        return Table(
            listOf(
                UInt64(cellOutput.capacity),
                serializeScript(cellOutput.lock),
                if(cellOutput.type == null)  Empty() else serializeScript(cellOutput.type!!)
            )
        )
    }

    fun serializeCellDep(cellDep: CellDep): Struct {
        val outPointStruct = serializeOutPoint(cellDep.outPoint)
        val depTypeBytes = if (CODE == cellDep.depType) Byte1("0") else Byte1("1")
        return Struct(listOf(outPointStruct, depTypeBytes))
    }

    fun serializeCellDeps(cellDeps: List<CellDep>): Fixed<Struct> {
        val cellDepList = ArrayList<Struct>()
        for (cellDep in cellDeps) {
            cellDepList.add(serializeCellDep(cellDep))
        }
        return Fixed(cellDepList)
    }

    fun serializeCellInputs(cellInputs: List<CellInput>): Fixed<Struct> {
        val cellInputList = ArrayList<Struct>()
        for (cellInput in cellInputs) {
            cellInputList.add(serializeCellInput(cellInput))
        }
        return Fixed(cellInputList)
    }

    fun serializeCellOutputs(cellOutputs: List<CellOutput>): Dynamic<Table> {
        val cellOutputList = ArrayList<Table>()
        for (cellOutput in cellOutputs) {
            cellOutputList.add(serializeCellOutput(cellOutput))
        }
        return Dynamic(cellOutputList)
    }

    fun serializeBytes(bytes: List<String>): Dynamic<Bytes> {
        val bytesList = ArrayList<Bytes>()
        for (data in bytes) {
            bytesList.add(Bytes(data))
        }
        return Dynamic(bytesList)
    }

    fun serializeByte32(bytes: List<String>): Fixed<Byte32> {
        val byte32List = ArrayList<Byte32>()
        for (data in bytes) {
            byte32List.add(Byte32(data))
        }
        return Fixed(byte32List)
    }

    fun serializeTransaction(transaction: Transaction): Table {
        val tx = Convert.parseTransaction(transaction)
        val versionUInt32 = UInt32(tx.version)
        val cellDepFixed = serializeCellDeps(tx.cellDeps)
        val headerDepFixed = serializeByte32(tx.headerDeps)
        val inputsFixed = serializeCellInputs(tx.inputs)
        val outputsVec = serializeCellOutputs(tx.outputs)
        val dataVec = serializeBytes(tx.outputsData)
        return Table(listOf(versionUInt32, cellDepFixed, headerDepFixed, inputsFixed, outputsVec, dataVec))
    }
}
