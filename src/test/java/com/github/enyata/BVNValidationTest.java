package com.github.enyata;


import com.github.enyata.bvnvalidations.BVNValidation;
import com.github.enyata.config.SecurityConfiguration;
import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.vo.ResetCredential;
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
    private BVNValidation bvnValidationService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

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

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateAuthHeadersNoSandBoxKey() throws  EncryptionException {
         bvnValidationService.generateHttpAuthHeaders("", "123", "123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateAuthHeadersNoPassword() throws  EncryptionException {
        bvnValidationService.generateHttpAuthHeaders("123", "", "123");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateAuthHeadersNoCode() throws  EncryptionException {
        bvnValidationService.generateHttpAuthHeaders("1223", "123", null);
    }

    @Test
    public void testGenerateAuthHeadersSuccess() throws  EncryptionException {
        HttpHeaders headers =  bvnValidationService.generateHttpAuthHeaders("1223", "123", "123");
        Assert.assertEquals("MTIz", headers.get("OrganisationCode").get(0));
        Assert.assertEquals("1223", headers.get("Sandbox-Key").get(0));
        Assert.assertEquals("MTIzOjEyMw==", headers.get("Authorization").get(0));

        Assert.assertEquals("SHA256", headers.get("SIGNATURE_METH").get(0));
        Assert.assertEquals("application/json", headers.get("Content-Type").get(0));
        Assert.assertEquals("application/json", headers.get("Accept").get(0));



        ResetCredential credential = new ResetCredential();
        credential.setPassword("123");
        credential.setCode("123");
        credential.setAesKey("teppp09090909pst");
        credential.setIvKey("test9000oplkoilo");

        securityConfiguration.setResetCredential(credential);
        HttpHeaders headers2 =  bvnValidationService.generateHttpAuthHeaders("1223");
        Assert.assertEquals("MTIz", headers2.get("OrganisationCode").get(0));
        Assert.assertEquals("1223", headers2.get("Sandbox-Key").get(0));
        Assert.assertEquals("MTIzOjEyMw==", headers2.get("Authorization").get(0));

        Assert.assertEquals("SHA256", headers2.get("SIGNATURE_METH").get(0));
        Assert.assertEquals("application/json", headers2.get("Content-Type").get(0));
        Assert.assertEquals("application/json", headers2.get("Accept").get(0));

    }
}