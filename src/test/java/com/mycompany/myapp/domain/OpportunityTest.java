package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class OpportunityTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opportunity.class);
        Opportunity opportunity1 = new Opportunity();
        opportunity1.setId(1L);
        Opportunity opportunity2 = new Opportunity();
        opportunity2.setId(opportunity1.getId());
        assertThat(opportunity1).isEqualTo(opportunity2);
        opportunity2.setId(2L);
        assertThat(opportunity1).isNotEqualTo(opportunity2);
        opportunity1.setId(null);
        assertThat(opportunity1).isNotEqualTo(opportunity2);
    }
}
