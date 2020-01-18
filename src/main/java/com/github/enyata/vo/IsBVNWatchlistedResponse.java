package com.github.enyata.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IsBVNWatchlistedResponse {

    private String message;
    private Data data;

    public class Data{
        private String responseCode;
        private String bankVerificationNumber;
        private String bankCode;
        private String WatchListed;
        private String Category;

        public String getResponseCode() {
            return responseCode;
        }

        @JsonProperty("ResponseCode")
        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getBankVerificationNumber() {
            return bankVerificationNumber;
        }

        @JsonProperty("BVN")
        public void setBankVerificationNumber(String bankVerificationNumber) {
            this.bankVerificationNumber = bankVerificationNumber;
        }

        public String getBankCode() {
            return bankCode;
        }

        @JsonProperty("BankCode")
        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getWatchListed() {
            return WatchListed;
        }

        @JsonProperty("WatchListed")
        public void setWatchListed(String watchListed) {
            WatchListed = watchListed;
        }

        public String getCategory() {
            return Category;
        }

        @JsonProperty("Category")
        public void setCategory(String category) {
            Category = category;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
