package org.example.new_auth.batch;

public record BatchParams(int pack, int interval) {

    public static final int DEFAULT_PACK = 100;
    public static final int DEFAULT_INTERVAL = 15000;

    public BatchParams {
        if (pack <= 0) throw new IllegalArgumentException("Parameter 'pack' must be greater than 0");
        if (interval < 5000) throw new IllegalArgumentException("Parameter 'interval' must be greater than or equal to 5000");
    }

    public static BatchParams defaults() {
        return new BatchParams(DEFAULT_PACK, DEFAULT_INTERVAL);
    }

}
