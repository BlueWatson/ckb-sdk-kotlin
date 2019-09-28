package org.nervos.ckb.example

import com.google.gson.Gson
import java.math.BigInteger
import org.nervos.ckb.service.Api
import org.nervos.ckb.type.Block
import org.nervos.ckb.type.BlockchainInfo

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
object RpcExample {

    private val api: Api = Api("http://localhost:8114")

    @JvmStatic
    fun main(args: Array<String>) {
      println("Welcome to use SDK to visit CKB")
      println(
        "CKB information: " + Gson().toJson(api.blockchainInfo)
      )
      val currentBlockNumber = api.tipBlockNumber
      println("Current block number: $currentBlockNumber")
      println(
        "Current block information: " + Gson().toJson(api.getBlockByNumber(currentBlockNumber.toString()))
      )
    }
}
