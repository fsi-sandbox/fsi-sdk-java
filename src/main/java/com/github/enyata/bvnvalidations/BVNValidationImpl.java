package com.github.enyata.bvnvalidations;

import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.util.StringUtils;
import com.github.enyata.vo.ResetCredential;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class BVNValidationImpl  {

    /**
     * This method generates the required Headers required for calling the /Reset endpoint
     * @param sandBoxKey  The sandbox key is available on the FSI Portal
     * @param organizationCode Usually provided by Nibss. For testing purposse, the value of 11111 is used. This value
     *                         may change on production.
     * @return HttpHeaders  Object that contains key-pair value for calling the /Reset endpoint
     * @throws IllegalArgumentException
     */
    public HttpHeaders getResetCredentialsHeaders(String sandBoxKey, String organizationCode) throws IllegalArgumentException {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isBlank(sandBoxKey) || StringUtils.isBlank(organizationCode)){
            throw new IllegalArgumentException("sandBoxKey or organizationCode value required to generate reset credentials is missing.");
        }

        headers.set("OrganisationCode", Base64.encodeBase64String(organizationCode.getBytes()));
        headers.set("Sandbox-Key", sandBoxKey);
        return headers;
    }

    /**
     * The Reset credentials are usually returned in the response header after calling the /Reset endpoint
     * Call this method and pass the response headers to help you get this required values.
     * @param headers Contains four required key-pair values(keys include AES_KEY, Code, IVKey, PASSWORD)
     *                that will be used for all other nibss interfaces
     * @return ResetCredential  Object that holds thee gotten values from the headers
     * @throws IllegalArgumentException
     * @throws BadRemoteResponseException
     */
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

}
