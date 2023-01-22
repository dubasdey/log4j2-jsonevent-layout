package org.erc.log4j2.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User Field POJO Test")
class UserFieldPojoTest {

    @Test
    void testSetGet() {
        UserField field = new UserField("K", "V");
        assertEquals("K", field.getKey(), "Key");
        assertEquals("V", field.getValue(), "Value");

        field.setKey("K2");
        field.setValue("V2");
        assertEquals("K2", field.getKey(), "Key");
        assertEquals("V2", field.getValue(), "Value");

    }

    @Test
    void testSetGetFromStatic() {
        UserField field = UserField.createUserField(null, "K", "V");
        assertEquals("K", field.getKey(), "Key");
        assertEquals("V", field.getValue(), "Value");

        field.setKey("K2");
        field.setValue("V2");
        assertEquals("K2", field.getKey(), "Key");
        assertEquals("V2", field.getValue(), "Value");

    }

    @Test
    void testToString() {
        UserField field = UserField.createUserField(null, "K", "V");
        assertNotNull(field.toString(), "has value");
    }

    @Test
    void hasCode() {
        UserField field = UserField.createUserField(null, "K", "V");
        assertNotNull(field.hashCode(), "has value");
        assertTrue(field.hashCode() != 0, "has value");
        assertTrue(field.hashCode() != 1, "has value");

        field = UserField.createUserField(null, null, null);
        assertTrue(field.hashCode() == 961, "has value");
    }

    @Test
    void equals() {
        UserField field = UserField.createUserField(null, "K", "V");
        UserField field2 = UserField.createUserField(null, null, null);


        assertNotEquals(field, null, "Not equals");
        assertFalse(field.equals(new Object()), "Not equals");
        assertFalse(field.equals(new UserField("K", "V1")), "Not equals");
        assertFalse(field.equals(new UserField("K1", "V1")), "Not equals");
        assertFalse(field.equals(new UserField("K", null)), "Not equals");
        assertFalse(field.equals(new UserField(null, null)), "Not equals");
        assertFalse(field2.equals(new UserField("K1", null)), "Not equals");
        assertFalse(field2.equals(new UserField(null, "V1")), "Not equals");

        assertTrue(field.equals(field), "equals");
        assertTrue(field.equals(new UserField("K", "V")), "equals");
        assertTrue(field2.equals(new UserField(null, null)), "equals");
    }

}
