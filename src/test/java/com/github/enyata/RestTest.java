package com.github.enyata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.enyata.bvnvalidations.BVNValidationRestService;
import com.github.enyata.config.SecurityConfiguration;
import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.vo.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
@ActiveProfiles({"test"})
public class RestTest {

    @Autowired
    public BVNValidationRestService restService;

    @Value("${sandbox.key}")
    private String sandBoxKey;

    @Value("${sandbox.organization.code}")
    private String sandBoxOrganizationCode;

    @Autowired
    private SecurityConfiguration securityConfiguration;
    @Test
    public void testReset() throws BadRemoteResponseException, EncryptionException, JsonProcessingException {

        ResetCredential credential =  restService.resetCredentials(sandBoxKey, sandBoxOrganizationCode);
        GetSingleBVNResponse response =  restService.getSingleBVN("12345678901", sandBoxKey, credential.getCode(), credential.getPassword(), credential.getAesKey(), credential.getIvKey());
        Assert.assertNotNull(response.getMessage());
        Assert.assertNotNull(response.getData().getBvn());
        Assert.assertNotNull(response.getData().getDateOfBirth());
        Assert.assertNotNull(response.getData().getEnrollmentBank());
        Assert.assertNotNull(response.getData().getFirstName());
        Assert.assertNotNull(response.getData().getLastName());
        Assert.assertNotNull(response.getData().getEnrollmentBranch());
        Assert.assertNotNull(response.getData().getMiddleName());
        Assert.assertNotNull(response.getData().getPhoneNumber1());
        Assert.assertNotNull(response.getData().getPhoneNumber2());
        Assert.assertNotNull(response.getData().getRegistrationDate());
        Assert.assertNotNull(response.getData().getResponseCode());
        Assert.assertNotNull(response.getData().getWatchListed());
    }

    @Test
    public void testGetSingleBVN() throws BadRemoteResponseException, EncryptionException, JsonProcessingException {

        ResetCredential credential =  restService.resetCredentials(sandBoxKey, sandBoxOrganizationCode);
        //very important you call this method, so it caches the ciphers used for encryption and decryption, else encryption will fail
        //you can as well publish this credential using Kafka Topics to all running instance of your application, then have a consumer which calls this method
        securityConfiguration.setResetCredential(credential);

        GetSingleBVNResponse response = restService.getSingleBVN("12345678901",sandBoxKey, credential.getCode());
        Assert.assertNotNull(response.getMessage());
        Assert.assertNotNull(response.getData().getBvn());
        Assert.assertNotNull(response.getData().getDateOfBirth());
        Assert.assertNotNull(response.getData().getEnrollmentBank());
        Assert.assertNotNull(response.getData().getFirstName());
        Assert.assertNotNull(response.getData().getLastName());
        Assert.assertNotNull(response.getData().getEnrollmentBranch());
        Assert.assertNotNull(response.getData().getMiddleName());
        Assert.assertNotNull(response.getData().getPhoneNumber1());
        Assert.assertNotNull(response.getData().getPhoneNumber2());
        Assert.assertNotNull(response.getData().getRegistrationDate());
        Assert.assertNotNull(response.getData().getResponseCode());
        Assert.assertNotNull(response.getData().getWatchListed());


        VerifySingleBVNResponse response2 = restService.verifySingleBVN("12345678901",sandBoxKey, credential.getCode());
        Assert.assertNotNull(response2.getMessage());
        Assert.assertNotNull(response2.getData().getBvn());
        Assert.assertNotNull(response2.getData().getCategory());
        Assert.assertNotNull(response2.getData().getBankCode());
        Assert.assertNotNull(response2.getData().getResponseCode());
        Assert.assertNotNull(response2.getData().getWatchListed());
    }

    @Test
    public void testVerifySingleBVN() throws BadRemoteResponseException, EncryptionException, JsonProcessingException {

        ResetCredential credential =  restService.resetCredentials(sandBoxKey, sandBoxOrganizationCode);
        VerifySingleBVNResponse response =  restService.verifySingleBVN("12345678901", sandBoxKey, credential.getCode(), credential.getPassword(), credential.getAesKey(), credential.getIvKey());
        Assert.assertNotNull(response.getMessage());
        Assert.assertNotNull(response.getData().getBvn());
        Assert.assertNotNull(response.getData().getDateOfBirth());
        Assert.assertNotNull(response.getData().getEnrollmentBank());
        Assert.assertNotNull(response.getData().getFirstName());
        Assert.assertNotNull(response.getData().getLastName());
        Assert.assertNotNull(response.getData().getEnrollmentBranch());
        Assert.assertNotNull(response.getData().getMiddleName());
        Assert.assertNotNull(response.getData().getPhoneNumber());
        Assert.assertNotNull(response.getData().getRegistrationDate());
        Assert.assertNotNull(response.getData().getResponseCode());
        Assert.assertNotNull(response.getData().getWatchListed());
    }

    @Test
    public void testIsBVNWatchListed() throws BadRemoteResponseException, EncryptionException, JsonProcessingException {
        ResetCredential credential =  restService.resetCredentials(sandBoxKey, sandBoxOrganizationCode);
        IsBVNWatchlistedResponse response =  restService.isBVNWatchlisted("12345678901", sandBoxKey, credential.getCode(), credential.getPassword(), credential.getAesKey(), credential.getIvKey());
        Assert.assertNotNull(response.getMessage());
        Assert.assertNotNull(response.getData().getCategory());
        Assert.assertNotNull(response.getData().getBankCode());
        Assert.assertNotNull(response.getData().getBankVerificationNumber());
        Assert.assertNotNull(response.getData().getResponseCode());
        Assert.assertNotNull(response.getData().getWatchListed());
    }

    @Test
    public void testGetMultipleBVN() throws BadRemoteResponseException, EncryptionException, JsonProcessingException {
        ResetCredential credential =  restService.resetCredentials(sandBoxKey, sandBoxOrganizationCode);
        GetMultipleBVNResponse response =  restService.getMultipleBVN(Arrays.asList("12345678901", "12345678902", "12345678903"), sandBoxKey, credential.getCode(), credential.getPassword(), credential.getAesKey(), credential.getIvKey());
        Assert.assertNotNull(response.getMessage());
        Assert.assertNotNull(response.getData());
        Assert.assertNotNull(response.getData().getResponseCode());
        Assert.assertEquals(response.getData().getValidationResponses().size(), 3);
        response.getData().getValidationResponses().forEach(s->{
            Assert.assertNotNull(response.getMessage());
            Assert.assertNotNull(s.getBvn());
            Assert.assertNotNull(s.getDateOfBirth());
            Assert.assertNotNull(s.getEnrollmentBank());
            Assert.assertNotNull(s.getFirstName());
            Assert.assertNotNull(s.getLastName());
            Assert.assertNotNull(s.getEnrollmentBranch());
            Assert.assertNotNull(s.getMiddleName());
            Assert.assertNotNull(s.getPhoneNumber1());
            Assert.assertNotNull(s.getPhoneNumber2());
            Assert.assertNotNull(s.getRegistrationDate());
            Assert.assertNotNull(s.getResponseCode());
            Assert.assertNotNull(s.getWatchListed());
        });
    }

    @Test
    public void testGetMultipleBVN2() throws BadRemoteResponseException, EncryptionException, JsonProcessingException {
        ResetCredential credential =  restService.resetCredentials(sandBoxKey, sandBoxOrganizationCode);
        securityConfiguration.setResetCredential(credential);
        GetMultipleBVNResponse response =  restService.getMultipleBVN(Arrays.asList("12345678901", "12345678902", "12345678903"), sandBoxKey, sandBoxOrganizationCode);
        Assert.assertNotNull(response.getMessage());
        Assert.assertNotNull(response.getData());
        Assert.assertNotNull(response.getData().getResponseCode());
        Assert.assertEquals(response.getData().getValidationResponses().size(), 3);
        response.getData().getValidationResponses().forEach(s->{
            Assert.assertNotNull(response.getMessage());
            Assert.assertNotNull(s.getBvn());
            Assert.assertNotNull(s.getDateOfBirth());
            Assert.assertNotNull(s.getEnrollmentBank());
            Assert.assertNotNull(s.getFirstName());
            Assert.assertNotNull(s.getLastName());
            Assert.assertNotNull(s.getEnrollmentBranch());
            Assert.assertNotNull(s.getMiddleName());
            Assert.assertNotNull(s.getPhoneNumber1());
            Assert.assertNotNull(s.getPhoneNumber2());
            Assert.assertNotNull(s.getRegistrationDate());
            Assert.assertNotNull(s.getResponseCode());
            Assert.assertNotNull(s.getWatchListed());
        });
    }
}
