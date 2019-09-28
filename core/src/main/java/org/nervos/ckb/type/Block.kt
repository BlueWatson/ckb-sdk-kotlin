package org.nervos.ckb.type

import org.nervos.ckb.type.transaction.Transaction

/** Copyright © 2018 Nervos Foundation. All rights reserved.  */
data class Block (
    var header: Header,
    var transactions: List<Transaction>,
    var proposals: List<String>,
    var uncles: List<Uncle>)

data class Uncle (var header: Header, var proposals: List<String>)
