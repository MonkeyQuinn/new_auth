package org.example.new_auth.util;

import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.domain.User;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UserUtils {

    private UserUtils() {
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

    public static boolean hasAllRequired(User user, Set<String> required, Function<Permission, String> mapper) {
        if (required == null || required.isEmpty()) return true;

        Set<String> userValues = nonNullStream(user.getPermissions())
                .map(mapper)
                .collect(Collectors.toSet());

        return userValues.containsAll(required);
    }

    public static <T> List<T> extractUniqueFromUsers(Collection<User> users, Function<Permission, T> extractor) {
        return nonNullStream(users)
                .flatMap(user -> nonNullStream(user.getPermissions()))
                .map(extractor)
                .distinct()
                .toList();
    }

}
