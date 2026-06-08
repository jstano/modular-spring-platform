package com.stano.exceptions;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {

   public RuntimeIOException(IOException cause) {

      super(cause);
   }

   public RuntimeIOException(Throwable cause) {

      super(cause);
   }
}
