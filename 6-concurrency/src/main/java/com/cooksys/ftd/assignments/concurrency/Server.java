package com.cooksys.ftd.assignments.concurrency;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.cooksys.ftd.assignments.concurrency.model.config.ServerConfig;

import Util.Debugger;

public class Server implements Runnable {
	private int port;
	private int maxClients;

	public Server(ServerConfig config) {
		Debugger.debugMessage("configuring server");
		this.port = config.getPort();
		this.maxClients = config.getMaxClients();
		Debugger.debugMessage("server configured");
	}

	@Override
	public void run() {

		try {
			Debugger.debugMessage("Starting server on port " + port);
			ServerSocket serverSocket = new ServerSocket(port);
 
			while (true) {
				Debugger.debugMessage("Server is waiting for client to connect");
				Socket client = serverSocket.accept();
				Debugger.debugMessage("Client accepted, creating new thread for client handler");
				new Thread(new ClientHandler(client)).start();
			}

		} catch (IOException e) {
			Debugger.errorStackTrace(e);
		}
	}
}
