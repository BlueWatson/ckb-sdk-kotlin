/*
 * Copyright 2011 Google Inc.
 * Copyright 2015 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nervos.ckb.exceptions

open class AddressFormatException : IllegalArgumentException {
    constructor() : super() {}

    constructor(message: String) : super(message) {}

    /**
     * This exception is thrown by Bech32 when you try to decode data and a character isn't valid. You
     * shouldn't allow the user to proceed in this case.
     */
    class InvalidCharacter(val character: Char, val position: Int) :
        AddressFormatException("Invalid character '" + Character.toString(character) + "' at position " + position)

    /**
     * This exception is thrown by Bech32 when you try to decode data and a character isn't valid. You
     * shouldn't allow the user to proceed in this case.
     */
    class InvalidDataLength : AddressFormatException {
        constructor() : super() {}

        constructor(message: String) : super(message) {}
    }

    /**
     * This exception is thrown by Bech32 when you try to decode data and a character isn't valid. You
     * shouldn't allow the user to proceed in this case.
     */
    class InvalidChecksum : AddressFormatException {
        constructor() : super("Checksum does not validate") {}

        constructor(message: String) : super(message) {}
    }

    /**
     * This exception is thrown by the hierarchy of classes when you try and decode an address or
     * private key with an invalid prefix (version header or human-readable part). You shouldn't allow
     * the user to proceed in this case.
     */
    class InvalidPrefix : AddressFormatException {
        constructor() : super() {}

        constructor(message: String) : super(message) {}
    }
}
