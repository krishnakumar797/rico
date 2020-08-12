package ro.common.rest;

public interface CommonErrorCodes {

  int ERROR_CODE_LENGTH = 5;
  String ERRORCODE_PATTERN = "^ERR-\\w+-\\d{" + ERROR_CODE_LENGTH + "}$";

  String E_HTTP_MISSING_SERVLET_REQ_PARAM = "203";
  String E_HTTP_BINDING_ERR = "207";
  String E_HTTP_TYPE_MISMATCH = "205";
  String E_HTTP_HTTP_MSG_UNREADABLE = "206";
  String E_GEN_INTERNAL_ERR = "500";
  String E_HTTP_METHOD_NOT_ALLOWED = "208";
  String E_HTTP_BAD_REQUEST = "400";
  String E_HTTP_UNKNOWN_URL = "404";
  String E_HTTP_ILLEGAL_URL_PARAM = "201";
  String E_HTTP_ILLEGAL_JSON_PARAM = "202";
  String E_HTTP_UNAUTHORIZED_RQST = "209";
}
