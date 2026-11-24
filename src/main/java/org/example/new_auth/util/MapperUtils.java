package org.example.new_auth.util;

import org.example.new_auth.batch.BatchResult;

import java.util.List;
import java.util.function.Function;

public final class MapperUtils {

    private MapperUtils() {
    }

    public static <T, R> List<R> mapList(List<T> source, Function<T, R> mapper) {
        return source == null ? List.of() : source.stream().map(mapper).toList();
    }

    public static <T, R> BatchResult<R> mapBatchResult(BatchResult<T> source, Function<List<T>, List<R>> mapper) {
        return new BatchResult<>(mapper.apply(source.success()), source.errors());
    }

}
