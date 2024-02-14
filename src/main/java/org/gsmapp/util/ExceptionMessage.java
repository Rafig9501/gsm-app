package org.gsmapp.util;

public final class ExceptionMessage {
    public static final String INPUT_VALIDATION = "Input validation has been failed";
    public static final String INTERNAL_ERROR = "Internal error has been occurred";
    public static final String METHOD_NOT_SUPPORTED = "Method is not supported";
    public static final String BAD_REQUEST_EXCEPTION = "Request body validation has failed due to some reasons";
    public static final String USER_ALREADY_EXISTS_EXCEPTION = "User already exists in database";
    public static final String NOT_FOUND_EXCEPTION = "No records in database";
    public static final String CUSTOMER_NOT_FOUND = "No customer record found in database";
    public static final String NOT_ENOUGH_BALANCE = "Customer has no enough balance for purchase";
    public static final String TRANSACTION_AMOUNT_LESS = "Purchase transaction amount less than refund amount or transaction already been refunded";
    public static final String INVALID_TRANSACTION_TYPE = "Invalid transaction type: ";
    public static final String USER_NOT_FOUND = "No user record found in database";
    public static final String AUTHENTICATION_EXCEPTION = "Authentication failed, wrong username or password";
    public static final String AUTHORIZATION_EXCEPTION = "Authorization failed, you don't have permissions";
    public static final String TOKEN_NOT_FOUND = "Token owner has been deleted or is not active";
    public static final String TOKEN_CANNOT_BE_REFRESHED = "Token cannot be refreshed";
    public static final String TOKEN_NOT_FOUND_IN_REQUEST = "Request does not contain token";
    private ExceptionMessage() {
    }
}