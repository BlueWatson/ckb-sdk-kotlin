package org.nervos.ckb.exceptions

/** Encoding exception.  */
class MessageEncodingException : RuntimeException {
    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
