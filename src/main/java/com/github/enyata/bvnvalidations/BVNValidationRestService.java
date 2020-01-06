package com.github.enyata.bvnvalidations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.enyata.exceptions.BadRemoteResponseException;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.vo.GetSingleBVNResponse;
import com.github.enyata.vo.ResetCredential;
import org.springframework.stereotype.Service;

@Service
public interface BVNValidationRestService {


    /**
     * This method calls the /Reset endpoint
     * @param sandboxKey
     * @param organizationCode
     * @return
     * @throws BadRemoteResponseException
     * @throws EncryptionException
     */
    public ResetCredential resetCredentials(String sandboxKey, String organizationCode) throws BadRemoteResponseException, EncryptionException ;



    /**
     * This method calls the /GetSingleBVN endpoint
     * @param bvn
     * @param sandboxKey
     * @param code
     * @return
     * @throws EncryptionException
     * @throws JsonProcessingException
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
    public GetSingleBVNResponse getSingleBVN(String bvn, String sandboxKey,  String code, String password, String encryptionKey, String ivKey) throws EncryptionException, JsonProcessingException ;

}