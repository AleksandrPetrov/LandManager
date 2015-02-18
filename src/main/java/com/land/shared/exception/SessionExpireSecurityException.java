package com.land.shared.exception;

@SuppressWarnings("serial")
public class SessionExpireSecurityException extends ServiceSecurityException {

  public SessionExpireSecurityException() {
    super();
  }

  public SessionExpireSecurityException(String message) {
    super(message, null);
  }

  public SessionExpireSecurityException(String message, Throwable cause) {
    super(message, cause);
  }

}
