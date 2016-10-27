package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;

import Util.ClientServerOutput;
import Util.Debugger;

public class ClientHandler implements Runnable {

	private Socket clientSocket;

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
    public void run() {
    	try {
    		//initialize our readers and writers for communication with the client
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String receivedMessage;
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			String writtenMessage;
					
			//send message to client acknowledging connection
			writtenMessage = "You have connected to the server.\n";
			writer.write(writtenMessage);
			writer.flush();
			ClientServerOutput.serverOutput("sent message: " + writtenMessage + " to client");
			
			//as long as we are connected to the client continue looking to requests and responding to them
			while(!clientSocket.isClosed()) {
				//check to see if a request was made
				if((receivedMessage = reader.readLine()) != null) {
					ClientServerOutput.serverOutput("Message received from Client: " + receivedMessage);
					Debugger.debugMessage("Server received a request from a client");
					
					//case statement to determine what request was made and fulfill it
					switch (receivedMessage) {
					case "IDENTITY":
						writer.write("Identity: " + clientSocket.getRemoteSocketAddress() + "\n");
						writer.flush();
						ClientServerOutput.serverOutput("Sent identity back to client");
						break;
					case "TIME":
						writer.write("Time: " + LocalDateTime.now() + "\n");
						writer.flush();
						ClientServerOutput.serverOutput("Sent time back to client");
						break;
					case "DONE":
						clientSocket.close();
						break;
					default: 
						writer.write("Request unknown\n");
						break;
					}
					Debugger.debugMessage("Server sent response to client");
				}	
			}
			Debugger.debugMessage("Ending a Client Handler");
    	} catch (IOException e) {
    		try {
				clientSocket.close();
			} catch (IOException e1) {
				Debugger.errorStackTrace(e1);
			}
			Debugger.errorStackTrace(e);
		}
    }
}