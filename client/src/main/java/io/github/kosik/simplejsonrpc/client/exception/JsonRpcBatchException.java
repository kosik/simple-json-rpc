package io.github.kosik.simplejsonrpc.client.exception;

import io.github.kosik.simplejsonrpc.core.domain.ErrorMessage;

import java.util.Map;

/**
 * Date: 10/13/14
 * Time: 8:17 PM
 * <p>
 * Exception that occurs when batch JSON-RPC request is not completely successful
 */
public class JsonRpcBatchException extends RuntimeException {

    /**
     * Succeeded requests
     */
    private final Map<?, ?> successes;

    /**
     * Failed requests
     */
    private final Map<?, ErrorMessage> errors;

    public JsonRpcBatchException(String message, Map<?, ?> successes, Map<?, ErrorMessage> errors) {
        super(message);
        this.successes = successes;
        this.errors = errors;
    }


    public Map<?, ?> getSuccesses() {
        return successes;
    }


    public Map<?, ErrorMessage> getErrors() {
        return errors;
    }
}
