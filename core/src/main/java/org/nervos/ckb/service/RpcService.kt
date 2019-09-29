package org.nervos.ckb.service

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicLong
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor

/** Copyright © 2019 Nervos Foundation. All rights reserved.  */
internal class RpcService(private val url: String, isDebug: Boolean) {

  private var client: OkHttpClient? = null
  private val gson: Gson

  init {
    if (isDebug) {
      val logging = HttpLoggingInterceptor()
      logging.level = HttpLoggingInterceptor.Level.BODY
      client = OkHttpClient.Builder().addInterceptor(logging).build()
    } else {
      client = OkHttpClient()
    }
    gson = Gson()
  }

  fun <T> post(method: String, params: List<*>, cls: Type): T? {
    val requestParams = RequestParams(method, params)
    val body = gson.toJson(requestParams).toRequestBody(JSON_MEDIA_TYPE)
    val request = Request.Builder().url(url).post(body).build()
    try {
      val response = client!!.newCall(request).execute()
      if (response.isSuccessful) {
        val responseBody = response.body!!.string()
        val rpcResponse = gson.fromJson<RpcResponse<*>>(responseBody, object : TypeToken<RpcResponse<*>>() {

        }.type)

        if (rpcResponse.error != null) {
          throw IOException(
            "RpcService method " + method + " error " + gson.toJson(rpcResponse.error)
          )
        }

        val jsonElement = JsonParser().parse(responseBody).asJsonObject.get("result")
        print(jsonElement)
        return if (jsonElement.isJsonObject) {
          gson.fromJson(jsonElement.asJsonObject, cls)
        } else gson.fromJson<T>(jsonElement, cls)
      } else {
        throw IOException("RpcService method " + method + " error code " + response.code)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return null
  }

  fun <T> postAsync(
    method: String, params: List<*>, cls: Type, callback: RpcCallback<T>
  ) {
    val requestParams = RequestParams(method, params)
    val body = gson.toJson(requestParams).toRequestBody(JSON_MEDIA_TYPE)
    val request = Request.Builder().url(url).post(body).build()
    client!!
      .newCall(request)
      .enqueue(
        object : Callback {
          override fun onFailure(call: Call, e: IOException) {
            callback.onFailure(e.message!!)
          }

          @Throws(IOException::class)
          override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
              val responseBody = response.body!!.string()
              val rpcResponse = gson.fromJson<RpcResponse<T>>(responseBody, object : TypeToken<RpcResponse<T>>() {

              }.type)
              if (rpcResponse.error != null) {
                throw IOException(
                  "RpcService method " + method + " error " + gson.toJson(rpcResponse.error)
                )
              }
              val jsonElement = JsonParser().parse(responseBody).asJsonObject.get("result")
              if (jsonElement.isJsonObject) {
                callback.onResponse(gson.fromJson(jsonElement.asJsonObject, cls))
              }
              callback.onResponse(gson.fromJson(jsonElement, cls))
            } else {
              throw IOException(
                "RpcService method " + method + " error code " + response.code
              )
            }
          }
        })
  }

  internal class RequestParams(var method: String, var params: List<*>) {
    var jsonrpc = "2.0"
    var id: Long = 0

    init {
      this.id = nextId.getAndIncrement()
    }
  }

  internal class RpcResponse<T> {
    var id: Long = 0
    var jsonrpc: String? = null
    var result: T? = null
    var error: Error? = null

    internal inner class Error {
      var code: Int = 0
      var message: String? = null
    }
  }

  companion object {
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    private val nextId = AtomicLong(0)
  }
}
