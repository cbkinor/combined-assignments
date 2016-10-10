package com.cooksys.serialization.assignment.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Session {
    private String location;
    private String startDate;
    private Instructor instructor;
    private List<Student> students;

    public String getLocation() {
        return location;
    }

    @XmlAttribute(name="location")
    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    @XmlAttribute(name="start-date")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    @XmlElement(name="instructor")
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Student> getStudents() {
        return students;
    }

    @XmlElement(name="student")
    @XmlElementWrapper(name="students")
    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
