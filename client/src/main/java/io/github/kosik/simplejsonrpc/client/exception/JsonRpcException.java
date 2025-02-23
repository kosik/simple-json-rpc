package io.github.kosik.simplejsonrpc.client.exception;

import io.github.kosik.simplejsonrpc.core.domain.ErrorMessage;

/**
 * Date: 8/9/14
 * Time: 10:08 PM
 * <p>
 * Represents JSON-RPC error returned by a server
 */
public class JsonRpcException extends RuntimeException {

    /**
     * Actual error message
     */
    private final ErrorMessage errorMessage;

    public JsonRpcException(ErrorMessage errorMessage) {
        super(errorMessage.toString());
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
