package org.nervos.ckb.service

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
internal interface RpcCallback<T> {

  fun onFailure(errorMessage: String)

  fun onResponse(response: T)
}
