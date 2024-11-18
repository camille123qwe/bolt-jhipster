package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ExecutionTypeTestSamples.getExecutionTypeSample1;
import static com.mycompany.myapp.domain.ExecutionTypeTestSamples.getExecutionTypeSample2;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExecutionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutionType.class);
        ExecutionType executionType1 = getExecutionTypeSample1();
        ExecutionType executionType2 = new ExecutionType();
        assertThat(executionType1).isNotEqualTo(executionType2);

        executionType2.setId(executionType1.getId());
        assertThat(executionType1).isEqualTo(executionType2);

        executionType2 = getExecutionTypeSample2();
        assertThat(executionType1).isNotEqualTo(executionType2);
    }
}
