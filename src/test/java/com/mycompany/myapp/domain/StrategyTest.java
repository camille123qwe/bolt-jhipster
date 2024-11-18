package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ExecutionTypeTestSamples.getExecutionTypeRandomSampleGenerator;
import static com.mycompany.myapp.domain.ProjectTestSamples.getProjectRandomSampleGenerator;
import static com.mycompany.myapp.domain.SourceTestSamples.getSourceRandomSampleGenerator;
import static com.mycompany.myapp.domain.StrategyTestSamples.getStrategyRandomSampleGenerator;
import static com.mycompany.myapp.domain.StrategyTestSamples.getStrategySample1;
import static com.mycompany.myapp.domain.StrategyTestSamples.getStrategySample2;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StrategyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Strategy.class);
        Strategy strategy1 = getStrategySample1();
        Strategy strategy2 = new Strategy();
        assertThat(strategy1).isNotEqualTo(strategy2);

        strategy2.setId(strategy1.getId());
        assertThat(strategy1).isEqualTo(strategy2);

        strategy2 = getStrategySample2();
        assertThat(strategy1).isNotEqualTo(strategy2);
    }

    @Test
    void executionTypeTest() {
        Strategy strategy = getStrategyRandomSampleGenerator();
        ExecutionType executionTypeBack = getExecutionTypeRandomSampleGenerator();

        strategy.setExecutionType(executionTypeBack);
        assertThat(strategy.getExecutionType()).isEqualTo(executionTypeBack);

        strategy.executionType(null);
        assertThat(strategy.getExecutionType()).isNull();
    }

    @Test
    void sourceTest() {
        Strategy strategy = getStrategyRandomSampleGenerator();
        Source sourceBack = getSourceRandomSampleGenerator();

        strategy.setSource(sourceBack);
        assertThat(strategy.getSource()).isEqualTo(sourceBack);

        strategy.source(null);
        assertThat(strategy.getSource()).isNull();
    }

    @Test
    void projectTest() {
        Strategy strategy = getStrategyRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        strategy.setProject(projectBack);
        assertThat(strategy.getProject()).isEqualTo(projectBack);

        strategy.project(null);
        assertThat(strategy.getProject()).isNull();
    }
}
