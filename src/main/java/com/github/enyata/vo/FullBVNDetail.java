package com.github.enyata.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FullBVNDetail extends BVNDetail  {

    private String phoneNumber1;
    private String email;
    private String gender;
    private String phoneNumber2;
    private String levelOfAccount;
    private String lgaOfOrigin;
    private String maritalStatus;
    private String nationalIdentificationNumber;
    private String lgaOfResidence;
    private String nationality;
    private String nameOnCard;
    private String residentialAddress;
    private String stateOfOrigin;
    private String stateOfResidence;
    private String title;
    private String base64Image;

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    @JsonProperty("PhoneNumber1")
    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        this.email = email;
    }


    public String getGender() {
        return gender;
    }

    @JsonProperty("Gender")
    public void setGender(String gender) {
        this.gender = gender;
    }



    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    @JsonProperty("PhoneNumber2")

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }


    public String getLevelOfAccount() {
        return levelOfAccount;
    }

    @JsonProperty("LevelOfAccount")
    public void setLevelOfAccount(String levelOfAccount) {
        this.levelOfAccount = levelOfAccount;
    }

    public String getLgaOfOrigin() {
        return lgaOfOrigin;
    }

    @JsonProperty("LgaOfOrigin")
    public void setLgaOfOrigin(String lgaOfOrigin) {
        this.lgaOfOrigin = lgaOfOrigin;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    @JsonProperty("MaritalStatus")
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNationalIdentificationNumber() {
        return nationalIdentificationNumber;
    }

    @JsonProperty("NIN")
    public void setNationalIdentificationNumber(String nationalIdentificationNumber) {
        this.nationalIdentificationNumber = nationalIdentificationNumber;
    }

    public String getLgaOfResidence() {
        return lgaOfResidence;
    }

    @JsonProperty("LgaOfResidence")
    public void setLgaOfResidence(String lgaOfResidence) {
        this.lgaOfResidence = lgaOfResidence;
    }

    public String getNationality() {
        return nationality;
    }

    @JsonProperty("Nationality")
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    @JsonProperty("NameOnCard")
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    @JsonProperty("ResidentialAddress")
    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    @JsonProperty("StateOfOrigin")
    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getStateOfResidence() {
        return stateOfResidence;
    }

    @JsonProperty("StateOfResidence")
    public void setStateOfResidence(String stateOfResidence) {
        this.stateOfResidence = stateOfResidence;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty("Title")
    public void setTitle(String title) {
        this.title = title;
    }


    public String getBase64Image() {
        return base64Image;
    }

    @JsonProperty("Base64Image")
    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
