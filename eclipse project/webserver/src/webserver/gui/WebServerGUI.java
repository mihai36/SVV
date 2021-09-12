package webserver.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import webserver.server.WebServer;

public class WebServerGUI {
	private static final String PANEL_START_STOP_LABEL = "WebServer Control";
	private static final String PANEL_INFO_LABEL = "WebServer Info";
	private static final String PANEL_CONFIG_LABEL = "WebServer Configuration";
	
	private static final String START_ICON_PATH = "/start-icon.png";
	private static final String STOP_ICON_PATH = "/stop-icon.png";
	
	private static final String SERVER_STATUS_LABEL = "Server status:";
	private static final String SERVER_ADDRESS_LABEL = "Server address:";
	private static final String SERVER_LISTENING_LABEL = "Server listening port:";
	
	private static final String SERVER_STATUS_OVALUE = "stopped";
	private static final String SERVER_ADDRESS_OVALUE = "-";
	private static final String SERVER_LISTENING_OVALUE = "" + WebServer.PORT;
	
	private static final String WEB_ROOT_DIRECTORY_LABEL = "Web root directory:";
	private static final String MAINTENANCE_DIRECTORY_LABEL = "Maintenance directory:";
	
	private static final String WEB_ROOT_DIRECTORY_OVALUE = "./htdocs/";
	private static final String MAINTENANCE_DIRECTORY_OVALUE = "./htdocs-maintenance/";
	
	private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
	private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);
	
	private final ImageIcon startIcon = new ImageIcon(WebServerGUI.class.getResource(START_ICON_PATH));
	private final ImageIcon stopIcon = new ImageIcon(WebServerGUI.class.getResource(STOP_ICON_PATH));
	
	private final JCheckBox maintenancechekcbox = new JCheckBox("Switch to maintenance mode");
	
	private final JTextField portField = new JTextField(SERVER_LISTENING_OVALUE);
	private final JTextField webRootField = new JTextField(WEB_ROOT_DIRECTORY_OVALUE);
	private final JTextField maintenanceField = new JTextField(MAINTENANCE_DIRECTORY_OVALUE);
	
	private final JButton webRootButton = new JButton("...");
	private final JButton maintenanceButton = new JButton("...");
	
	private final JLabel statusValue = new JLabel();
	private final JLabel addressValue = new JLabel();
	private final JLabel portValue = new JLabel();
	
	private Thread webServerThread = null;

	protected final JFrame frame = new JFrame();
	
	private volatile WebServer webServer;
	
	public WebServerGUI() {
		webServer = new WebServer();
		
		JPanel panelstartstop = new JPanel();
		panelstartstop.setBounds(420, 10, 400, 200); 
		panelstartstop.setBorder(BorderFactory.createTitledBorder(PANEL_START_STOP_LABEL));
		
		BoxLayout startstopLayout = new BoxLayout(panelstartstop, BoxLayout.Y_AXIS);
		panelstartstop.setLayout(startstopLayout);
				
		JButton startstopbutton = new JButton(startIcon);  
		startstopbutton.setText("Start");
		startstopbutton.setToolTipText("WebServer Start");
		startstopbutton.setMaximumSize(new Dimension(100, 50));
		startstopbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startstopbutton.setLayout(null);
		
		ActionListener startstoplistener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	try {
			    	if (webServer.isRunning()) {
						webServer.stopServer();
						webServerThread.join();
						
						JButton sender = (JButton)e.getSource();
				        sender.setText("Start");
				        sender.setToolTipText("WebServer Start");
				        sender.setIcon(startIcon);
				        
				        portField.setEnabled(true);
				        webRootField.setEnabled(true);
				        maintenanceField.setEnabled(true);
				        webRootButton.setEnabled(true);
				        maintenanceButton.setEnabled(true);
				        
				        statusValue.setText("stopped");
				        addressValue.setText("-");
				        portValue.setText("" + portField.getText());
				        
				        maintenancechekcbox.setEnabled(false);
				        maintenancechekcbox.setSelected(false);
					} else {
						webServer.setWebRoot(webRootField.getText());
						webServer.setPort(Integer.parseInt(portField.getText()));
						
						webServer.setMaintenanceRoot(maintenanceField.getText());
						
						System.out.println(portField.getText());
						
						webServerThread = new Thread(){
							public void run(){								
								System.out.println("Thread Running");
								System.out.println(portField.getText());							
								
								webServer.startServer();
							}
						};
						webServerThread.start();
						
						JButton sender = (JButton)e.getSource();
				        sender.setText("Stop");
				        sender.setToolTipText("WebServer Stop");
				        sender.setIcon(stopIcon);
				        
				        portField.setEnabled(false);
				        portField.setEnabled(false);
				        webRootField.setEnabled(false);
				        maintenanceField.setEnabled(false);
				        webRootButton.setEnabled(false);
				        maintenanceButton.setEnabled(false);
				        
				        statusValue.setText("started");
				        addressValue.setText("" + InetAddress.getLocalHost());
				        portValue.setText("" + portField.getText());
				        
				        maintenancechekcbox.setEnabled(true);
					}
		    	} catch (Exception e2) {
		    		System.err.println("ERROR...");
					System.err.println(e2.getMessage());
		    	}
		    }
		};
		
		startstopbutton.addActionListener(startstoplistener);
		
		panelstartstop.add(startstopbutton);
		
		maintenancechekcbox.setAlignmentX(Component.CENTER_ALIGNMENT);
		maintenancechekcbox.setEnabled(false);
		
		ItemListener maintenancelistener = new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					webServer.stopServer();
					webServerThread.join();
					
					webServer.setWebRoot(webRootField.getText());					
					webServer.setPort(Integer.parseInt(portField.getText()));
					
					webServer.setMaintenanceRoot(maintenanceField.getText());
					
					if (maintenancechekcbox.isSelected()) {
						// should be here only if server is running
						webServer.setUnderMaintenance(true);
					} else {
						// arrived here after the selection has been created
						webServer.setUnderMaintenance(false);
					}
					
					System.out.println(portField.getText());
					
					webServerThread = new Thread(){
						public void run(){								
							System.out.println("Thread Running");
							System.out.println(portField.getText());							
							
							webServer.startServer();
						}
					};
					webServerThread.start();
				} catch (Exception ex) {
					System.err.println("ERROR...");
					System.err.println(ex.getMessage());
				}
			}
		};
		
		maintenancechekcbox.addItemListener(maintenancelistener);
		
		panelstartstop.add(maintenancechekcbox);
		
		JPanel panelinfo = new JPanel();
		panelinfo.setBounds(10, 10, 400, 200); 
		panelinfo.setBorder(BorderFactory.createTitledBorder(PANEL_INFO_LABEL));
		
		GridBagLayout infoLayout = new GridBagLayout();
		
		panelinfo.setLayout(infoLayout);
		
        JLabel statusLabel = new JLabel();
        statusLabel.setText(SERVER_STATUS_LABEL);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelinfo.add(statusLabel, createGbc(0, 0, 1, 1));
        
        statusValue.setText(SERVER_STATUS_OVALUE);
        statusValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusValue.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelinfo.add(statusValue, createGbc(1, 0, 1, 1));
        
        JLabel addressLabel = new JLabel();
        addressLabel.setText(SERVER_ADDRESS_LABEL);
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addressLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelinfo.add(addressLabel, createGbc(0, 1, 1, 1));
        
        addressValue.setText(SERVER_ADDRESS_OVALUE);
        addressValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        addressValue.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelinfo.add(addressValue, createGbc(1, 1, 1, 1));
        
        JLabel portLabel = new JLabel();
        portLabel.setText(SERVER_LISTENING_LABEL);
        portLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        portLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelinfo.add(portLabel, createGbc(0, 2, 1, 1));
        
        portValue.setText(SERVER_LISTENING_OVALUE);
        portValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        portValue.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelinfo.add(portValue, createGbc(1, 2, 1, 1));

		JPanel panelconfig = new JPanel();
		panelconfig.setBounds(10, 230, 810, 200); 
		panelconfig.setBorder(BorderFactory.createTitledBorder(PANEL_CONFIG_LABEL));
		
		GridBagLayout configLayout = new GridBagLayout();
		
		panelconfig.setLayout(configLayout);
		
		JLabel portInputLabel = new JLabel();
		portInputLabel.setText(SERVER_LISTENING_LABEL);
		portInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		portInputLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelconfig.add(portInputLabel, createGbc(0, 0, 1, 1));
        
        portField.setPreferredSize(new Dimension(100, 20));
        portField.setBorder(BorderFactory.createEmptyBorder());
        panelconfig.add(portField, createGbc(1, 0, 2, 1));
             
        JLabel webrootInputLabel = new JLabel();
        webrootInputLabel.setText(WEB_ROOT_DIRECTORY_LABEL);
        webrootInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        webrootInputLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelconfig.add(webrootInputLabel, createGbc(0, 1, 1, 1));
        
        webRootField.setPreferredSize(new Dimension(150, 20));
        webRootField.setBorder(BorderFactory.createEmptyBorder());
        panelconfig.add(webRootField, createGbc(1, 1, 1, 1));
        
        webRootButton.setPreferredSize(new Dimension(20, 20));
        panelconfig.add(webRootButton, createGbc(2, 1, 1, 1));
                
        JLabel maintenanceInputLabel = new JLabel();
        maintenanceInputLabel.setText(MAINTENANCE_DIRECTORY_LABEL);
        maintenanceInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        maintenanceInputLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelconfig.add(maintenanceInputLabel, createGbc(0, 2, 1, 1));
            
        maintenanceField.setPreferredSize(new Dimension(150, 20));
        maintenanceField.setBorder(BorderFactory.createEmptyBorder());
        panelconfig.add(maintenanceField, createGbc(1, 2, 1, 1));
        
        maintenanceButton.setPreferredSize(new Dimension(20, 20));
        panelconfig.add(maintenanceButton, createGbc(2, 2, 1, 1));
        
		frame.add(panelstartstop);
		frame.add(panelinfo); 
		frame.add(panelconfig); 
		          
		frame.setSize(845,500);  
		frame.setLayout(null);  
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				webServer.stopServer();
			}
		});
	}
	
	private GridBagConstraints createGbc(int x, int y, int gw, int gh) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;

		gbc.anchor = GridBagConstraints.WEST;
		// gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
		// gbc.fill = (x == 0) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;

	    gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
	    gbc.weightx = (x == 0) ? 0.1 : 1.0;
	    gbc.weighty = 1.0;
	    return gbc;
	} 

	public static void main(String[] args) {
		new WebServerGUI();
	}

}
