package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StrategyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Strategy getStrategySample1() {
        return new Strategy()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .label("label1")
            .description("description1")
            .executionRule("executionRule1");
    }

    public static Strategy getStrategySample2() {
        return new Strategy()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .label("label2")
            .description("description2")
            .executionRule("executionRule2");
    }

    public static Strategy getStrategyRandomSampleGenerator() {
        return new Strategy()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .executionRule(UUID.randomUUID().toString());
    }
}
