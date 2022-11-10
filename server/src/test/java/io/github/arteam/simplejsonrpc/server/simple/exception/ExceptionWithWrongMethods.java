package io.github.arteam.simplejsonrpc.server.simple.exception;

import io.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import io.github.arteam.simplejsonrpc.core.annotation.JsonRpcErrorData;

@JsonRpcError(code = -30005, message = "Error with wrong methods")
public class ExceptionWithWrongMethods extends RuntimeException {

    private final String[] data;

    public ExceptionWithWrongMethods(String message, String[] data) {
        super(message);
        this.data = data;
    }

    @JsonRpcErrorData
    public void returnVoid() {
    }

    @JsonRpcErrorData
    public String hasArgs(String s) {
        return s;
    }

    @JsonRpcErrorData
    public String[] getData() {
        return data;
    }

}
