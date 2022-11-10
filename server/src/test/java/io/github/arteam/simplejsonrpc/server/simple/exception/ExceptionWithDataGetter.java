package io.github.arteam.simplejsonrpc.server.simple.exception;

import io.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import io.github.arteam.simplejsonrpc.core.annotation.JsonRpcErrorData;

@JsonRpcError(code = -30001, message = "Error with data (getter)")
public class ExceptionWithDataGetter extends RuntimeException {

    private final String[] data;

    public ExceptionWithDataGetter(String message, String[] data) {
        super(message);
        this.data = data;
    }

    @JsonRpcErrorData
    public String[] getData() {
        return data;
    }
}
