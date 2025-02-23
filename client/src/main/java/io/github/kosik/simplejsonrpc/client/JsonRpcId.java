package io.github.kosik.simplejsonrpc.client;

import io.github.kosik.simplejsonrpc.client.generator.IdGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 24.08.14
 * Time: 18:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcId {

    Class<? extends IdGenerator<?>> value();
}
