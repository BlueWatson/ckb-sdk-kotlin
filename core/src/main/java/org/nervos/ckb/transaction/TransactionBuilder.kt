package org.nervos.ckb.transaction

import java.io.IOException
import java.math.BigInteger
import java.util.ArrayList
import org.nervos.ckb.crypto.Blake2b
import org.nervos.ckb.crypto.secp256k1.ECKeyPair
import org.nervos.ckb.crypto.secp256k1.Sign
import org.nervos.ckb.service.Api
import org.nervos.ckb.system.type.SystemScriptCell
import org.nervos.ckb.type.cell.CellDep
import org.nervos.ckb.type.cell.CellInput
import org.nervos.ckb.type.cell.CellOutput
import org.nervos.ckb.type.transaction.Transaction
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class TransactionBuilder(api: Api) {

    private var systemSecpCell: SystemScriptCell? = null
    private val cellInputs = ArrayList<CellInput>()
    private val cellOutputs = ArrayList<CellOutput>()
    private val cellOutputsData = ArrayList<String>()
    private val witnesses = ArrayList<String>()
    var transaction: Transaction? = null
        private set

    init {
        try {
            this.systemSecpCell = Utils.getSystemScriptCell(api)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addInput(input: CellInput) {
        cellInputs.add(input)
    }

    fun addInputs(inputs: List<CellInput>) {
        cellInputs.addAll(inputs)
    }

    fun addOutput(output: CellOutput) {
        cellOutputs.add(output)
    }

    fun addOutputs(outputs: List<CellOutput>) {
        cellOutputs.addAll(outputs)
    }

    @Throws(IOException::class)
    fun buildTx() {
        var needCapacity = BigInteger.ZERO
        for (output in cellOutputs) {
            needCapacity = needCapacity.add(Numeric.toBigInt(output.capacity))
        }
        if (needCapacity < MIN_CAPACITY) {
            throw IOException("Less than min capacity")
        }
        if (cellInputs.size == 0) {
            throw IOException("Cell inputs could not empty")
        }
        for (i in cellOutputs.indices) {
            cellOutputsData.add("0x")
            witnesses.add("0x")
        }
        transaction = Transaction(
            "0",
            listOf(CellDep(systemSecpCell!!.outPoint, CellDep.DEP_GROUP)),
            emptyList(),
            cellInputs,
            cellOutputs,
            cellOutputsData,
            witnesses
        )
    }

    @Throws(IOException::class)
    fun signInput(index: Int, privateKey: String) {
        if (transaction == null) {
            throw IOException("Transaction could not null")
        }
        if (witnesses.size < cellInputs.size) {
            throw IOException("Invalid number of witnesses")
        }
        witnesses[index] = signWitness(witnesses[index], privateKey)
    }

    fun sign(privateKey: String) {
        for (witness in witnesses) {
            witnesses.add(signWitness(witness, privateKey))
        }
    }

    private fun signWitness(witness: String, privateKey: String): String {
        val ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false)
        val blake2b = Blake2b()
        blake2b.update(Numeric.hexStringToByteArray(transaction!!.computeHash()))
        blake2b.update(Numeric.hexStringToByteArray(witness))
        val message = blake2b.doFinalString()

        val signature = Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair)
                .signature
        )
        return signature + Numeric.cleanHexPrefix(witness)
    }

    companion object {

        private val MIN_CAPACITY = BigInteger("6000000000")
    }
}
