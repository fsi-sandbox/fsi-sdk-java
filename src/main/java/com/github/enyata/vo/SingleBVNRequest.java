package com.github.enyata.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleBVNRequest {


    private String bankVerificationNumber;

    @JsonProperty("BVN")
    public String getBankVerificationNumber() {
        return bankVerificationNumber;
    }

    public void setBankVerificationNumber(String bvn) {
        this.bankVerificationNumber = bvn;
    }
}
