package com.github.enyata.bvnvalidations.impl;

import com.github.enyata.bvnvalidations.BVNValidation;
import com.github.enyata.config.SecurityConfiguration;
import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.util.DateFormatter;
import com.github.enyata.util.StringUtils;
import com.github.enyata.vo.ResetCredential;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class BVNValidationImpl implements BVNValidation {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    public HttpHeaders getResetCredentialsHeaders(String sandBoxKey, String organizationCode) throws IllegalArgumentException {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isBlank(sandBoxKey) || StringUtils.isBlank(organizationCode)){
            throw new IllegalArgumentException("sandBoxKey or organizationCode value required to generate reset credentials is missing.");
        }

        headers.set("OrganisationCode", Base64.encodeBase64String(organizationCode.getBytes()));
        headers.set("Sandbox-Key", sandBoxKey);
        return headers;
    }

    public ResetCredential getResetCredentialsFromResponseHeaders(HttpHeaders headers) throws IllegalArgumentException, BadRemoteResponseException {

        if (headers == null){
            throw new IllegalArgumentException("Null headers passed as argument.");
        }
        if (!(headers.containsKey("Code") && headers.containsKey("AES_KEY") && headers.containsKey("IVKey") && headers.containsKey("PASSWORD"))){
            throw new BadRemoteResponseException("Remote responded with incomplete data");
        }

        ResetCredential resetCredential = new ResetCredential();
        resetCredential.setCode(headers.get("Code").get(0));
        resetCredential.setAesKey(headers.get("AES_KEY").get(0));
        resetCredential.setIvKey(headers.get("IVKey").get(0));
        resetCredential.setPassword(headers.get("PASSWORD").get(0));
        return resetCredential;
    }



    public HttpHeaders generateHttpAuthHeaders(String sandBoxKey, String password, String code) throws EncryptionException {


        if (StringUtils.isBlank(sandBoxKey) || StringUtils.isBlank(password) || StringUtils.isBlank(code)){
            throw new IllegalArgumentException("sandBoxKey, password or code value was not passed as argument.");
        }

        final String signatureMethodHeader = "SHA256";
        final String username = code;
        final String requestDate = DateFormatter.formatDate(new Date());
        // Concatenate all three strings into one string
        final String signatureString = username + requestDate + password;

        // Concatenate the strings in the format username:password
        final String authString = username + ':' + password;

        // Encode it to Base64 and save it for it will be used later
        final String authHeader = Base64.encodeBase64String(authString.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authHeader);
        httpHeaders.set("SIGNATURE", securityConfiguration.encryptWithoutIV(signatureString));
        httpHeaders.set("OrganisationCode", Base64.encodeBase64String(username.getBytes()));
        httpHeaders.set("SIGNATURE_METH", signatureMethodHeader);
        httpHeaders.set("Sandbox-Key", sandBoxKey);

        return httpHeaders;

    }

    /**
     * This method generates all the required headers for calls to all subsequent available BVN endpoints.
     *
     * @return HttpHeaders
     * @throws EncryptionException
     */
    @Override
    public HttpHeaders generateHttpAuthHeaders(String sandBoxKey) throws EncryptionException {

        final String signatureMethodHeader = "SHA256";
        final String username = securityConfiguration.getResetCredential().getCode();
        final String requestDate = DateFormatter.formatDate(new Date());
        // Concatenate all three strings into one string
        final String signatureString = username + requestDate + securityConfiguration.getResetCredential().getPassword();

        // Concatenate the strings in the format username:password
        final String authString = username + ':' + securityConfiguration.getResetCredential().getPassword();

        // Encode it to Base64 and save it for it will be used later
        final String authHeader = Base64.encodeBase64String(authString.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authHeader);
        httpHeaders.set("SIGNATURE", securityConfiguration.encryptWithoutIV(signatureString));
        httpHeaders.set("OrganisationCode", Base64.encodeBase64String(username.getBytes()));
        httpHeaders.set("SIGNATURE_METH", signatureMethodHeader);
        httpHeaders.set("Sandbox-Key", sandBoxKey);

        return httpHeaders;
    }
}
