package com.land.shared.exception;

@SuppressWarnings("serial")
public class MailException extends AnyServiceException {

  public MailException() {
    super();
  }

  public MailException(String message) {
    super(message);
  }

  public MailException(String message, Throwable cause) {
    super(message, cause);
  }

}
