package com.github.enyata.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BVNDetail {
    private String responseCode;
    private String bvn;
    private String firstName;
    private String middleName;
    private String lastName;
    private String category;
    private String dateOfBirth;
    private String phoneNumber;
    private String registrationDate;
    private String enrollmentBank;
    private String enrollmentBranch;
    private String watchListed;
    private String bankCode;

    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("ResponseCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getBvn() {
        return bvn;
    }

    @JsonProperty("BVN")
    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("FirstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("MiddleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @JsonProperty("LastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonProperty("DateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("PhoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    @JsonProperty("RegistrationDate")
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEnrollmentBank() {
        return enrollmentBank;
    }

    @JsonProperty("EnrollmentBank")
    public void setEnrollmentBank(String enrollmentBank) {
        this.enrollmentBank = enrollmentBank;
    }

    public String getEnrollmentBranch() {
        return enrollmentBranch;
    }

    @JsonProperty("EnrollmentBranch")
    public void setEnrollmentBranch(String enrollmentBranch) {
        this.enrollmentBranch = enrollmentBranch;
    }


    public String getWatchListed() {
        return watchListed;
    }

    @JsonProperty("WatchListed")
    public void setWatchListed(String watchListed) {
        this.watchListed = watchListed;
    }

    public String getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(String category) {
        this.category = category;
    }

    public String getBankCode() {
        return bankCode;
    }

    @JsonProperty("BankCode")
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
