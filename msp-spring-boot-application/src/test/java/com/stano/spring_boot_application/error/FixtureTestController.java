package com.stano.spring_boot_application.error;

import com.stano.exceptions.BadRequestException;
import com.stano.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/errors")
class FixtureTestController {
  @GetMapping("/not-found")
  String notFound() {
    throw new ResourceNotFoundException("Widget 42 not found");
  }

  @GetMapping("/entity-not-found")
  String entityNotFound() {
    throw new EntityNotFoundException("Widget 99 not found");
  }

  @GetMapping("/jpa-object-retrieval-failure")
  String jpaObjectRetrievalFailure() {
    throw new JpaObjectRetrievalFailureException(new EntityNotFoundException("Widget 7 not found"));
  }

  @GetMapping("/bad-request")
  String badRequest() {
    throw new BadRequestException("Missing required field 'name'");
  }

  @GetMapping("/boom")
  String boom() {
    throw new IllegalStateException("kaboom - internal detail that must not leak");
  }
}
