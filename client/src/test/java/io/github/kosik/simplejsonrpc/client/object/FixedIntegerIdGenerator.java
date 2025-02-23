package io.github.kosik.simplejsonrpc.client.object;

import io.github.kosik.simplejsonrpc.client.generator.IdGenerator;

/**
 * Date: 24.08.14
 * Time: 18:43
 */
public class FixedIntegerIdGenerator implements IdGenerator<Integer> {

    private final Integer value;

    public FixedIntegerIdGenerator(Integer value) {
        this.value = value;
    }

    @Override
    public Integer generate() {
        return value;
    }
}
