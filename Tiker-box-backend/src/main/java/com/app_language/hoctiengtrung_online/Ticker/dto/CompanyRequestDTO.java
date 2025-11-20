package com.app_language.hoctiengtrung_online.Ticker.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CompanyRequestDTO {

    @NotBlank(message = "Company code is required")
    @Size(min = 2, max = 20, message = "Company code must be between 2 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Company code can only contain uppercase letters, numbers and underscores")
    private String code;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String name;

    private String description;

    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number should be between 10 and 15 digits")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String website;
    private String contactPerson;
    private String taxCode;

    // Constructors
    public CompanyRequestDTO() {}

    public CompanyRequestDTO(String code, String name, String description, String address,
                             String phone, String email, String password) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    @Override
    public String toString() {
        return "CompanyRequestDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", website='" + website + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", taxCode='" + taxCode + '\'' +
                '}';
    }
}