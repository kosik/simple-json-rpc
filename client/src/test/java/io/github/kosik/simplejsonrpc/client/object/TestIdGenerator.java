package io.github.kosik.simplejsonrpc.client.object;

import io.github.kosik.simplejsonrpc.client.generator.IdGenerator;

/**
 * Date: 24.08.14
 * Time: 18:24
 */
public class TestIdGenerator implements IdGenerator<String> {

    private static String id;

    @Override
    public String generate() {
        return "asd671";
    }
}
