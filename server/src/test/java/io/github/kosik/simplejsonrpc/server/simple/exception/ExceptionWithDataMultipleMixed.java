package io.github.kosik.simplejsonrpc.server.simple.exception;

import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcError;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcErrorData;

@JsonRpcError(code = -30004, message = "Error with data (multiple getters)")
public class ExceptionWithDataMultipleMixed extends RuntimeException {

    @JsonRpcErrorData
    private final String[] data;

    public ExceptionWithDataMultipleMixed(String message, String[] data) {
        super(message);
        this.data = data;
    }

    @JsonRpcErrorData
    public String[] getData() {
        return data;
    }

}
