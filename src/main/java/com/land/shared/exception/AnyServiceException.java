package com.land.shared.exception;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AnyServiceException extends Exception implements Serializable, IsSerializable {

  public AnyServiceException() {
    super();
  }

  public AnyServiceException(String message) {
    super(message, null);
  }

  public AnyServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public AnyServiceException(Exception e) {
    super(e.getMessage(), e);
  }

}
