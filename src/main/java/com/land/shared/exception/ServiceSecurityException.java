package com.land.shared.exception;

@SuppressWarnings("serial")
public class ServiceSecurityException extends AnyServiceException {

  public ServiceSecurityException() {
    super();
  }

  public ServiceSecurityException(String message) {
    super(message, null);
  }

  public ServiceSecurityException(String message, Throwable cause) {
    super(message, cause);
  }

}
