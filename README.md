# fsi-nibss-interface
Spring boot library for BVN validation as exposed by NIBSS on the FSI Sandbox. 

## To use the library, you need to set up the following
1. Set up maven for your project on your system.
2. Add the following block to your settings.xml file in your .m2 directory in order to download the fsi-nibss-interface from sonatype's repository.  (.m2 directory is usually found in the user’s home directory)
```
<profiles>
    <profile>
        <id>default</id>
        </profile>
		<profile>
     <id>allow-snapshots</id>
        <activation><activeByDefault>true</activeByDefault></activation>
     <repositories>
       <repository>
         <id>snapshots-repo</id>
         <url>https://oss.sonatype.org/content/repositories/snapshots</url>
         <releases><enabled>false</enabled></releases>
         <snapshots><enabled>true</enabled></snapshots>
       </repository>
     </repositories>
   </profile>
</profiles>
```

3. In your application's pom file, add this snippet in the distributionManagement tag
```
<snapshotRepository>
   <id>ossrh</id>
   <name>My Snapshot Repository</name>
   <url>https://oss.sonatype.org/content/repositories/snapshots</url>
   <uniqueVersion>false</uniqueVersion>
</snapshotRepository>
```

4. Add as a dependency to your project
```
<dependency>
   <groupId>com.github.enyata</groupId>
   <artifactId>fsi-nibss-interface</artifactId>
   <version>1.0.1-SNAPSHOT</version>
</dependency>
```


5. add the following properties in your application.properties file to make it work
```
   fsi.sandbox.base.url=https://sandboxapi.fsi.ng    
   Note that the base url value will change when moving to production. 
   fsi.sandbox.reset.url=${fsi.sandbox.base.url}/nibss/bvnr/Reset
   fsi.sandbox.get.singlebvn.url=${fsi.sandbox.base.url}/nibss/bvnr/GetSingleBVN
   fsi.sandbox.verify.singlebvn.url=${fsi.sandbox.base.url}/nibss/bvnr/VerifySingleBVN
   fsi.sandbox.is.bvn.watchlisted.url=${fsi.sandbox.base.url}/nibss/bvnr/IsBVNWatchlisted
   fsi.sandbox.get.multiplebvn.url=${fsi.sandbox.base.url}/nibss/bvnr/GetMultipleBVN
   fsi.sandbox.verify.multiplebvn.url=${fsi.sandbox.base.url}/nibss/bvnr/VerifyMultipleBVN

 ```

## Sample Code using the library
```java

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.enyata.bvnvalidations.BVNValidation;
import com.github.enyata.config.SecurityConfiguration;
import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.vo.GetSingleBVNResponse;
import com.github.enyata.vo.ResetCredential;
import com.github.enyata.vo.SingleBVNRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


public class TestApp {
    public static Logger LOGGER = LoggerFactory.getLogger(TestCall.class);

    @Autowired
    private BVNValidation bvnValidationservice;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private RestTemplate restTemplate;

    private static ObjectMapper objectMapper = new ObjectMapper();



    public void testMethod() throws BadRemoteResponseException, IOException, EncryptionException {
        //this makes a call to the reset method
        String sandBoxKey = "*******";  //replace with your key
	String code = "11111";
        String resetUrl = "https://sandboxapi.fsi.ng/nibss/bvnr/Reset";
        String getsinglebvnUrl = "https://sandboxapi.fsi.ng//nibss/bvnr/GetSingleBVN";
        HttpHeaders headers =  bvnValidationservice.getResetCredentialsHeaders(sandBoxKey, code);
        HttpEntity entity = new HttpEntity(headers);

        HttpEntity<String> response = restTemplate.exchange(resetUrl, HttpMethod.POST, entity, String.class);
        HttpHeaders responseHeader  = response.getHeaders();
        //all the necessary credentials for calling subsequent endpoints are contained in the ResetCredential Object
        ResetCredential credential =  bvnValidationservice.getResetCredentialsFromResponseHeaders(responseHeader);

        LOGGER.info("Encryption key is {}", credential.getAesKey());  //Required for encryption
        LOGGER.info("Init vector key is {}", credential.getIvKey()); //Required for encryption
        LOGGER.info("password is {}", credential.getPassword());
        LOGGER.info("code is {}", credential.getCode());


        //you can get single bvn using the reset credential generated this way

        //create the request object
        SingleBVNRequest request = new SingleBVNRequest();
        request.setBankVerificationNumber("12345678901");

        //seralize the request to string
        String bvnRequestString = objectMapper.writeValueAsString(request);

        //encrypt the request
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString, credential.getAesKey(), credential.getIvKey());

        //generate headers for the request
        HttpHeaders getSingleBvnHeader =  bvnValidationservice.generateHttpAuthHeaders(sandBoxKey, credential.getPassword(), credential.getCode());

        HttpEntity getSingleBvnEntity = new HttpEntity(encryptedHex, getSingleBvnHeader);
        HttpEntity<String> getsinglebvnResponse = restTemplate.exchange(getsinglebvnUrl, HttpMethod.POST, getSingleBvnEntity, String.class);

        //get the body
        String response2 = getsinglebvnResponse.getBody();

        //decrypt the rsponse from the call
        String decrypted = securityConfiguration.decrypt(response2, credential.getAesKey(), credential.getIvKey());
        //deserialize the response
        GetSingleBVNResponse singleBVNResponse = objectMapper.readValue(decrypted, GetSingleBVNResponse.class);

        LOGGER.info("Level of account is {}", singleBVNResponse.getData().getLevelOfAccount());
        LOGGER.info("Phone number 1 is {}", singleBVNResponse.getData().getPhoneNumber1());
        LOGGER.info("Email is {}", singleBVNResponse.getData().getEmail());
        LOGGER.info("Firstname is {}", singleBVNResponse.getData().getFirstName());
        LOGGER.info("LastName is {}",singleBVNResponse.getData().getLastName());



        //another implementation

        //create the request object
        SingleBVNRequest request2 = new SingleBVNRequest();
        request2.setBankVerificationNumber("12345678901");

        //seralize the request to string
        String bvnRequestString2 = objectMapper.writeValueAsString(request2);

        //important call to this method, caches the ciphers for reuse in-memory for faster encryption
        //if you have multiple instances, you can use like a topic or something else to broadcast the credential value to all instances, then call this method in them
        securityConfiguration.setResetCredential(credential);

        //encrypt the request
        String encryptedHex2 = securityConfiguration.encrypt(bvnRequestString2);

        //generate headers for the request
        HttpHeaders getSingleBvnHeader2 =  bvnValidationservice.generateHttpAuthHeaders(sandBoxKey);

        HttpEntity getSingleBvnEntity2 = new HttpEntity(encryptedHex2, getSingleBvnHeader2);
        HttpEntity<String> getsinglebvnResponse2 = restTemplate.exchange(getsinglebvnUrl, HttpMethod.POST, getSingleBvnEntity2, String.class);

        //get the body
        String response3 = getsinglebvnResponse2.getBody();

        //decrypt the rsponse from the call
        String decrypted2 = securityConfiguration.decrypt(response2, credential.getAesKey(), credential.getIvKey());
        //deserialize the response
        GetSingleBVNResponse singleBVNResponse2 = objectMapper.readValue(decrypted2, GetSingleBVNResponse.class);

        LOGGER.info("Level of account is {}", singleBVNResponse2.getData().getLevelOfAccount());
        LOGGER.info("Phone number 1 is {}", singleBVNResponse2.getData().getPhoneNumber1());
        LOGGER.info("Email is {}", singleBVNResponse2.getData().getEmail());
        LOGGER.info("Firstname is {}", singleBVNResponse2.getData().getFirstName());
        LOGGER.info("LastName is {}",singleBVNResponse2.getData().getLastName());

    }

}

```
