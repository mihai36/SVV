package webserver.tests.junit;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import webserver.server.WebServer;

public class WebServerTest {
	private WebServer webServer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		webServer = new WebServer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_port_setter() {
		int expectedvalue = 10000;
		webServer.setPort(expectedvalue);
		
		try {
			final Field field = webServer.getClass().getDeclaredField("port");
			field.setAccessible(true);
			
			assertEquals("Port Fields did not match ON SET", field.get(webServer), expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}
	
	@Test
	public void test_port_getter() {
		int expectedvalue = 10000;
		
		try {
			final Field field = webServer.getClass().getDeclaredField("port");
			field.setAccessible(true);
			field.set(webServer, expectedvalue);
			
			int result = webServer.getPort();
			
			assertEquals("Port field was not retrieved properly ON GEt", result, expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}
	
	@Test
	public void test_webRoot_setter() {
		String expectedvalue = "htdocstest";
		webServer.setWebRoot(expectedvalue);
		
		try {
			final Field field = webServer.getClass().getDeclaredField("webRoot");
			field.setAccessible(true);
			
			assertEquals("webRoot fields did not match ON SET", field.get(webServer), expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}
	
	@Test
	public void test_webRoot_getter() {
		String expectedvalue = "htdocstest";
		
		try {
			final Field field = webServer.getClass().getDeclaredField("webRoot");
			field.setAccessible(true);
			field.set(webServer, expectedvalue);
			
			String result = webServer.getWebRoot();
			
			assertEquals("webRoot field was not retrieved properly ON GEt", result, expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}

	@Test
	public void test_maintenanceRoot_setter() {
		String expectedvalue = "htdocstest";
		webServer.setMaintenanceRoot(expectedvalue);
		
		try {
			final Field field = webServer.getClass().getDeclaredField("maintenanceRoot");
			field.setAccessible(true);
			
			assertEquals("maintenanceRoot fields did not match ON SET", field.get(webServer), expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}
	
	@Test
	public void test_maintenanceRoot_getter() {
		String expectedvalue = "htdocstest";
		
		try {
			final Field field = webServer.getClass().getDeclaredField("maintenanceRoot");
			field.setAccessible(true);
			field.set(webServer, expectedvalue);
			
			String result = webServer.getMaintenanceRoot();
			
			assertEquals("maintenanceRoot field was not retrieved properly ON GEt", result, expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}
	
	@Test
	public void test_running_false_setter() {
		boolean expectedvalue = false;
		webServer.setRunning(expectedvalue);
		
		try {
			final Field field = webServer.getClass().getDeclaredField("running");
			field.setAccessible(true);
			
			assertEquals("running fields did not match ON SET", field.get(webServer), expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}
	
	@Test
	public void test_running_false_getter() {
		boolean expectedvalue = false;
		
		try {
			final Field field = webServer.getClass().getDeclaredField("running");
			field.setAccessible(true);
			field.set(webServer, expectedvalue);
			
			boolean result = webServer.isRunning();
			
			assertEquals("maintenanceRoot field was not retrieved properly ON GEt", result, expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}

	@Test
	public void test_running_tru_setter() {
		boolean expectedvalue = true;
		webServer.setRunning(expectedvalue);
		
		try {
			final Field field = webServer.getClass().getDeclaredField("running");
			field.setAccessible(true);
			
			assertEquals("running fields did not match ON SET", field.get(webServer), expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}
	
	@Test
	public void test_running_true_getter() {
		boolean expectedvalue = true;
		
		try {
			final Field field = webServer.getClass().getDeclaredField("running");
			field.setAccessible(true);
			field.set(webServer, expectedvalue);
			
			boolean result = webServer.isRunning();
			
			assertEquals("maintenanceRoot field was not retrieved properly ON GEt", result, expectedvalue);
			
		} catch (NoSuchFieldException nsfex) {
			fail("There is not a port field in the object");
		} catch (IllegalAccessException iaex) {
			fail("Cannot access the field Port");
		}
	}
}
