package io.github.kosik.simplejsonrpc.client.object;

import io.github.kosik.simplejsonrpc.client.generator.IdGenerator;

/**
 * Date: 24.08.14
 * Time: 18:41
 */
public class FixedStringIdGenerator implements IdGenerator<String> {

    private final String value;

    public FixedStringIdGenerator(String value) {
        this.value = value;
    }

    @Override
    public String generate() {
        return value;
    }
}
