package org.nervos.ckb.example

import java.io.IOException
import java.math.BigInteger
import java.util.Arrays
import java.util.Collections
import org.nervos.ckb.example.transaction.CellsWithPrivateKey
import org.nervos.ckb.example.transaction.CollectUtils
import org.nervos.ckb.example.transaction.Receiver
import org.nervos.ckb.example.transaction.Sender
import org.nervos.ckb.service.Api
import org.nervos.ckb.transaction.CellGatherer
import org.nervos.ckb.transaction.TransactionBuilder
import org.nervos.ckb.type.cell.CellInput

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
object TransactionExample {

    private const val NODE_URL = "http://localhost:8114"
    private val UnitCKB = BigInteger("100000000")
    private var api: Api = Api(NODE_URL, false)
    private var KeyPairs: List<KeyPair> = ArrayList()

    init {
        KeyPairs = listOf(KeyPair(
                "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2",
                "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g"
            ), KeyPair(
                "a202386cb9e46cecff9bc14b748b714c713075dd964c2507c8a8900540164959",
                "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek"
            ), KeyPair(
                "89b773ec5cf97b8fd2cf280ab1e37cd658dc28d84bac8f8dda4a8646cc08d266",
                "ckt1qyqxvnycu7tdtyuejn3mmcnl4y09muxz8c3s2ewjd4"
            ), KeyPair(
                "fec27185a66dd21abb97eeaaeb6bf63fb9c5b7c7966550e6798a78fbaf4197f0",
                "ckt1qyq8n3400g4lw7xs4denyjprpyzaa6z2z5wsl7e2gs"
            ), KeyPair(
                "2cee134faa03a158011dff33b7756e89a0c76ba64006640615be7b483b2935b4",
                "ckt1qyqd4lgpt5auunu6s3wskkwxmdx548wksvcqyq44en"
            ), KeyPair(
                "55b55c7bd177ed837dde45bbde12fc79c12fb8695be258064f40e6d5f65db96c",
                "ckt1qyqrlj6znd3uhvuln5z83epv54xu8pmphzgse5uylq"
            ))
    }

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val minerPrivateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"
        val minerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83"
        val receivers1 = listOf(
            Receiver(KeyPairs[0].address, BigInteger("800").multiply(UnitCKB)),
            Receiver(KeyPairs[1].address, BigInteger("900").multiply(UnitCKB)),
            Receiver(KeyPairs[2].address, BigInteger("1000").multiply(UnitCKB)))

        println(
            "Before transfer, miner's balance: "
                + getBalance(minerAddress).divide(UnitCKB).toString(10)
                + " CKB"
        )

        println(
            "Before transfer, first receiver1's balance: "
                + getBalance(KeyPairs[0].address).divide(UnitCKB).toString(10)
                + " CKB"
        )

        // miner send capacity to three receiver1 accounts with 800, 900 and 1000 CKB
        val hash = sendCapacity(minerPrivateKey, receivers1, minerAddress)
        println("First transaction hash: $hash")
        Thread.sleep(30000) // waiting transaction into block, sometimes you should wait more seconds

        println(
            "After transfer, first receiver1's balance: "
                + getBalance(KeyPairs[0].address).divide(UnitCKB).toString(10)
                + " CKB"
        )

        // Second transaction

        val senders1 = listOf(
            Sender(KeyPairs[0].privateKey, BigInteger("500").multiply(UnitCKB)),
            Sender(KeyPairs[1].privateKey, BigInteger("600").multiply(UnitCKB)),
            Sender(KeyPairs[2].privateKey, BigInteger("700").multiply(UnitCKB)))

        val receivers2 = listOf(
            Receiver(KeyPairs[3].address, BigInteger("400").multiply(UnitCKB)),
            Receiver(KeyPairs[4].address, BigInteger("500").multiply(UnitCKB)),
            Receiver(KeyPairs[5].address, BigInteger("600").multiply(UnitCKB)))

        val changeAddress = "ckt1qyqfnym6semhw2vzm33fjvk3ngxuf5433l9qz3af8a"

        println(
            "Before transfer, first receiver2's balance: "
                + getBalance(receivers2[0].address).divide(UnitCKB).toString(10)
                + " CKB"
        )

        // sender1 accounts send capacity to three receiver2 accounts with 400, 500 and 600 CKB
        val hash2 = sendCapacity(senders1, receivers2, changeAddress)
        println("Second transaction hash: $hash2")
        Thread.sleep(30000) // waiting transaction into block, sometimes you should wait more seconds

        println(
            "After transfer, receiver2's balance: "
                + getBalance(receivers2[0].address).divide(UnitCKB).toString(10)
                + " CKB"
        )
    }

    @Throws(IOException::class)
    private fun getBalance(address: String): BigInteger {
        return CellGatherer(api).getCapacitiesWithAddress(address)
    }

    @Throws(IOException::class)
    private fun sendCapacity(
        privateKey: String, receivers: List<Receiver>, changeAddress: String
    ): String? {
        var needCapacity = BigInteger.ZERO
        for (receiver in receivers) {
            needCapacity = needCapacity.add(receiver.capacity)
        }
        val senders = listOf(Sender(privateKey, needCapacity))
        return sendCapacity(senders, receivers, changeAddress)
    }

    @Throws(IOException::class)
    private fun sendCapacity(
        senders: List<Sender>, receivers: List<Receiver>, changeAddress: String
    ): String? {
        val builder = TransactionBuilder(api)
        val txUtils = CollectUtils(api)

        val cellsWithPrivateKeys = txUtils.collectInputs(senders)

        builder.addInputs(txUtils.collectInputs(senders).flatMap { cellsWithPrivateKey -> cellsWithPrivateKey.inputs })

        builder.addOutputs(txUtils.generateOutputs(receivers, changeAddress))

        builder.buildTx()

        var index = 0
        for (cellsWithPrivateKey in cellsWithPrivateKeys) {
            for (i in cellsWithPrivateKey.inputs.indices) {
                builder.signInput(index + i, cellsWithPrivateKey.privateKey)
            }
            index += cellsWithPrivateKey.inputs.size
        }

        return api.sendTransaction(builder.transaction!!)
    }

    internal class KeyPair(var privateKey: String, var address: String)
}
