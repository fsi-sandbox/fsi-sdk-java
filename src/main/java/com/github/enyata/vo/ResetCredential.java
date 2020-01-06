package com.github.enyata.vo;


import org.springframework.context.annotation.Configuration;

@Configuration
public class ResetCredential {
    private String aesKey;
    private String ivKey;
    private String password;
    private String code;

    private String sandboxKey;

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getIvKey() {
        return ivKey;
    }

    public void setIvKey(String ivKey) {
        this.ivKey = ivKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSandboxKey() {
        return sandboxKey;
    }

    public void setSandboxKey(String sandboxKey) {
        this.sandboxKey = sandboxKey;
    }
}
