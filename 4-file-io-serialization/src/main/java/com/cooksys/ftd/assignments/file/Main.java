package com.cooksys.serialization.assignment;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.cooksys.serialization.assignment.model.*;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cooksys.serialization.assignment.model.Contact;
import com.cooksys.serialization.assignment.model.Instructor;
import com.cooksys.serialization.assignment.model.Session;
import com.cooksys.serialization.assignment.model.Student;

public class Main {

	
    /**
     * Creates a {@link Student} object using the given studentContactFile.
     * The studentContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param studentContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return a {@link Student} object built using the {@link Contact} data in the given file
     */
	
    public static Student readStudent(File studentContactFile, JAXBContext jaxb) {
    	try{
    		Unmarshaller jaxbUnmarshaller = jaxb.createUnmarshaller();
           	Contact newContact = (Contact) jaxbUnmarshaller.unmarshal(studentContactFile);
            Student newStudent = new Student(newContact);
	        newStudent.setContact(newContact);
	        return newStudent;
	        
	    	}catch (JAXBException e) {
	    		e.printStackTrace();
	    	}
    	return null;
    }
    

    /**
     * Creates a list of {@link Student} objects using the given directory of student contact files.
     *
     * @param studentDirectory the directory of student contact files to use
     * @param jaxb the JAXB context to use
     * @return a list of {@link Student} objects built using the contact files in the given directory
     */
    public static List<Student> readStudents(File studentDirectory, JAXBContext jaxb) throws JAXBException {
       	List<Student> students = new ArrayList<Student>();
		for (File newFile : studentDirectory.listFiles()) {
			Student newStudent = readStudent(newFile, jaxb);
			students.add(newStudent);
		}
			return students;
    }

    /**
     * Creates an {@link Instructor} object using the given instructorContactFile.
     * The instructorContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param instructorContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return an {@link Instructor} object built using the {@link Contact} data in the given file
     */
    public static Instructor readInstructor(File instructorContactFile, JAXBContext jaxb) {
    	try{
    		Unmarshaller jaxbUnmarshaller = jaxb.createUnmarshaller();
           	Contact newContact = (Contact) jaxbUnmarshaller.unmarshal(instructorContactFile);
           	Instructor instructor = new Instructor(newContact);
	        instructor.setContact(newContact);
	        return instructor;
	        
    	}catch (JAXBException e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    /**
     * Creates a {@link Session} object using the given rootDirectory. A {@link Session}
     * root directory is named after the location of the {@link Session}, and contains a directory named
     * after the start date of the {@link Session}. The start date directory in turn contains a directory named
     * `students`, which contains contact files for the students in the session. The start date directory
     * also contains an instructor contact file named `instructor.xml`.
     *
     * @param rootDirectory the root directory of the session data, named after the session location
     * @param jaxb the JAXB context to use
     * @return a {@link Session} object built from the data in the given directory
     */
    public static Session readSession(File rootDirectory, JAXBContext jaxb) throws JAXBException {
        Session session = new Session();
		
		session.setLocation(rootDirectory.getName());
		for (File file : rootDirectory.listFiles()) {
			session.setStartDate(file.getName());
			File instructorFile = new File(file.getAbsoluteFile() + "\\instructor.xml");
			session.setInstructor(readInstructor(instructorFile, jaxb));
			File studentsDirectory = new File(file.getAbsolutePath() + "\\students");
			session.setStudents(readStudents(studentsDirectory, jaxb));
		}
		return session;
    }

    /**
     * Writes a given session to a given XML file
     *
     * @param session the session to write to the given file
     * @param sessionFile the file to which the session is to be written
     * @param jaxb the JAXB context to use
     */
    public static void writeSession(Session session, File sessionFile, JAXBContext jaxb) {
    	try {
			Marshaller jaxbMarshaller = jaxb.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(session, sessionFile);
			jaxbMarshaller.marshal(session, System.out);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    }

    /**
     * Main Method Execution Steps:
     * 1. Configure JAXB for the classes in the com.cooksys.serialization.assignment.model package
     * 2. Read a session object from the <project-root>/input/memphis/ directory using the methods defined above
     * 3. Write the session object to the <project-root>/output/session.xml file.
     *
     * JAXB Annotations and Configuration:
     * You will have to add JAXB annotations to the classes in the com.cooksys.serialization.assignment.model package
     *
     * Check the XML files in the <project-root>/input/ directory to determine how to configure the {@link Contact}
     *  JAXB annotations
     *
     * The {@link Session} object should marshal to look like the following:
     *      <session location="..." start-date="...">
     *           <instructor>
     *               <contact>...</contact>
     *           </instructor>
     *           <students>
     *               ...
     *               <student>
     *                   <contact>...</contact>
     *               </student>
     *               ...
     *           </students>
     *      </session>
     */
    public static void main(String[] args) {
    	
    	Contact contact = new Contact();
    	contact.setFirstName("Chris");
    	contact.setLastName("Kuiper");
    	contact.setEmail("chris.kuiper@gmail.com");
    	contact.setPhoneNumber("999-999-9999");
    	
    	try {
    		File file = new File("C:\\Users\\13\\Documents\\Projects\\combined-assignments\\4-file-io-serialization\\input\\memphis");
    	JAXBContext fileSave = JAXBContext.newInstance(Contact.class);
    	Marshaller jaxbMarshaller = fileSave.createMarshaller();
    	
    	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	
    	jaxbMarshaller.marshal(contact, file);
    	jaxbMarshaller.marshal(contact, System.out);
    	
    	} catch (JAXBException e) {
    		e.printStackTrace();
    	}
    }
}
