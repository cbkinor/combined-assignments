package com.cooksys.ftd.assignments.concurrency;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import com.cooksys.ftd.assignments.concurrency.model.config.Config;
import com.cooksys.ftd.assignments.concurrency.model.config.ServerConfig;

import Util.Debugger;
import javassist.compiler.Javac;

public class Main {

	/**
	 * First, load a
	 * {@link com.cooksys.ftd.assignments.concurrency.model.config.Config}
	 * object from the <project-root>/config/config.xml file.
	 *
	 * If the embedded
	 * {@link com.cooksys.ftd.assignments.concurrency.model.config.ServerConfig}
	 * object is not disabled, create a {@link Server} object with the server
	 * config and spin off a thread to run it.
	 *
	 * If the embedded
	 * {@link com.cooksys.ftd.assignments.concurrency.model.config.ClientConfig}
	 * object is not disabled, create a {@link Client} object with the client
	 * config ans spin off a thread to run it.
	 * 
	 * @throws JAXBException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws JAXBException, InterruptedException {
		Path configPath = Paths.get("./config/config.xml");
		Config config = Config.load(configPath);

		// start the thread for managing the server if it is not disabled
		if (!config.getServer().isDisabled()) {
			Debugger.debugMessage("Starting new thread for our server");
			Server server = new Server(config.getServer());
			new Thread(server).start();
		}
		
		Thread.sleep(500);
		
		// start the thread to configure client instances if they are not disabeled
		if (!config.getClient().isDisabled()) {
			Client client = new Client(config.getClient());
			Debugger.debugMessage("Starting up client manager on new thread");
			new Thread(client).start();

		}
		
		Thread.sleep(2000);
		System.out.println(Thread.activeCount());
	}
}
