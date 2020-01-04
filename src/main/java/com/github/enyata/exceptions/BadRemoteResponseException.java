package com.github.enyata.exceptions;

/**
 *
 * This exception is usually thrown if NIBSS BVN validation portal does not respond as expected.
 * More like a fail safe to know that nibss responded correctly.
 */

public class BadRemoteResponseException extends Exception {

    public BadRemoteResponseException(String message) {
        super( message);
    }

}
