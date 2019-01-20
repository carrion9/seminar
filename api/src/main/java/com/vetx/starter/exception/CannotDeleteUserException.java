package com.vetx.starter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class CannotDeleteUserException extends RuntimeException {
  public CannotDeleteUserException() {
    super("Cannot delete this User");
  }
}
