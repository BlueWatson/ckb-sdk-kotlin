package org.nervos.ckb.utils

/** Assertion utility functions.  */
object Assertions {
    /**
     * Verify that the provided precondition holds true.
     *
     * @param assertionResult assertion value
     * @param errorMessage error message if precondition failure
     */
    fun verifyPrecondition(assertionResult: Boolean, errorMessage: String) {
        if (!assertionResult) {
            throw RuntimeException(errorMessage)
        }
    }
}
