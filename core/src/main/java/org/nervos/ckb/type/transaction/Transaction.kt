package org.nervos.ckb.type.transaction

import com.google.gson.annotations.SerializedName

import java.io.IOException
import java.math.BigInteger
import java.util.ArrayList
import org.nervos.ckb.Encoder
import org.nervos.ckb.crypto.Blake2b
import org.nervos.ckb.crypto.secp256k1.ECKeyPair
import org.nervos.ckb.crypto.secp256k1.Sign
import org.nervos.ckb.type.cell.CellDep
import org.nervos.ckb.type.cell.CellInput
import org.nervos.ckb.type.cell.CellOutput
import org.nervos.ckb.utils.Numeric
import org.nervos.ckb.utils.Serializer

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
class Transaction {

    var version: String

    lateinit var hash: String

    @SerializedName("cell_deps")
    var cellDeps: List<CellDep>

    @SerializedName("header_deps")
    var headerDeps: List<String>

    var inputs: List<CellInput>
    var outputs: List<CellOutput>

    @SerializedName("outputs_data")
    var outputsData: List<String>

    var witnesses: List<String>

    constructor(
        version: String,
        cellDeps: List<CellDep>,
        headerDeps: List<String>,
        cellInputs: List<CellInput>,
        cellOutputs: List<CellOutput>,
        outputsData: List<String>,
        witnesses: List<String>
    ) {
        this.version = version
        this.cellDeps = cellDeps
        this.headerDeps = headerDeps
        this.inputs = cellInputs
        this.outputs = cellOutputs
        this.outputsData = outputsData
        this.witnesses = witnesses
    }

    constructor(
        version: String,
        hash: String,
        cellDeps: List<CellDep>,
        headerDeps: List<String>,
        cellInputs: List<CellInput>,
        cellOutputs: List<CellOutput>,
        outputsData: List<String>,
        witnesses: List<String>
    ) {
        this.version = version
        this.hash = hash
        this.cellDeps = cellDeps
        this.headerDeps = headerDeps
        this.inputs = cellInputs
        this.outputs = cellOutputs
        this.outputsData = outputsData
        this.witnesses = witnesses
    }

    fun computeHash(): String {
        val blake2b = Blake2b()
        blake2b.update(Encoder.encode(Serializer.serializeTransaction(this)))
        return blake2b.doFinalString()
    }

    fun sign(privateKey: BigInteger): Transaction {
        if (witnesses.size < inputs.size) {
            throw IOException("Invalid number of witnesses")
        }
        val txHash = computeHash()
        val ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false)
        val signedWitnesses = ArrayList<String>()
        for (witness in witnesses) {
            val blake2b = Blake2b()
            blake2b.update(Numeric.hexStringToByteArray(txHash))
            blake2b.update(Numeric.hexStringToByteArray(witness))
            val message = blake2b.doFinalString()

            val signature = Numeric.toHexString(
                Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).signature
            )
            signedWitnesses.add(signature + Numeric.cleanHexPrefix(witness))
        }
        return Transaction(
            version, txHash, cellDeps, headerDeps, inputs, outputs, outputsData, signedWitnesses
        )
    }
}
