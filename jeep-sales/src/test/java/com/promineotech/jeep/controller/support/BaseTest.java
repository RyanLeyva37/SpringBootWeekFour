package com.promineotech.jeep.controller.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import lombok.Getter;

public class BaseTest {
  @LocalServerPort
  private int serverPort;
  
  protected String getBaseUriForJeeps() {
    return String.format("http://localhost:%d/jeeps", serverPort);
  }
  
  protected String getBaseUriForOrders() {
	    return String.format("http://localhost:%d/orders", serverPort);
	  }
  
  @Autowired
  private TestRestTemplate restTemplate;
  
  public TestRestTemplate getRestTemplate() {
	return restTemplate;
}

}
 