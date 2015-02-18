package com.land.shared.exception;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MailUnableToSendException extends MailException {

  private List<String> faildAddressList = new ArrayList<String>();
  private List<String> unsentAddressList = new ArrayList<String>();

  public MailUnableToSendException() {
    super();
  }

  public MailUnableToSendException(String message, List<String> faildAddressList, List<String> unsentAddressList) {
    super(message);
    this.faildAddressList = faildAddressList;
    this.unsentAddressList = unsentAddressList;
  }

  public List<String> getFaildAddressList() {
    return faildAddressList;
  }

  public void setFaildAddressList(List<String> faildAddressList) {
    this.faildAddressList = faildAddressList;
  }

  public List<String> getUnsentAddressList() {
    return unsentAddressList;
  }

  public void setUnsentAddressList(List<String> unsentAddressList) {
    this.unsentAddressList = unsentAddressList;
  }

}
