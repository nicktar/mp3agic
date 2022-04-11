package com.mpatric.mp3agic;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class ID3v2FrameSetTest {

    @Test
    void shouldCorrectlyImplementHashCodeAndEquals() {
        EqualsVerifier.forClass(ID3v2FrameSet.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}
