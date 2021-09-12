package webserver.server;

import java.net.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.io.*;

public class WebServer extends Thread {
	static final String WEB_ROOT_PATH = "htdocs";
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String METHOD_NOT_SUPPORTED = "405.html";
	// port to listen connection
	public static final int PORT = 10008;
	
	protected volatile String webRoot = WEB_ROOT_PATH;
	protected volatile String maintenanceRoot = WEB_ROOT_PATH;
	protected volatile int port = PORT;
	protected volatile boolean underMaintenance = false;
	
	protected volatile boolean running = false;
	
	protected Socket clientSocket;
	protected ServerSocket serverSocket;

	public WebServer() {
		
	}
	
	protected WebServer(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void stopServer() {
		try {
			this.running = false;
			this.serverSocket.close();
		} catch (Exception e) {
			
		} finally {
			
		}
	}
	
	public void startServer() {	
		this.running = true;
		
		serverSocket = null;
		
		System.out.println(getWebRoot());
		
		try {
			serverSocket = new ServerSocket(this.port);
			System.out.println("Connection Socket Created\nListening for connections on port : " + this.port + "\n");
			try {
				while (running) {
					System.out.println("Waiting for Connection");
					WebServer server = new WebServer();
					server.setPort(this.getPort());
					server.setWebRoot(this.getWebRoot());
					server.setMaintenanceRoot(this.getMaintenanceRoot());
					server.setUnderMaintenance(this.isUnderMaintenance());
					Socket cs = serverSocket.accept();
					server.setClientSocket(cs);
					
					System.out.println("Connection accepted");
					// create dedicated thread to manage the client connection
					Thread thread = new Thread(server);
					thread.start();
				}
			} catch (IOException e) {
				System.err.println("Accept failed. - probably closing...");
				// System.exit(1);
			}
		} catch (Exception e) {
			System.err.println("ERROR...");
			System.err.println(e.getMessage());
		} finally {
			try {				
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port.");
				System.exit(1);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Connection Socket Created\nListening for connections on port : " + PORT + "\n");
			try {
				while (true) {
					System.out.println("Waiting for Connection");
					WebServer server = new WebServer();					
					Socket cs = serverSocket.accept();
					server.setClientSocket(cs);
										
					System.out.println("Connection accepted");
					// create dedicated thread to manage the client connection
					Thread thread = new Thread(server);
					thread.start();
				}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port.");
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port.");
				System.exit(1);
			}
		}
	}
	
	private byte[] readFileFromDisk(File file, int fileLength) throws IOException {
		FileInputStream in = null;
		byte[] data = new byte[fileLength];
		
		try {
			in = new FileInputStream(file);
			in.read(data);
		} finally {
			if (in != null) 
				in.close();
		}
		
		return data;
	}
	
	private String getContentType(String file) {
		if (file.endsWith(".htm") || file.endsWith(".html")) {
			return "text/html";
		} else {
			return "text/plain";
		}
	}

	@Override
	public void run() {
		System.out.println(getWebRoot());
		
		// attend to the request
		BufferedReader in = null;
		PrintWriter out = null;
		
		BufferedOutputStream binFileOut = null;
		
		// the requested file path 
		String file = null;
		
		File webRootFile;
		if (isUnderMaintenance()) {
			webRootFile = new File("./" + getMaintenanceRoot() + "/");
		} else {
			webRootFile = new File("./" + getWebRoot() + "/");
		}
		
		System.out.println("+++++++++++++++++UNDER MAIN : " + (isUnderMaintenance() ? "DA" : "NU"));
		
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// write here the simple text request
			out = new PrintWriter(clientSocket.getOutputStream());
			
			// write here the binary / text file which was requested
			binFileOut = new BufferedOutputStream(clientSocket.getOutputStream());
			
			// read the first line
			String input = in.readLine();
						
			// split the first line
			if (input == null || isUnderMaintenance()) {
				input = "GET /";
			}
			StringTokenizer parse = new StringTokenizer(input);
			
			String method = parse.nextToken().toUpperCase(); // GET or POST or something else
			
			// this is the file
			file = parse.nextToken().toLowerCase();
			
			// HEAD very similar to GET, to check whether the file actually exists, but not download it. just 200 / 404 status codes
			if (!method.equals("GET")  &&  !method.equals("HEAD")) {
				// return method not supported file
				File fileToSend = new File(webRootFile, METHOD_NOT_SUPPORTED);
				int fileLength = (int) fileToSend.length();
				String contentMimeType = "text/html";
				
				// read file from disk
				byte[] fileData = readFileFromDisk(fileToSend, fileLength);
					
				// headers for method not implemented according to HTML standard
				out.println("HTTP/1.1 501 Not Implemented");
				out.println("Server: HTTP Server for SVV");
				out.println("Date: " + new Date());
				out.println("Content-type: " + contentMimeType);
				out.println("Content-length: " + fileLength);
				
				// output a blank, empty line after the headers, before coontent
				out.println(); 
				out.flush(); // flush character output stream buffer
				
				// file
				binFileOut.write(fileData, 0, fileLength);
				binFileOut.flush();
			} else {
				// GET or HEAD method
				
				// if a folder / no file was requested, append the default file to it
				if (file.endsWith("/")) {
					file += DEFAULT_FILE;
				}
				
				// replace %20 with a literal space
				if (file.contains("%20")) {
					file = file.replaceAll("%20", " ");
				}
				
				File fileToSend = new File(webRootFile, file);
								
				int fileLength = (int) fileToSend.length();
				String content = getContentType(file);
				
				// download the file only if GET
				if (method.equals("GET")) {
					byte[] fileData = readFileFromDisk(fileToSend, fileLength);
					
					// send HTTP Headers
					out.println("HTTP/1.1 200 OK");
					out.println("Server: HTTP Server for SVV");
					out.println("Date: " + new Date());
					out.println("Content-type: " + content);
					out.println("Content-length: " + fileLength);
					
					// output a blank, empty line after the headers, before coontent
					out.println(); 
					out.flush(); // flush character output stream buffer
					
					binFileOut.write(fileData, 0, fileLength);
					binFileOut.flush();
				}
			}
		} catch (FileNotFoundException fnfe) {
			try {
				// return custom 404 file
				File fileToSend = new File(webRootFile, FILE_NOT_FOUND);
				int fileLength = (int) fileToSend.length();
				String content = "text/html";
				byte[] fileData = readFileFromDisk(fileToSend, fileLength);
				
				out.println("HTTP/1.1 404 File Not Found");
				out.println("Server: HTTP Server for SVV");
				out.println("Date: " + new Date());
				out.println("Content-type: " + content);
				out.println("Content-length: " + fileLength);
				
				// output a blank, empty line after the headers, before coontent
				out.println(); 
				out.flush(); // flush character output stream buffer
				
				binFileOut.write(fileData, 0, fileLength);
				binFileOut.flush();
			} catch (IOException ex) {
				System.err.println("Error while getting a file not found exception : " + ex.getMessage());
			}
			
		} catch (IOException ex) {			
			System.err.println("General server IO error : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				binFileOut.close();
				clientSocket.close(); // we close socket connection
			} catch (Exception ex) {
				System.err.println("Error while closing stream : " + ex.getMessage());
			} 
		}
	}

	public String getWebRoot() {
		return webRoot;
	}

	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	public String getMaintenanceRoot() {
		return maintenanceRoot;
	}

	public void setMaintenanceRoot(String maintenanceRoot) {
		this.maintenanceRoot = maintenanceRoot;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public boolean isUnderMaintenance() {
		return underMaintenance;
	}

	public void setUnderMaintenance(boolean underMaintenante) {
		this.underMaintenance = underMaintenante;
	}
}