package service

import org.junit.jupiter.api.*
import org.nervos.ckb.service.Api
import org.nervos.ckb.type.*
import org.nervos.ckb.type.transaction.Transaction

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiTest {

    private var api: Api = Api("http://localhost:8114", false)

    @Test
    fun testGetBlockByNumber() {
        val block = api.getBlockByNumber("0x1")
        Assertions.assertNotNull(block)
    }

    @Test
    fun testGetBlockHashByNumber() {
        val blockHash = api.getBlockHash("0x1")
        Assertions.assertNotNull(blockHash)
    }

    @Test
    fun testGetCellbaseOutputCapacityDetails() {
        val blockHash = api.getBlockHash("0x1")
        val cellbaseOutputCapacity = api.getCellbaseOutputCapacityDetails(blockHash!!)
        Assertions.assertNotNull(cellbaseOutputCapacity)
    }

    @Test
    fun testBlockAndTransaction() {
        val blockHash = api.getBlockHash("0x1")
        val block = api.getBlock(blockHash!!)
        Assertions.assertNotNull(block)
        Assertions.assertNotNull(block?.header)
    }

    @Test
    fun testTransaction() {
        val transactionHash = api.getBlockByNumber("0x1")!!.transactions[0].hash
        val transaction = api.getTransaction(transactionHash)!!.transaction
        Assertions.assertNotNull(transaction)
    }

    @Test
    fun testGetTipHeader() {
        val header = api.tipHeader
        Assertions.assertNotNull(header)
    }

    @Test
    fun testGetTipBlockNumber() {
        val blockNumber = api.tipBlockNumber
        Assertions.assertNotNull(blockNumber.toString())
    }

    @Test
    fun testGetCurrentEpoch() {
        val epoch = api.currentEpoch
        Assertions.assertNotNull(epoch)
    }

    @Test
    fun testGetEpochByNumber() {
        val epoch = api.getEpochByNumber("0")
        Assertions.assertNotNull(epoch)
    }

    @Test
    fun testGetHeader() {
        val blockHash = api.getBlockHash("0x1")
        val header = api.getHeader(blockHash!!)
        Assertions.assertNotNull(header)
    }

    @Test
    fun testGetHeaderByNumber() {
        val header = api.getHeaderByNumber("0x1")
        Assertions.assertNotNull(header)
    }

    @Test
    fun localNodeInfo() {
        val nodeInfo = api.localNodeInfo()
        Assertions.assertNotNull(nodeInfo)
    }

    @Test
    fun getPeers() {
        val peers = api.peers
        Assertions.assertNotNull(peers)
    }

    @Test
    fun testSetBan() {
        val bannedAddress = BannedAddress("192.168.0.2", "insert", "1840546800000", true, "test set_ban rpc")
        val banResult = api.setBan(bannedAddress)
        Assertions.assertNull(banResult)
    }

    @Test
    fun testGetBannedAddress() {
        val bannedAddresses = api.bannedAddress
        Assertions.assertNotNull(bannedAddresses)
    }

    @Test
    fun txPoolInfo() {
        val txPoolInfo = api.txPoolInfo()
        Assertions.assertNotNull(txPoolInfo)
    }

    @Test
    fun testGetBlockchainInfo() {
        val blockchainInfo = api.blockchainInfo
        Assertions.assertNotNull(blockchainInfo)
    }

    @Test
    fun testGetPeersState() {
        val peerStates = api.peersState
        Assertions.assertNotNull(peerStates)
    }

    @Test
    fun testGetCellsByLockHash() {
        val cellOutputWithOutPoints = api.getCellsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba", "0", "100"
        )
        Assertions.assertTrue(cellOutputWithOutPoints!!.isNotEmpty())
    }

    @Test
    fun testGetLiveCell() {
        val cellWithStatus = api.getLiveCell(
            OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            true
        )
        Assertions.assertNotNull(cellWithStatus)
    }

    @Test
    fun testGetLiveCellWithData() {
        val (cell) = api.getLiveCell(
            OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            true
        )!!
        Assertions.assertNotNull(cell.data)
    }

    @Test
    fun testGetLiveCellWithoutData() {
        val (cell) = api.getLiveCell(
            OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            false
        )!!
        Assertions.assertNull(cell.data)
    }

    @Test
    fun testSendTransaction() {
        val transactionHash = api.sendTransaction(
            Transaction(
                "0",
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
            )
        )
        Assertions.assertNotNull(transactionHash)
    }

    @Test
    fun testDryRunTransaction() {
        val cycles = api.dryRunTransaction(
            Transaction(
                "0",
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
            )
        )
        Assertions.assertNotNull(cycles)
    }

    @Test
    fun testComputeTransactionHash() {
        val transactionHash = api.computeTransactionHash(
            Transaction(
                "0",
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
            )
        )
        Assertions.assertNotNull(transactionHash)
    }

    @Test
    fun testIndexLockHash() {
        val lockHashIndexState =
            api.indexLockHash("0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e")
        Assertions.assertNotNull(lockHashIndexState)
    }

    @Test
    fun testIndexLockHashWithBlockNumber() {
        val lockHashIndexState = api.indexLockHash(
            "0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e", "0"
        )
        Assertions.assertNotNull(lockHashIndexState)
    }

    @Test
    fun testDeindexLockHash() {
        val lockHashs = api.deindexLockHash("0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e")
        Assertions.assertNull(lockHashs)
    }

    @Test
    fun testGetLockHashIndexStates() {
        val lockHashIndexStates = api.lockHashIndexStates
        Assertions.assertNotNull(lockHashIndexStates)
    }

    @Test
    fun testGetLiveCellsByLockHash() {
        val liveCells = api.getLiveCellsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba",
            "0",
            "100",
            false
        )
        Assertions.assertNotNull(liveCells)
    }

    @Test
    fun testGetTransactionsByLockHash() {
        val cellTransactions = api.getTransactionsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba",
            "0",
            "100",
            false
        )
        Assertions.assertNotNull(cellTransactions)
    }
}
