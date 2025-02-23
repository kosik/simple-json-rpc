package io.github.kosik.simplejsonrpc.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 8/2/14
 * Time: 6:10 PM
 * Annotation for marking a service as a JSON-RPC service
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface JsonRpcService {
}
