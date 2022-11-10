package io.github.kosik.simplejsonrpc.server.metadata;

import java.util.Optional;

@FunctionalInterface
public interface ErrorDataResolver {

    Optional<Object> resolveData(Throwable throwable) throws Exception;
}
