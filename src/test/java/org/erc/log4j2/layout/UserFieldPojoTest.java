package org.erc.log4j2.layout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserFieldPojoTest {

	@Test
	public void testSetGet() {
		UserField field = new UserField("K", "V");
		assertEquals("Key","K",field.getKey());
		assertEquals("Value","V",field.getValue());
		
		field.setKey("K2");
		field.setValue("V2");
		assertEquals("Key","K2",field.getKey());
		assertEquals("Value","V2",field.getValue());
		
	}
	
	@Test
	public void testSetGetFromStatic() {
		UserField field = UserField.createUserField(null,"K", "V");
		assertEquals("Key","K",field.getKey());
		assertEquals("Value","V",field.getValue());
		
		field.setKey("K2");
		field.setValue("V2");
		assertEquals("Key","K2",field.getKey());
		assertEquals("Value","V2",field.getValue());
		
	}
	
	@Test
	public void testToString() {
		UserField field = UserField.createUserField(null,"K", "V");
		assertNotNull("has value",field.toString());
	}
	
	@Test
	public void hasCode() {
		UserField field = UserField.createUserField(null,"K", "V");
		assertNotNull("has value",field.hashCode());
		assertTrue("has value",field.hashCode()!=0);
		assertTrue("has value",field.hashCode()!=1);
		
		field = UserField.createUserField(null,null, null);
		assertTrue("has value",field.hashCode()==961);
	}
	
	
	@Test
	public void equals() {
		UserField field = UserField.createUserField(null,"K", "V");
		UserField field2 = UserField.createUserField(null,null,null);
		
		assertFalse("Not equals",field.equals(null));
		assertFalse("Not equals",field.equals(new Object()));
		assertFalse("Not equals",field.equals(new UserField("K","V1")));
		assertFalse("Not equals",field.equals(new UserField("K1","V1")));
		assertFalse("Not equals",field.equals(new UserField("K",null)));
		assertFalse("Not equals",field.equals(new UserField(null,null)));
		assertFalse("Not equals",field2.equals(new UserField("K1",null)));
		assertFalse("Not equals",field2.equals(new UserField(null,"V1")));
		
		assertTrue("equals",field.equals(field));
		assertTrue("equals",field.equals(new UserField("K","V")));
		assertTrue("equals",field2.equals(new UserField(null,null)));
	}
	
}
