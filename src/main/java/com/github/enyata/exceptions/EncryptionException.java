package com.github.enyata.exceptions;

/**
 *
 * This exception is usually thrown if NIBSS BVN validation portal does not respond as expected.
 * More like a fail safe to know that nibss responded correctly.
 */

public class EncryptionException extends Exception {

    public EncryptionException(String message) {
        super( message);
    }
    public EncryptionException(String message, Throwable e) {
        super( message, e);
    }

}
