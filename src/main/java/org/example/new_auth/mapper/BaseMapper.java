package org.example.new_auth.mapper;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class BaseMapper {

    protected <T, R> List<R> mapList(List<T> source, Function<T, R> mapper) {
        return Objects.isNull(source) ? List.of() : source.stream().map(mapper).toList();
    }

}
