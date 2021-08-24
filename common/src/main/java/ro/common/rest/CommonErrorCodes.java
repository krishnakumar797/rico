/* Licensed under Apache-2.0 */
package ro.common.rest;

public interface CommonErrorCodes {

  int ERROR_CODE_LENGTH = 5;
  String ERRORCODE_PATTERN = "^ERR-\\w+-\\d{" + ERROR_CODE_LENGTH + "}$";

    // Information responses
    String E_HTTP_CONTINUE = "100";
    String E_HTTP_SWITCHING_PROTOCOL = "101";
    String E_HTTP_PROCESSING = "102";
    String E_HTTP_EARLY_HINTS = "103";

    // Successful responses
    String E_HTTP_OK = "200";
    String E_HTTP_CREATED = "201";
    String E_HTTP_ACCEPTED = "202";
    String E_HTTP_NON_AUTHORITATIVE_INFORMATION = "203";
    String E_HTTP_NO_CONTENT = "204";
    String E_HTTP_RESET_CONTENT = "205";
    String E_HTTP_PARTIAL_CONTENT = "206";
    String E_HTTP_MULTI_STATUS = "207";
    String E_HTTP_ALREADY_REPORTED = "208";
    String E_HTTP_IM_USED = "209";

    // Redirection messages
    String E_HTTP_MULTIPLE_CHOICE = "300";
    String E_HTTP_MOVED_PERMANENTLY = "301";
    String E_HTTP_FOUND = "302";
    String E_HTTP_SEE_OTHER = "303";
    String E_HTTP_NOT_MODIFIED = "304";
    String E_HTTP_USE_PROXY = "305";
    String E_HTTP_UNUSED = "306";
    String E_HTTP_TEMPORARY_REDIRECT = "307";
    String E_HTTP_PERMANENT_REDIRECT = "308";

    // Client error responses
    String E_HTTP_BAD_REQUEST = "400";
    String E_HTTP_UNAUTHORIZED = "401";
    String E_HTTP_PAYMENT_REQUIRED = "402";
    String E_HTTP_FORBIDDEN = "403";
    String E_HTTP_NOT_FOUND = "404";
    String E_HTTP_METHOD_NOT_ALLOWED = "405";
    String E_HTTP_NOT_ACCEPTABLE = "406";
    String E_HTTP_PROXY_AUTHENTICATION_REQUIRED = "407";
    String E_HTTP_REQUEST_TIMEOUT = "408";
    String E_HTTP_CONFLICT = "409";
    String E_HTTP_GONE = "410";
    String E_HTTP_LENGTH_REQUIRED = "411";
    String E_HTTP_PRECONDITION_FAILED = "412";
    String E_HTTP_PAYLOAD_TOO_LARGE = "413";
    String E_HTTP_URI_TOO_LONG = "414";
    String E_HTTP_UNSUPPORTED_MEDIA_TYPE = "415";
    String E_HTTP_RANGE_NOT_SATISFIABLE = "416";
    String E_HTTP_EXPECTATION_FAILED = "417";
    String E_HTTP_IM_A_TEAPOT = "418";
    String E_HTTP_MISDIRECTED_REQUEST = "421";
    String E_HTTP_UNPROCESSABLE_ENTITY = "422";
    String E_HTTP_LOCKED = "423";
    String E_HTTP_FAILED_DEPENDENCY = "424";
    String E_HTTP_TOO_EARLY = "425";
    String E_HTTP_UPGRADE_REQUIRED = "426";
    String E_HTTP_PRECONDITION_REQUIRED = "428";
    String E_HTTP_TOO_MANY_REQUESTS = "429";
    String E_HTTP_REQUEST_HEADER_FIELDS_TOO_LARGE = "431";
    String E_HTTP_UNAVAILABLE_FOR_LEGAL_REASONS = "451";

    // Server error responses
    String E_HTTP_INTERNAL_SERVER_ERROR = "500";
    String E_HTTP_NOT_IMPLEMENTED = "501";
    String E_HTTP_BAD_GATEWAY = "502";
    String E_HTTP_SERVICE_UNAVAILABLE = "503";
    String E_HTTP_GATEWAY_TIMEOUT = "504";
    String E_HTTP_HTTP_VERSION_NOT_SUPPORTED = "505";
    String E_HTTP_VARIANT_ALSO_NEGOTIATES = "506";
    String E_HTTP_INSUFFICIENT_STORAGE = "507";
    String E_HTTP_LOOP_DETECTED = "508";
    String E_HTTP_NOT_EXTENDED = "510";
    String E_HTTP_NETWORK_AUTHENTICATION_REQUIRED = "511";
}
