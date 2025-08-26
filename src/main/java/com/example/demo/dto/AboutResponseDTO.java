package com.example.demo.dto;

public class AboutResponseDTO {
    private String name;
    private String address;
    private String aboutDescription;
    private String contact;

    public AboutResponseDTO() {}

    public AboutResponseDTO(String name, String address, String aboutDescription, String contact) {
        this.name = name;
        this.address = address;
        this.aboutDescription = aboutDescription;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAboutDescription() {
        return aboutDescription;
    }

    public void setAboutDescription(String aboutDescription) {
        this.aboutDescription = aboutDescription;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
