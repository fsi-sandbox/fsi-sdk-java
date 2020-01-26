package com.github.enyata.bvnvalidations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BVNValidationRestService {


    /**
     * This method calls the /Reset endpoint
     * @param sandboxKey Provided in the sandbox portal
     * @param organizationCode
     * @return ResetCredential Object thar contains all the credentials for subsequent calls to other BVN validation endpoints
     * @throws BadRemoteResponseException Exception is thrown when the required response header values are missing. This is unlikely thrown
     *          as long as API is not changed
     * @throws EncryptionException error that occurs encrypting header values
     */
    public ResetCredential resetCredentials(String sandboxKey, String organizationCode) throws BadRemoteResponseException, EncryptionException ;



    /**
     * This method calls the /GetSingleBVN endpoint
     * @param bvn  BVN to be validated
     * @param sandboxKey Provided in the sandbox portal
     * @param code provided on the Sandbox portal
     * @return
     * @throws EncryptionException error encrypting or decrypting request/response
     * @throws JsonProcessingException error serializing/dezerializing request
     * @throws BadRemoteResponseException
     */
    public GetSingleBVNResponse getSingleBVN(String bvn, String sandboxKey,  String code) throws EncryptionException, JsonProcessingException ;


    /**
     * This method calls the /GetSingleBVN endpoint
     * @param bvn
     * @param sandboxKey
     * @param code
     * @param password
     * @param encryptionKey
     * @param ivKey
     * @return
     * @throws EncryptionException
     * @throws JsonProcessingException
     *
     */
    GetSingleBVNResponse getSingleBVN(String bvn, String sandboxKey,  String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException;


    /**
     *
     * @param bvn
     * @param sandboxKey
     * @param code
     * @param password
     * @param encryptionKey
     * @param ivKey
     * @return
     * @throws EncryptionException
     * @throws JsonProcessingException
     */

    VerifySingleBVNResponse verifySingleBVN(String bvn, String sandboxKey, String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException;

    /**
     *
     * @param bvn
     * @param sandboxKey
     * @param code
     * @return
     * @throws EncryptionException
     * @throws JsonProcessingException
     */
    VerifySingleBVNResponse verifySingleBVN(String bvn, String sandboxKey,  String code) throws EncryptionException, JsonProcessingException;

    /**
     *
     * @param bvn
     * @param sandboxKey
     * @param code
     * @return
     * @throws EncryptionException
     * @throws JsonProcessingException
     */

    IsBVNWatchlistedResponse isBVNWatchlisted(String bvn, String sandboxKey,  String code) throws EncryptionException, JsonProcessingException;

    /**
     *
     * @param bvn
     * @param sandboxKey
     * @param code
     * @param password
     * @param encryptionKey
     * @param ivKey
     * @return
     * @throws EncryptionException
     * @throws JsonProcessingException
     */
    IsBVNWatchlistedResponse isBVNWatchlisted(String bvn, String sandboxKey, String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException;

    /**
     *
     * @param bankVerificationNumbers
     * @param sandboxKey
     * @param code
     * @param password
     * @param encryptionKey
     * @param ivKey
     * @return
     * @throws EncryptionException
     * @throws JsonProcessingException
     */
    GetMultipleBVNResponse getMultipleBVN(List<String> bankVerificationNumbers, String sandboxKey, String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException;

    /**
     *
     * @param bankVerificationNumbers
     * @param sandboxKey
     * @param code
     * @return
     * @throws EncryptionException
     * @throws JsonProcessingException
     */

    GetMultipleBVNResponse getMultipleBVN(List<String> bankVerificationNumbers, String sandboxKey, String code) throws EncryptionException, JsonProcessingException;

}