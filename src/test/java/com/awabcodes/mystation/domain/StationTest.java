package com.awabcodes.mystation.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.awabcodes.mystation.web.rest.TestUtil;

public class StationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Station.class);
        Station station1 = new Station();
        station1.setId(1L);
        Station station2 = new Station();
        station2.setId(station1.getId());
        assertThat(station1).isEqualTo(station2);
        station2.setId(2L);
        assertThat(station1).isNotEqualTo(station2);
        station1.setId(null);
        assertThat(station1).isNotEqualTo(station2);
    }
}
