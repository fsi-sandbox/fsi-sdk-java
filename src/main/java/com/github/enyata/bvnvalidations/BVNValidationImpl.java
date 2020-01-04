package com.github.enyata.bvnvalidations;

import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.util.StringUtils;
import com.github.enyata.vo.ResetCredential;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class BVNValidationImpl  {

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
}
