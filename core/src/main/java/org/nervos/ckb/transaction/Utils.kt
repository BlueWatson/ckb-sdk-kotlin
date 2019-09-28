package org.nervos.ckb.transaction

import java.io.IOException
import org.nervos.ckb.address.AddressUtils
import org.nervos.ckb.crypto.Hash
import org.nervos.ckb.crypto.secp256k1.ECKeyPair
import org.nervos.ckb.service.Api
import org.nervos.ckb.system.SystemContract
import org.nervos.ckb.system.type.SystemScriptCell
import org.nervos.ckb.type.Script
import org.nervos.ckb.utils.Network
import org.nervos.ckb.utils.Numeric

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
object Utils {

  private val TESTNET_PREFIX = "ckt"

  fun generateLockScriptWithPrivateKey(privateKey: String, codeHash: String): Script {
    val publicKey = ECKeyPair.publicKeyFromPrivate(privateKey)
    val blake160 = Numeric.prependHexPrefix(Numeric.cleanHexPrefix(Hash.blake2b(publicKey)).substring(0, 40))
    return Script(codeHash, blake160, Script.TYPE)
  }

  fun generateLockScriptWithAddress(address: String, codeHash: String): Script {
    val addressUtils = AddressUtils(if (address.startsWith(TESTNET_PREFIX)) Network.TESTNET else Network.MAINNET)
    val publicKeyBlake160 = addressUtils.getBlake160FromAddress(address)
    return Script(codeHash, publicKeyBlake160, Script.TYPE)
  }

  @Throws(IOException::class)
  fun getSystemScriptCell(api: Api): SystemScriptCell {
    return SystemContract.getSystemScriptCell(api)
  }
}
