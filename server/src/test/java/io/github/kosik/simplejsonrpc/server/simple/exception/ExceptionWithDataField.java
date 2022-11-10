package io.github.kosik.simplejsonrpc.server.simple.exception;

import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcError;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcErrorData;

@JsonRpcError(code = -30000, message = "Error with data (field)")
public class ExceptionWithDataField extends RuntimeException {

    @JsonRpcErrorData
    private final String[] data;

    public ExceptionWithDataField(String message, String[] data) {
        super(message);
        this.data = data;
    }

}
