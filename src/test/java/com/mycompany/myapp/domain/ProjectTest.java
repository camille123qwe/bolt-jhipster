package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CustomerTestSamples.getCustomerRandomSampleGenerator;
import static com.mycompany.myapp.domain.ProjectTestSamples.getProjectRandomSampleGenerator;
import static com.mycompany.myapp.domain.ProjectTestSamples.getProjectSample1;
import static com.mycompany.myapp.domain.ProjectTestSamples.getProjectSample2;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void customerTest() {
        Project project = getProjectRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        project.setCustomer(customerBack);
        assertThat(project.getCustomer()).isEqualTo(customerBack);

        project.customer(null);
        assertThat(project.getCustomer()).isNull();
    }
}
