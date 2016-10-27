package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import com.cooksys.ftd.assignments.concurrency.model.config.ClientInstanceConfig;
import com.cooksys.ftd.assignments.concurrency.model.message.Request;

import Util.ClientServerOutput;
import Util.Debugger;

public class ClientInstance implements Runnable {

	private int delay;
	private List<Request> requests;
	private Socket clientSocket;

	public ClientInstance(ClientInstanceConfig config, Socket clientSocket) {
		this.delay = config.getDelay();
		this.requests = config.getRequests();
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			//make sure that the socket is not closed
			if (!clientSocket.isClosed()) {
				//initialize the readers and writers for communicating with the server
				BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String receivedMessage;
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
				
				//the server will send a message confirming connection, wait until we receive this to move on
				while ((receivedMessage = reader.readLine()) == null) {
					//check once every hundredth of a second to see if we received our response
					Thread.sleep(10);
				}
				ClientServerOutput.clientOutput("Message received from server: " + receivedMessage);
				
				//once connection is confirmed begin sending requests one at a time
				for (Request request : requests) {
					ClientServerOutput.clientOutput("Requesting " + request.getType() + " from server");
					writer.write(request.getType().name() + "\n");
					writer.flush();
					
					//wait until we receive a response to our request before moving on to the next request
					while ((receivedMessage = reader.readLine()) == null) {
						//check once every hundredth of a second to see if we received our response
						Thread.sleep(10);
					}
					ClientServerOutput.clientOutput("Message received from server: " + receivedMessage);
				}
				
				//tell the server to close communication
				Debugger.debugMessage("Closing client socket");
//				writer.write("DONE\n");
//				writer.flush();
				clientSocket.close();
				
//				//now that we have sent our requests keep the connection open until all requests have been received
//				int receivedResponses = 0;
//				int expectedResponses = requests.size();
//				while (receivedResponses != expectedResponses) {
//					if ((receivedMessage = reader.readLine()) != null) {
//						ClientServerOutput.clientOutput("Message received from server: " + receivedMessage);
//						receivedResponses++;
//					}
//				}
				Debugger.debugMessage("Client received all messages");
			}
		} catch (IOException e) {
			Debugger.errorStackTrace(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
