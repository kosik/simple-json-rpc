package io.github.kosik.simplejsonrpc.server.simple.exception;

import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcError;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcErrorData;

@JsonRpcError(code = -30003, message = "Error with data (multiple getters)")
public class ExceptionWithDataMultipleGetters extends RuntimeException {

    private final String[] data;
    private final String anotherData;

    public ExceptionWithDataMultipleGetters(String message, String[] data, String anotherData) {
        super(message);
        this.data = data;
        this.anotherData = anotherData;
    }

    @JsonRpcErrorData
    public String[] getData() {
        return data;
    }

    @JsonRpcErrorData
    public String getAnotherData() {
        return anotherData;
    }
}
