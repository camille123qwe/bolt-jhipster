package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExecutionTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExecutionType getExecutionTypeSample1() {
        return new ExecutionType()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .label("label1")
            .description("description1");
    }

    public static ExecutionType getExecutionTypeSample2() {
        return new ExecutionType()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .label("label2")
            .description("description2");
    }

    public static ExecutionType getExecutionTypeRandomSampleGenerator() {
        return new ExecutionType()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
