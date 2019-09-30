# CKB SDK Kotlin

[![Telegram Group](https://cdn.rawgit.com/Patrolavia/telegram-badge/8fe3382b/chat.svg)](https://t.me/nervos_ckb_dev)

Kotlin SDK for Nervos [CKB](https://github.com/nervosnetwork/ckb).

### Prerequisites

* Kotlin 1.3.40 or later
* Gradle 5.0 or later

### Installation

You can generate the jar and import manually.
```shell
git clone https://github.com/duanyytop/ckb-sdk-kotlin.git

cd ckb-sdk-kotlin

gradle shadowJar  // ./gradlew shadowJar 
```
A `console-{version}-all.jar` package will be generated in `console/build/libs`, which you can put into your project to develop with it.

If you don't want to generate the jar by yourself, you can download a build from [releases](https://github.com/daunyytop/ckb-sdk-kotlin/releases).

#### Import Jar to Project

When you need to import `ckb-sdk-kotlin` dependency to your project, you can add the `console-{version}-all.jar` to your project `libs` package. 

If you use IDE (eg. IntelliJ IDEA or Eclipse or other Editors), you can import jar according to IDE option help documents.

### Usage

#### JSON-RPC

You can make JSON-RPC request to your CKB node URL with this SDK. Below are some examples:

```Kotlin
val api = Api("your-ckb-node-url")

// using RPC `get_tip_block_number`, it will return the latest block number
val blockNumber = api.getTipBlockNumber()

// using RPC `get_block_hash` with block number as parameter, it will return block hash
val blockNumber = "0"
val blockHash = api.getBlockHash(blockNumber)

// using RPC `get_block` with block hash as parameter, it will return block object
val block = api.getBlock(blockHash)

```

You can see more JSON-RPC requests from [RPC Document](https://github.com/nervosnetwork/ckb/blob/develop/rpc/README.md)

#### Transfer

`console/example/TransactionExample.kt` provides `sendCapacity` method with any amount inputs which belong to any amount private keys.

You can reference detail example in `console/example/TransactionExample.kt`.

```Kotlin
  val api = Api("your-ckb-node-url")

  val inputs: List<CellInput> = listOf(
    input1, // Input from address 'cktxxx', capacity 100 CKB
    input2, // Input from address 'cktxxx', capacity 200 CKB
    input3 // Input from address 'cktxxx', capacity 300 CKB
  )
  
  val outputs: List<CellOutput> = listOf(
    output1, // Output to address 'cktxxx', capacity 200
    output2, // Output to address 'cktxxx', capacity 300
    output3 // Output to address 'cktxxx' as change, capacity 100
  )
  
  val builder = TransactionBuilder(api)
  
  builder.addInputs(inputs)
  
  builder.addOutputs(outputs)
  
  builder.signInput(0, privateKey1)
  builder.signInput(1, privateKey2)
  builder.signInput(2, privateKey3)
  
  val hash = api.sendTransaction(builder.getTransaction())
```

#### Address

You can generate ckb address through this SDK as below:

> There are many address generating methods, and this is just an example.

```Kotlin
// Generate ckb testnet address
val utils = AddressUtils(Network.TESTNET)

// Generate public key from private key through SECP256K1
val publicKey =
    Sign.publicKeyFromPrivate(
            Numeric.toBigInt(
                "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"),
            true)
        .toString(16)

val address = utils.generateFromPublicKey(publicKey)
```

## Development

The SDK use [ktlint](https://github.com/pinterest/ktlint) to make code more clean and tidy.

## License

The SDK is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).

## Changelog

See [CHANGELOG](CHANGELOG.md) for more information.
