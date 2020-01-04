package com.fsi.sandbox;


import com.fsi.sandbox.bvnValidations.BVNValidationImpl;
import com.fsi.sandbox.exceptions.BadRemoteResponseException;
import com.fsi.sandbox.vo.ResetCredential;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
@ActiveProfiles({"test"})
public class BVNValidationTest {

    @Autowired
    private BVNValidationImpl bvnValidationService;

    @Test(expected = IllegalArgumentException.class)
    public void testResetHeadersEmptySecretKey(){
        bvnValidationService.getResetCredentialsHeaders("", "testorg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetHeadersUntrimmedSecretKey(){
        bvnValidationService.getResetCredentialsHeaders(null, "testorg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetHeadersEmptyOrganizationCode(){
        bvnValidationService.getResetCredentialsHeaders("test123", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetHeadersUntrimmedOrganizationCode(){
        bvnValidationService.getResetCredentialsHeaders("test123", null);
    }

    @Test
    public void testResetHeadersContainsRequiredHeaders(){
        HttpHeaders headers = bvnValidationService.getResetCredentialsHeaders("test123", "test1234");
        Assert.assertTrue(headers.containsKey("Sandbox-Key"));
        Assert.assertTrue(headers.containsKey("OrganisationCode"));
        Assert.assertEquals("test123", headers.get("Sandbox-Key").get(0));
        Assert.assertEquals( Base64.encodeBase64String("test1234".getBytes()), headers.get("OrganisationCode").get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetResetCredentialNullHeaderResponse() throws BadRemoteResponseException {
        bvnValidationService.getResetCredentialsFromResponseHeaders(null);
    }

    @Test(expected = BadRemoteResponseException.class)
    public void testGetResetCredentialNoAES_KEY_HeaderResponse() throws BadRemoteResponseException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Code", "code");
        //headers.set("AES_KEY", "AES_KEY");
        headers.set("IVKey", "IVKey");
        headers.set("PASSWORD", "PASSWORD");
        try {
            bvnValidationService.getResetCredentialsFromResponseHeaders(headers);
        }catch (BadRemoteResponseException ex){
            Assert.assertEquals("Remote responded with incomplete data", ex.getMessage());
            throw ex;
        }

    }

    @Test(expected = BadRemoteResponseException.class)
    public void testGetResetCredentialNoIVKey_HeaderResponse() throws BadRemoteResponseException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Code", "code");
        headers.set("AES_KEY", "AES_KEY");
        //headers.set("IVKey", "IVKey");
        headers.set("PASSWORD", "PASSWORD");
        try {
            bvnValidationService.getResetCredentialsFromResponseHeaders(headers);
        }catch (BadRemoteResponseException ex){
            Assert.assertEquals("Remote responded with incomplete data", ex.getMessage());
            throw ex;
        }

    }

    @Test(expected = BadRemoteResponseException.class)
    public void testGetResetCredentialNoPASSWORD_HeaderResponse() throws BadRemoteResponseException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Code", "code");
        headers.set("AES_KEY", "AES_KEY");
        headers.set("IVKey", "IVKey");
        //headers.set("PASSWORD", "PASSWORD");
        try {
            bvnValidationService.getResetCredentialsFromResponseHeaders(headers);
        }catch (BadRemoteResponseException ex){
            Assert.assertEquals("Remote responded with incomplete data", ex.getMessage());
            throw ex;
        }

    }

    @Test(expected = BadRemoteResponseException.class)
    public void testGetResetCredentialNoCode_HeaderResponse() throws BadRemoteResponseException {
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Code", "code");
        headers.set("AES_KEY", "AES_KEY");
        headers.set("IVKey", "IVKey");
        headers.set("PASSWORD", "PASSWORD");
        try {
            bvnValidationService.getResetCredentialsFromResponseHeaders(headers);
        }catch (BadRemoteResponseException ex){
            Assert.assertEquals("Remote responded with incomplete data", ex.getMessage());
            throw ex;
        }

    }

    @Test
    public void testGetResetCredentialSuccess() throws BadRemoteResponseException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Code", "code");
        headers.set("AES_KEY", "AES_KEY");
        headers.set("IVKey", "IVKey");
        headers.set("PASSWORD", "PASSWORD");

        ResetCredential credential = bvnValidationService.getResetCredentialsFromResponseHeaders(headers);

        Assert.assertEquals("AES_KEY", credential.getAesKey());
        Assert.assertEquals("code", credential.getCode());
        Assert.assertEquals("IVKey", credential.getIvKey());
        Assert.assertEquals("PASSWORD", credential.getPassword());



    }
}