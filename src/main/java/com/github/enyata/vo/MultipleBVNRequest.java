package com.github.enyata.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MultipleBVNRequest {

    private String bankVerificationNumbers;

    @JsonProperty("BVNS")
    public String getBankVerificationNumbers() {
        return bankVerificationNumbers;
    }

    public void setBankVerificationNumbers(String bankVerificationNumbers) {
        this.bankVerificationNumbers = bankVerificationNumbers;
    }
}
