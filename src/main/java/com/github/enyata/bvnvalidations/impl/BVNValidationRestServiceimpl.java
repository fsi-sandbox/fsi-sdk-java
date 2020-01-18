package com.github.enyata.bvnvalidations.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.enyata.bvnvalidations.BVNValidation;
import com.github.enyata.bvnvalidations.BVNValidationRestService;
import com.github.enyata.config.SecurityConfiguration;
import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.util.StringUtils;
import com.github.enyata.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BVNValidationRestServiceimpl implements BVNValidationRestService {

    @Autowired
    private BVNValidation bvnValidation;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Value("${fsi.sandbox.verify.singlebvn.url}")
    private String verifySingleBVNUrl;

    @Value("${fsi.sandbox.reset.url}")
    private String resetUrl;

    @Value("${fsi.sandbox.get.singlebvn.url}")
    private String getSingleBVNUrl;

    @Value("${fsi.sandbox.is.bvn.watchlisted.url}")
    private String isBVNWatchlistedUrl;

    @Value("${fsi.sandbox.get.multiplebvn.url}")
    private String getMultipleBVNUrl;

    @Value("${fsi.sandbox.verify.multiplebvn.url}")
    private String verifyMultipleBVNUrl;
//
//    @Value("${fsi.sandbox.validate.record.url}")
//    private String validateBVNRecordUrl;
//
//    @Value("${fsi.sandbox.validate.records.url}")
//    private String validateBVNRecordsUrl;

    public static Logger LOGGER = LoggerFactory.getLogger(BVNValidationRestServiceimpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResetCredential resetCredentials(String sandboxKey, String organizationCode) throws BadRemoteResponseException, EncryptionException {

        HttpHeaders headers = bvnValidation.getResetCredentialsHeaders(sandboxKey, organizationCode);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(resetUrl, HttpMethod.POST, entity, String.class);
        HttpHeaders responseHeaders = responseEntity.getHeaders();

        return bvnValidation.getResetCredentialsFromResponseHeaders(responseHeaders);

    }

    public GetSingleBVNResponse getSingleBVN(String bvn, String sandboxKey,  String code) throws EncryptionException, JsonProcessingException {

        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, securityConfiguration.getResetCredential().getPassword(), code);
        SingleBVNRequest bvnRequest = new SingleBVNRequest();
        bvnRequest.setBankVerificationNumber(bvn);
        String bvnRequestString = objectMapper.writeValueAsString(bvnRequest);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity =  restTemplate.exchange(getSingleBVNUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();
        String decrypted = securityConfiguration.decrypt(responseBody);
        GetSingleBVNResponse response = objectMapper.readValue(decrypted, GetSingleBVNResponse.class);
        return response;
    }

    public GetSingleBVNResponse getSingleBVN(String bvn, String sandboxKey,  String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException {


        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, password, code);
        SingleBVNRequest bvnRequest = new SingleBVNRequest();
        bvnRequest.setBankVerificationNumber(bvn);
        String bvnRequestString = objectMapper.writeValueAsString(bvnRequest);

        //use this if you have called the securityConfiguration.setEncryptionCiphers(credential);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString, encryptionKey, ivKey);

        //else you can call the securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity =  restTemplate.exchange(getSingleBVNUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();

        //else you can call the securityConfiguration.decrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        String decrypted = securityConfiguration.decrypt(responseBody, encryptionKey, ivKey);
        GetSingleBVNResponse response = objectMapper.readValue(decrypted, GetSingleBVNResponse.class);
        return response;
    }

    public GetMultipleBVNResponse getMultipleBVN(List<String> bankVerificationNumbers, String sandboxKey, String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException {


        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, password, code);
        MultipleBVNRequest request = new MultipleBVNRequest();

        String result = bankVerificationNumbers
                .stream()
                .reduce("", (partialString, element) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(partialString);
                    if (!StringUtils.isBlank(partialString))
                        stringBuilder.append(", ");

                    stringBuilder.append(element);
                    return stringBuilder.toString();
                });
        request.setBankVerificationNumbers(result);
        String bvnRequestString = objectMapper.writeValueAsString(request);

        //use this if you have called the securityConfiguration.setEncryptionCiphers(credential);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString, encryptionKey, ivKey);

        //else you can call the securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity =  restTemplate.exchange(getMultipleBVNUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();

        //else you can call the securityConfiguration.decrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        String decrypted = securityConfiguration.decrypt(responseBody, encryptionKey, ivKey);
        GetMultipleBVNResponse response = objectMapper.readValue(decrypted, GetMultipleBVNResponse.class);
        return response;
    }

    public GetMultipleBVNResponse getMultipleBVN(List<String> bankVerificationNumbers, String sandboxKey, String code) throws EncryptionException, JsonProcessingException {


        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, securityConfiguration.getResetCredential().getPassword(), code);
        MultipleBVNRequest request = new MultipleBVNRequest();

        String result = bankVerificationNumbers
                .stream()
                .reduce("", (partialString, element) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(partialString);
                    if (!StringUtils.isBlank(partialString))
                        stringBuilder.append(", ");

                    stringBuilder.append(element);
                    return stringBuilder.toString();
                });
        request.setBankVerificationNumbers(result);
        String bvnRequestString = objectMapper.writeValueAsString(request);

        //use this if you have called the securityConfiguration.setEncryptionCiphers(credential);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);

        //else you can call the securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity =  restTemplate.exchange(getMultipleBVNUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();

        //else you can call the securityConfiguration.decrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        String decrypted = securityConfiguration.decrypt(responseBody);
        GetMultipleBVNResponse response = objectMapper.readValue(decrypted, GetMultipleBVNResponse.class);
        return response;
    }

    @Override
    public IsBVNWatchlistedResponse isBVNWatchlisted(String bvn, String sandboxKey, String code) throws EncryptionException, JsonProcessingException {
        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, securityConfiguration.getResetCredential().getPassword(), code);
        SingleBVNRequest bvnRequest = new SingleBVNRequest();
        bvnRequest.setBankVerificationNumber(bvn);
        String bvnRequestString = objectMapper.writeValueAsString(bvnRequest);

        //use this if you have called the securityConfiguration.setEncryptionCiphers(credential);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);

        //else you can call the securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);

        ResponseEntity<String> responseEntity =  restTemplate.exchange(isBVNWatchlistedUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();


        //else you can call the securityConfiguration.decrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        String decrypted = securityConfiguration.decrypt(responseBody);
        IsBVNWatchlistedResponse response = objectMapper.readValue(decrypted, IsBVNWatchlistedResponse.class);
        return response;
    }

    @Override
    public IsBVNWatchlistedResponse isBVNWatchlisted(String bvn, String sandboxKey, String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException {
        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, password, code);
        SingleBVNRequest bvnRequest = new SingleBVNRequest();
        bvnRequest.setBankVerificationNumber(bvn);
        String bvnRequestString = objectMapper.writeValueAsString(bvnRequest);

        //use this if you have called the securityConfiguration.setEncryptionCiphers(credential);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString, encryptionKey, ivKey);

        //else you can call the securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity =  restTemplate.exchange(isBVNWatchlistedUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();

        //else you can call the securityConfiguration.decrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        String decrypted = securityConfiguration.decrypt(responseBody, encryptionKey, ivKey);
        IsBVNWatchlistedResponse response = objectMapper.readValue(decrypted, IsBVNWatchlistedResponse.class);
        return response;
    }

    public VerifySingleBVNResponse verifySingleBVN(String bvn, String sandboxKey,  String code) throws EncryptionException, JsonProcessingException {

        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, securityConfiguration.getResetCredential().getPassword(), code);
        SingleBVNRequest bvnRequest = new SingleBVNRequest();
        bvnRequest.setBankVerificationNumber(bvn);
        String bvnRequestString = objectMapper.writeValueAsString(bvnRequest);

        //use this if you have called the securityConfiguration.setEncryptionCiphers(credential);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);

        //else you can call the securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);

        ResponseEntity<String> responseEntity =  restTemplate.exchange(isBVNWatchlistedUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();


        //else you can call the securityConfiguration.decrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        String decrypted = securityConfiguration.decrypt(responseBody);
        VerifySingleBVNResponse response = objectMapper.readValue(decrypted, VerifySingleBVNResponse.class);
        return response;
    }

    public VerifySingleBVNResponse verifySingleBVN(String bvn, String sandboxKey,  String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException {

        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, password, code);
        SingleBVNRequest bvnRequest = new SingleBVNRequest();
        bvnRequest.setBankVerificationNumber(bvn);
        String bvnRequestString = objectMapper.writeValueAsString(bvnRequest);

        //use this if you have called the securityConfiguration.setEncryptionCiphers(credential);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString, encryptionKey, ivKey);

        //else you can call the securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity =  restTemplate.exchange(verifySingleBVNUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();

        //else you can call the securityConfiguration.decrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        String decrypted = securityConfiguration.decrypt(responseBody, encryptionKey, ivKey);
        VerifySingleBVNResponse response = objectMapper.readValue(decrypted, VerifySingleBVNResponse.class);
        return response;
    }

    public VerifyMultipleBVNResponse verifyMultipleBVN(List<String> bankVerificationNumbers, String sandboxKey, String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException {


        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, password, code);
        MultipleBVNRequest request = new MultipleBVNRequest();

        String result = bankVerificationNumbers
                .stream()
                .reduce("", (partialString, element) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(partialString);
                    if (!StringUtils.isBlank(partialString))
                        stringBuilder.append(", ");

                    stringBuilder.append(element);
                    return stringBuilder.toString();
                });
        request.setBankVerificationNumbers(result);
        String bvnRequestString = objectMapper.writeValueAsString(request);

        //use this if you have called the securityConfiguration.setEncryptionCiphers(credential);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString, encryptionKey, ivKey);

        //else you can call the securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity =  restTemplate.exchange(verifyMultipleBVNUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();

        //else you can call the securityConfiguration.decrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey()) method
        String decrypted = securityConfiguration.decrypt(responseBody, encryptionKey, ivKey);
        VerifyMultipleBVNResponse response = objectMapper.readValue(decrypted, VerifyMultipleBVNResponse.class);
        return response;
    }

    public VerifyMultipleBVNResponse verifySingleBVN(List<String> bankVerificationNumbers, String sandboxKey, String code) throws EncryptionException, JsonProcessingException {

        HttpHeaders headers = bvnValidation.generateHttpAuthHeaders(sandboxKey, securityConfiguration.getResetCredential().getPassword(), code);
        MultipleBVNRequest request = new MultipleBVNRequest();

        String result = bankVerificationNumbers
                .stream()
                .reduce("", (partialString, element) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(partialString);
                    if (!StringUtils.isBlank(partialString))
                        stringBuilder.append(", ");

                    stringBuilder.append(element);
                    return stringBuilder.toString();
                });
        request.setBankVerificationNumbers(result);
        String bvnRequestString = objectMapper.writeValueAsString(request);
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);

        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity =  restTemplate.exchange(verifyMultipleBVNUrl, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();

        String decrypted = securityConfiguration.decrypt(responseBody);
        VerifyMultipleBVNResponse response = objectMapper.readValue(decrypted, VerifyMultipleBVNResponse.class);
        return response;
    }
}