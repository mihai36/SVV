package webserver.tests.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import webserver.server.WebServer;

public class WebServerRunningTest {
	private static final int PORT = 10077;
	private static final String WEB_ROOT = "./htdocs/";
	private static final String MAINTENANCE_ROOT = "./htdocs/";
	private WebServer webServer;
	private Thread webServerThread;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		webServer = new WebServer();
		
		webServer.setPort(PORT);
		webServer.setWebRoot(WEB_ROOT);
		webServer.setMaintenanceRoot(MAINTENANCE_ROOT);
		webServer.setUnderMaintenance(false);
		
		webServerThread = new Thread(){
			public void run(){								
				System.out.println("Thread Running");							
				
				webServer.startServer();
			}
		};
		webServerThread.start();
	}

	@After
	public void tearDown() throws Exception {
		webServer.stopServer();
		webServerThread.join();
	}

	@Test(timeout=10000)
	public void test_server_running_by_flag() {
		boolean expectedvalue = true;
		assertEquals("Server does not seem to be running based on flag", webServer.isRunning(), expectedvalue);
	}
	
	@Test(timeout=10000)
	public void test_server_running_by_server_port() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
		    assertNotNull(serverSocket);
		    assertEquals("Server does not seem to be running based on port", serverSocket.getLocalPort(), PORT);
		    serverSocket.close();
		    assertTrue("Port is not in use, server must be down", false);		    
		} catch (IOException e) {
		    return;
		}
	}
	
	@Test(timeout=10000)
	public void test_server_running_by_remote_port() {		
		try {
			Socket socket = new Socket("localhost", PORT);
		    assertNotNull(socket);
		    socket.close();
		} catch (IOException e) {
			assertTrue("Port is not in use, server must be down", false);
		}
	}
	
	@Test(timeout=10000)
	public void test_server_running_by_web_client() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder(
				       URI.create("http://localhost:" + PORT + "/a.html"))
				   .build();
			HttpResponse<String> response =
			          client.send(request, BodyHandlers.ofString());	
			System.out.println(response.body());
		} catch (InterruptedException iex) {
			fail("Interrupted in htttclient");
		} catch (IOException iex) {
			fail("IO Exception in htttclient");
		}
	}

}
