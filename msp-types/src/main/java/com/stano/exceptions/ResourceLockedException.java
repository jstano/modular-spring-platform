package com.stano.exceptions;

public class ResourceLockedException extends RuntimeException {

   public ResourceLockedException(String message) {

      super(message);
   }

   public ResourceLockedException(String message, Throwable cause) {

      super(message, cause);
   }
}
