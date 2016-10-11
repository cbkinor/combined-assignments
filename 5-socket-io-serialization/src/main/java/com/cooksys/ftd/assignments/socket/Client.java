package com.cooksys.ftd.assignments.socket;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.socket.model.*;

public class Client {

    /**
     * The client should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" and "host" properties of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.RemoteConfig} object to create a socket that connects to
     * a {@link Server} listening on the given host and port.
     *
     * The client should expect the server to send a {@link com.cooksys.ftd.assignments.socket.model.Student} object
     * over the socket as xml, and should unmarshal that object before printing its details to the console.
     */
    public static void main(String[] args) throws UnknownHostException, IOException, JAXBException {
        Unmarshaller data = Utils.createJAXBContext().createUnmarshaller();
        File file = new File("config/config.xml");
        Config Config = (Config)data.unmarshal(file);
        
        Socket socket = null;
        InputStream in = null;
        
        try {
			socket = new Socket(Config.getRemote().getHost(), Config.getRemote().getPort());
			in = socket.getInputStream();
        	Unmarshaller jaxUnmarshaller = Utils.createJAXBContext().createUnmarshaller();
            Student student = (Student) jaxUnmarshaller.unmarshal(in);
            System.out.println(student);
        }
        catch (JAXBException e) {
			e.printStackTrace();
		}
        finally {
        	if(socket != null)
        		socket.close();
	        if(in != null)
	        	in.close();
		}
    }
}
