package com.ines.safetynet1.service.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // ignore les champs null
public class PersonInfoDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone; // <-- numéro de téléphone
    private String age;
    private String[] medications;
    private String[] allergies;

    // Getters et setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String[] getMedications() { return medications; }
    public void setMedications(String[] medications) { this.medications = medications; }

    public String[] getAllergies() { return allergies; }
    public void setAllergies(String[] allergies) { this.allergies = allergies; }
}
