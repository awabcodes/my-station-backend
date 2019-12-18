package com.awabcodes.mystation.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.awabcodes.mystation.web.rest.TestUtil;

public class SuggestionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Suggestion.class);
        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(1L);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(suggestion1.getId());
        assertThat(suggestion1).isEqualTo(suggestion2);
        suggestion2.setId(2L);
        assertThat(suggestion1).isNotEqualTo(suggestion2);
        suggestion1.setId(null);
        assertThat(suggestion1).isNotEqualTo(suggestion2);
    }
}
