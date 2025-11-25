package com.ines.safetynet1.service.dto;

import java.util.List;

public class ChildAlertDto {
    private String firstName;
    private String lastName;
    private int age;
    private List<String> otherHouseholdMembers; // noms des autres personnes à la même adresse

    public ChildAlertDto() {
    }


    // Getters et Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public List<String> getOtherHouseholdMembers() { return otherHouseholdMembers; }
    public void setOtherHouseholdMembers(List<String> otherHouseholdMembers) { this.otherHouseholdMembers = otherHouseholdMembers; }
}

