package org.nervos.ckb.service

import com.google.gson.reflect.TypeToken
import java.math.BigInteger
import java.util.Arrays
import org.nervos.ckb.type.*
import org.nervos.ckb.type.cell.CellOutputWithOutPoint
import org.nervos.ckb.type.cell.CellTransaction
import org.nervos.ckb.type.cell.CellWithStatus
import org.nervos.ckb.type.cell.LiveCell
import org.nervos.ckb.type.transaction.Transaction
import org.nervos.ckb.type.transaction.TransactionWithStatus
import org.nervos.ckb.utils.Convert
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Api @JvmOverloads constructor(nodeUrl: String, isDebug: Boolean = false) {

  private val rpcService: RpcService = RpcService(nodeUrl, isDebug)

  val tipHeader: Header?
    get() = rpcService.post<Header>("get_tip_header", emptyList<String>(), Header::class.java)

  val tipBlockNumber: BigInteger
    get() {
      val blockNumber = rpcService.post<String>("get_tip_block_number", emptyList<String>(), String::class.java)
      return Numeric.toBigInt(blockNumber!!)
    }

  val currentEpoch: Epoch?
    get() = rpcService.post<Epoch>("get_current_epoch", emptyList<String>(), Epoch::class.java)

  /** Stats RPC  */
  val blockchainInfo: BlockchainInfo?
    get() = rpcService.post<BlockchainInfo>("get_blockchain_info", emptyList<Any>(), BlockchainInfo::class.java)

  val peersState: List<PeerState>?
    get() = rpcService.post<List<PeerState>>(
      "get_peers_state", emptyList<Any>(), object : TypeToken<List<PeerState>>() {

      }.type
    )

  val bannedAddress: List<BannedResultAddress>?
    get() = rpcService.post<List<BannedResultAddress>>(
      "get_banned_address",
      emptyList<Any>(),
      object : TypeToken<List<BannedResultAddress>>() {

      }.type
    )

  val peers: List<NodeInfo>?
    get() = rpcService.post<List<NodeInfo>>(
      "get_peers", emptyList<Any>(), object : TypeToken<List<NodeInfo>>() {

      }.type
    )

  val lockHashIndexStates: List<LockHashIndexState>?
    get() = rpcService.post<List<LockHashIndexState>>(
      "get_lock_hash_index_states",
      emptyList<Any>(),
      object : TypeToken<List<LockHashIndexState>>() {

      }.rawType
    )

  fun getBlock(blockHash: String): Block? {
    return rpcService.post<Block>("get_block", listOf(blockHash), Block::class.java)
  }

  fun getBlockByNumber(blockNumber: String): Block? {
    return rpcService.post<Block>(
      "get_block_by_number",
      listOf(Numeric.toHexString(blockNumber)),
      Block::class.java
    )
  }

  fun getTransaction(transactionHash: String): TransactionWithStatus? {
    return rpcService.post<TransactionWithStatus>(
      "get_transaction", listOf(transactionHash), TransactionWithStatus::class.java
    )
  }

  fun getBlockHash(blockNumber: String): String? {
    return rpcService.post<String>(
      "get_block_hash",
      listOf(Numeric.toHexString(blockNumber)),
      String::class.java
    )
  }

  fun getCellbaseOutputCapacityDetails(blockHash: String): CellbaseOutputCapacity? {
    return rpcService.post<CellbaseOutputCapacity>(
      "get_cellbase_output_capacity_details",
      listOf(blockHash),
      CellbaseOutputCapacity::class.java
    )
  }

  fun getCellsByLockHash(
    lockHash: String, fromBlockNumber: String, toBlockNumber: String
  ): List<CellOutputWithOutPoint>? {
    return rpcService.post<List<CellOutputWithOutPoint>>(
      "get_cells_by_lock_hash",
      listOf(lockHash, Numeric.toHexString(fromBlockNumber), Numeric.toHexString(toBlockNumber)),
      object : TypeToken<List<CellOutputWithOutPoint>>() {

      }.type
    )
  }

  @JvmOverloads
  fun getLiveCell(outPoint: OutPoint, withData: Boolean = false): CellWithStatus? {
    return rpcService.post<CellWithStatus>(
      "get_live_cell",
      listOf(Convert.parseOutPoint(outPoint), withData),
      CellWithStatus::class.java
    )
  }

  fun getEpochByNumber(epochNumber: String): Epoch? {
    return rpcService.post<Epoch>(
      "get_epoch_by_number",
      listOf(Numeric.toHexString(epochNumber)),
      Epoch::class.java
    )
  }

  fun getHeader(blockHash: String): Header? {
    return rpcService.post<Header>("get_header", listOf(blockHash), Header::class.java)
  }

  fun getHeaderByNumber(blockNumber: String): Header? {
    return rpcService.post<Header>(
      "get_header_by_number",
      listOf(Numeric.toHexString(blockNumber)),
      Header::class.java
    )
  }

  fun setBan(bannedAddress: BannedAddress): String? {
    return rpcService.post<String>("set_ban", listOf(bannedAddress), String::class.java)
  }

  /** Pool RPC  */
  fun txPoolInfo(): TxPoolInfo? {
    return rpcService.post<TxPoolInfo>("tx_pool_info", emptyList<Any>(), TxPoolInfo::class.java)
  }

  fun sendTransaction(transaction: Transaction): String? {
    return rpcService.post<String>(
      "send_transaction",
      listOf(Convert.parseTransaction(transaction)),
      String::class.java
    )
  }

  /** Net RPC  */
  fun localNodeInfo(): NodeInfo? {
    return rpcService.post<NodeInfo>("local_node_info", emptyList<Any>(), NodeInfo::class.java)
  }

  /** Experiment RPC  */
  fun dryRunTransaction(transaction: Transaction): Cycles? {
    return rpcService.post<Cycles>(
      "dry_run_transaction",
      listOf(Convert.parseTransaction(transaction)),
      Cycles::class.java
    )
  }

  fun computeTransactionHash(transaction: Transaction): String? {
    return rpcService.post<String>(
      "_compute_transaction_hash",
      listOf(Convert.parseTransaction(transaction)),
      String::class.java
    )
  }

  fun computeScriptHash(script: Script): String? {
    return rpcService.post<String>("_compute_script_hash", listOf(script), String::class.java)
  }

  /* Indexer RPC */

  fun indexLockHash(lockHash: String): LockHashIndexState? {
    return rpcService.post<LockHashIndexState>(
      "index_lock_hash", listOf(lockHash), LockHashIndexState::class.java
    )
  }

  fun indexLockHash(lockHash: String, indexFrom: String): LockHashIndexState? {
    return rpcService.post<LockHashIndexState>(
      "index_lock_hash",
      listOf(lockHash, Numeric.toHexString(indexFrom)),
      LockHashIndexState::class.java
    )
  }

  fun deindexLockHash(lockHash: String): List<String>? {
    return rpcService.post<List<String>>(
      "deindex_lock_hash",
      listOf(lockHash),
      object : TypeToken<List<String>>() {

      }.type
    )
  }

  fun getLiveCellsByLockHash(
    lockHash: String, page: String, pageSize: String, reverseOrder: Boolean
  ): List<LiveCell>? {
    return rpcService.post<List<LiveCell>>(
      "get_live_cells_by_lock_hash",
      listOf(lockHash, Numeric.toHexString(page), Numeric.toHexString(pageSize), reverseOrder),
      object : TypeToken<List<LiveCell>>() {

      }.type
    )
  }

  fun getTransactionsByLockHash(
    lockHash: String, page: String, pageSize: String, reverseOrder: Boolean
  ): List<CellTransaction>? {
    return rpcService.post<List<CellTransaction>>(
      "get_transactions_by_lock_hash",
      listOf(lockHash, Numeric.toHexString(page), Numeric.toHexString(pageSize), reverseOrder),
      object : TypeToken<List<CellTransaction>>() {

      }.type
    )
  }
}
