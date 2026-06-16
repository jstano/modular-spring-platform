package com.stano.spring_boot_application.error;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FixtureTestController.class)
class GlobalExceptionHandlerTest {
  @Autowired private MockMvc mockMvc;

  @Test
  void resourceNotFoundExceptionShouldReturn404WithProblemDetailBody() throws Exception {
    mockMvc
        .perform(get("/test/errors/not-found"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.detail").value("Widget 42 not found"))
        .andExpect(jsonPath("$.instance").value("/test/errors/not-found"))
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.properties").doesNotExist());
  }

  @Test
  void entityNotFoundExceptionShouldReturn404WithProblemDetailBody() throws Exception {
    mockMvc
        .perform(get("/test/errors/entity-not-found"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.detail").value("Widget 99 not found"));
  }

  @Test
  void jpaObjectRetrievalFailureExceptionShouldReturn404WithProblemDetailBody() throws Exception {
    mockMvc
        .perform(get("/test/errors/jpa-object-retrieval-failure"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404));
  }

  @Test
  void badRequestExceptionShouldReturn400WithProblemDetailBody() throws Exception {
    mockMvc
        .perform(get("/test/errors/bad-request"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").value("Missing required field 'name'"));
  }

  @Test
  void uncaughtExceptionShouldReturn500WithGenericDetailAndNotLeakInternals() throws Exception {
    mockMvc
        .perform(get("/test/errors/boom"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(
            jsonPath("$.detail")
                .value(
                    "An unexpected error occurred. Please contact support if the problem"
                        + " persists."))
        .andExpect(jsonPath("$.detail", not(containsString("kaboom"))));
  }
}
