package org.example.new_auth.util;

import org.example.new_auth.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public final class AuthUtils {

    private AuthUtils() {
    }

    public static <T> Stream<T> nonNullStream(Collection<T> source) {
        return Stream.ofNullable(source)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull);
    }

    public static <T> Stream<T> ofNullableStream(Collection<T> source) {
        return Stream.ofNullable(source)
                .flatMap(Collection::stream);
    }

    public static <R, T> List<T> extractUniqueFromUsers(Collection<User> users, Function<User, Stream<R>> extractor, Function<R, T> mapper) {
        return nonNullStream(users)
                .flatMap(extractor)
                .map(mapper)
                .distinct()
                .toList();
    }

}
