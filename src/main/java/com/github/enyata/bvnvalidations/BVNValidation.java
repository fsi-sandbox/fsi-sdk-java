package com.github.enyata.bvnvalidations;

import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.vo.ResetCredential;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface BVNValidation {


    /**
     * This method generates the required Headers required for calling the /Reset endpoint
     * @param sandBoxKey  The sandbox key is available on the FSI Portal
     * @param organizationCode Usually provided by Nibss. For testing purposse, the value of 11111 is used. This value
     *                         may change on production.
     * @return HttpHeaders  Object that contains key-pair value for calling the /Reset endpoint
     * @throws IllegalArgumentException
     */
    public HttpHeaders getResetCredentialsHeaders(String sandBoxKey, String organizationCode) throws IllegalArgumentException;

    /**
     * The Reset credentials are usually returned in the response header after calling the /Reset endpoint
     * Call this method and pass the response headers to help you get this required values.
     * @param headers Contains four required key-pair values(keys include AES_KEY, Code, IVKey, PASSWORD)
     *                that will be used for all other nibss interfaces
     * @return ResetCredential  Object that holds thee gotten values from the headers
     * @throws IllegalArgumentException
     * @throws BadRemoteResponseException
     */
    public ResetCredential getResetCredentialsFromResponseHeaders(HttpHeaders headers) throws IllegalArgumentException, BadRemoteResponseException;


    /**
     * This method generates all the required headers for calls to all subsequent available BVN endpoints.
     * @param sandBoxKey
     * @param password
     * @param code
     * @return HttpHeaders
     * @throws EncryptionException
     */
    public HttpHeaders generateHttpAuthHeaders(String sandBoxKey, String password, String code) throws EncryptionException;

    /**
     * This method generates all the required headers for calls to all subsequent available BVN endpoints.
     * @param sandBoxKey
     * @return HttpHeaders
     * @throws EncryptionException
     */
    public HttpHeaders generateHttpAuthHeaders(String sandBoxKey) throws EncryptionException;
}
