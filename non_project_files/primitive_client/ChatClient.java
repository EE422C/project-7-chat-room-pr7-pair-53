package day23network;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class ChatClient {
	private JTextArea incoming;
	private JTextField outgoing;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean typingSent;
	

	public void run() throws Exception {
		initView();
		setUpNetworking();
	}

	private void initView() {
		JFrame frame = new JFrame("Ludicrously Simple Chat Client");
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		outgoing = new JTextField(20);

		outgoing.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(!typingSent){
					writer.println("Guy is typing");
					writer.flush();
					typingSent=true;
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(!typingSent){
					writer.println("Guy is typing");
					writer.flush();
					typingSent=true;
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(!typingSent){
					writer.println("Guy is typing");
					writer.flush();
					typingSent=true;
				}
			}
		});
		typingSent=false;

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(650, 500);
		frame.setVisible(true);

	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		writer.println("Guy has joined the group");
		writer.flush();
	}

	class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			writer.println(outgoing.getText());
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
			typingSent=false;
		}
	}

	public static void main(String[] args) {
		try {
			new ChatClient().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					
						incoming.append(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
