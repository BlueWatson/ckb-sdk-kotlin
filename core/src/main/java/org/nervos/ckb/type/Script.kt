package org.nervos.ckb.type

import com.google.gson.annotations.SerializedName
import org.nervos.ckb.Encoder
import org.nervos.ckb.crypto.Blake2b
import org.nervos.ckb.utils.Serializer

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
class Script(
    @SerializedName("code_hash") var codeHash: String,
    var args: String,
    @SerializedName("hash_type") var hashType: String = DATA) {

    fun computeHash(): String {
        val blake2b = Blake2b()
        blake2b.update(Encoder.encode(Serializer.serializeScript(this)))
        return blake2b.doFinalString()
    }

    companion object {

        const val DATA = "data"
        const val TYPE = "type"
    }
}
