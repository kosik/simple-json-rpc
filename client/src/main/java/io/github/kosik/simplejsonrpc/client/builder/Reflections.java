package io.github.kosik.simplejsonrpc.client.builder;

import io.github.kosik.simplejsonrpc.client.JsonRpcId;
import io.github.kosik.simplejsonrpc.client.JsonRpcParams;
import io.github.kosik.simplejsonrpc.client.ParamsType;
import io.github.kosik.simplejsonrpc.client.generator.AtomicLongIdGenerator;
import io.github.kosik.simplejsonrpc.client.generator.IdGenerator;
import io.github.kosik.simplejsonrpc.client.metadata.ClassMetadata;
import io.github.kosik.simplejsonrpc.client.metadata.MethodMetadata;
import io.github.kosik.simplejsonrpc.client.metadata.ParameterMetadata;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcMethod;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcOptional;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcParam;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcService;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 11/17/14
 * Time: 8:04 PM
 * <p>
 * Utility class for gathering meta-information about client proxies through reflection
 */
class Reflections {

    private Reflections() {
    }

    /**
     * Gets remote service interface metadata
     *
     * @param clazz an interface for representing a remote service
     * @return class metadata
     */
    public static ClassMetadata getClassMetadata(Class<?> clazz) {
        Map<Method, MethodMetadata> methodsMetadata = new HashMap<>(32);
        Class<?> searchClass = clazz;
        while (searchClass != null) {
            JsonRpcService rpcServiceAnn = getAnnotation(searchClass.getAnnotations(), JsonRpcService.class);
            if (rpcServiceAnn == null) {
                throw new IllegalStateException("Class '" + clazz.getCanonicalName() +
                        "' is not annotated as @JsonRpcService");
            }
            Method[] methods = searchClass.getMethods();
            for (Method method : methods) {
                Annotation[] methodAnnotations = method.getDeclaredAnnotations();
                JsonRpcMethod rpcMethodAnn = getAnnotation(methodAnnotations, JsonRpcMethod.class);
                if (rpcMethodAnn == null) {
                    throw new IllegalStateException("Method '" + method.getName() + "' is not annotated as @JsonRpcMethod");
                }

                // LinkedHashMap is needed to support method parameter ordering
                Map<String, ParameterMetadata> paramsMetadata = new LinkedHashMap<>(8);
                Annotation[][] parametersAnnotations = method.getParameterAnnotations();
                for (int i = 0; i < parametersAnnotations.length; i++) {
                    Annotation[] parametersAnnotation = parametersAnnotations[i];
                    // Check that it's a JSON-RPC param
                    JsonRpcParam rpcParamAnn = getAnnotation(parametersAnnotation, JsonRpcParam.class);
                    if (rpcParamAnn == null) {
                        throw new IllegalStateException("Parameter with index=" + i + " of method '" + method.getName() +
                                "' is not annotated with @JsonRpcParam");
                    }
                    // Check that's a param could be an optional
                    JsonRpcOptional optionalAnn = getAnnotation(parametersAnnotation, JsonRpcOptional.class);
                    ParameterMetadata parameterMetadata = new ParameterMetadata(i, optionalAnn != null);
                    if (paramsMetadata.put(rpcParamAnn.value(), parameterMetadata) != null) {
                        throw new IllegalStateException("Two parameters of method '" + method.getName() + "' have the " +
                                "same name '" + rpcParamAnn.value() + "'");
                    }

                }
                String name = !rpcMethodAnn.value().isEmpty() ? rpcMethodAnn.value() : method.getName();
                ParamsType paramsType = getParamsType(methodAnnotations);
                methodsMetadata.put(method, new MethodMetadata(name, paramsType, paramsMetadata));
            }
            searchClass = searchClass.getSuperclass();
        }

        Annotation[] classAnnotations = clazz != null ? clazz.getDeclaredAnnotations() : null;
        IdGenerator<?> idGenerator = getIdGenerator(classAnnotations);
        ParamsType paramsType = getParamsType(classAnnotations);
        return new ClassMetadata(paramsType, idGenerator, methodsMetadata);
    }


    /**
     * Get an actual id generator
     */
    private static IdGenerator<?> getIdGenerator(Annotation[] classAnnotations) {
        JsonRpcId jsonRpcIdAnn = getAnnotation(classAnnotations, JsonRpcId.class);
        Class<? extends IdGenerator<?>> idGeneratorClazz = (jsonRpcIdAnn == null) ?
                AtomicLongIdGenerator.class : jsonRpcIdAnn.value();
        try {
            return idGeneratorClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Unable instantiate id generator: " + idGeneratorClazz, e);
        }
    }

    @Nullable
    private static ParamsType getParamsType(Annotation[] annotations) {
        JsonRpcParams rpcParamsAnn = getAnnotation(annotations, JsonRpcParams.class);
        return rpcParamsAnn != null ? rpcParamsAnn.value() : null;

    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T extends Annotation> T getAnnotation(@Nullable Annotation[] annotations, Class<T> clazz) {
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation != null && annotation.annotationType().equals(clazz)) {
                    return (T) annotation;
                }
            }
        }
        return null;
    }
}
