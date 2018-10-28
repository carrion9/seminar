package com.vetx.starter.exception;

import java.net.MalformedURLException;

public class StorageFileNotFoundException extends RuntimeException {
  public StorageFileNotFoundException(String message, MalformedURLException e) {
    super(message, e);
  }

  public StorageFileNotFoundException(String message) {
    super(message);
  }
}
