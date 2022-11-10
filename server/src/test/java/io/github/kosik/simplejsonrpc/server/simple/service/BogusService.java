package io.github.kosik.simplejsonrpc.server.simple.service;

import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcMethod;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcService;

/**
 * Date: 8/2/14
 * Time: 6:25 PM
 */
@JsonRpcService
public class BogusService {

    @JsonRpcMethod
    public void bogus() {
        System.out.println("Bogus");
    }

    @JsonRpcMethod("bogus")
    public void bogus2() {
        System.out.println("Bogus2");
    }

    @JsonRpcMethod
    public void notBogus() {
        System.out.println("Not bogus");
    }
}
