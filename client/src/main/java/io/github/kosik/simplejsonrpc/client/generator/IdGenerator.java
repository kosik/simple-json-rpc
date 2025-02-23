package io.github.kosik.simplejsonrpc.client.generator;

/**
 * Date: 24.08.14
 * Time: 18:12
 * <p>
 * A strategy for the generation of request ids
 */
@FunctionalInterface
public interface IdGenerator<T> {

    T generate();
}
