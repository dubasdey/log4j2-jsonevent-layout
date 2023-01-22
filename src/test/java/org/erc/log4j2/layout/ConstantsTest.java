package org.erc.log4j2.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Constants")
class ConstantsTest {

    @Test
    void test() {
        assertNotNull(Constants.VERSION);
        assertEquals("1", Constants.VERSION);
    }
}
