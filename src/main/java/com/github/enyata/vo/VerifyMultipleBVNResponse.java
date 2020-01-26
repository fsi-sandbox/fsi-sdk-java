package com.github.enyata.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyMultipleBVNResponse {
    private String message;
    private Data data;

    public class Data {
        private String responseCode;
        private List<BVNDetail> validationResponses;

        public String getResponseCode() {
            return responseCode;
        }

        @JsonProperty("ResponseCode")
        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        @JsonProperty("ValidationResponses")
        public List<BVNDetail> getValidationResponses() {
            return Collections.unmodifiableList(validationResponses);
        }

        public void setValidationResponses(List<BVNDetail> validationResponses) {
            this.validationResponses = validationResponses;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
