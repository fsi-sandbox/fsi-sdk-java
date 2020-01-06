package com.github.enyata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.enyata.bvnvalidations.BVNValidationRestService;
import com.github.enyata.config.SecurityConfiguration;
import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.vo.GetSingleBVNResponse;
import com.github.enyata.vo.ResetCredential;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    }

}
