package io.github.luidmidev.jakarta.validations.structs;

public interface ThrowableFunction<T, R, E extends Throwable> {
    R apply(T t) throws E;

}
