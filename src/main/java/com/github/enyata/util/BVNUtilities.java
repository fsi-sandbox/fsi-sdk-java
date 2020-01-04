package com.github.enyata.util;

import java.util.List;

public class BVNUtilities {

    public static boolean lengthIsExpected(List<String> bankVerificationNumbers, List<Integer> allowedBVNLengths){
        return bankVerificationNumbers.stream()
                .allMatch(s-> !StringUtils.isBlank(s) && allowedBVNLengths.contains(s.length()) );
    }

    public static boolean lengthIsExpected(String bankVerificationNumber, List<Integer> allowedBVNLengths){
        return !StringUtils.isBlank(bankVerificationNumber) && allowedBVNLengths.contains(bankVerificationNumber.length());
    }
}
