package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.promineotech.jeep.Constants;
import com.promineotech.jeep.controller.support.FetchJeepTestSupport;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.service.JeepSalesService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
		"classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
		config = @SqlConfig(encoding = "utf-8"))
class FetchJeepTest extends FetchJeepTestSupport{
	
	
	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
			"classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
			config = @SqlConfig(encoding = "utf-8"))
	class TestsThatDoNotPolluteTheApplicationContext extends FetchJeepTestSupport{
		  @Test
		  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
		    
//		    JeepModel model = JeepModel.WRANGLER;
//		    String trim = "Sport";
//		    String uri = String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
		//    
//		    ResponseEntity<Jeep> response = super.getRestTemplate().getForEntity(uri, Jeep.class);
		//    
//		    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			  
			  
		    // Given: a valid model, trim and URI
		    JeepModel model = JeepModel.WRANGLER;
		    String trim = "Sport";
		    String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim);
		    
		    // When: a connection is made to the URI
		    @SuppressWarnings("unchecked")
			ResponseEntity<List<Jeep>> response = super.getRestTemplate().exchange(uri, HttpMethod.GET, null, 
		    			new ParameterizedTypeReference<>() {});
		    // Then: a successs (200) status code is returned
		    
		    //System.out.println(getBaseUri());
		    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		    
		    
		    // And: the actual list returned is the same as the expected list
		    List<Jeep> actual = response.getBody();
		    List<Jeep> expected = buildExpected();
		    
		    actual.forEach(jeep -> jeep.setModelPK(null));
		    
		    assertThat(response.getBody()).isEqualTo(expected);
		    System.out.println("OUTPUT: " + response.getBody());
		  }
		  
		  
		  @Test
		  void testThatAnErrorMessageIsReturnedWhenAnUnknownTrimIsSupplied() {
		    
//		    JeepModel model = JeepModel.WRANGLER;
//		    String trim = "Sport";
//		    String uri = String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
		//    
//		    ResponseEntity<Jeep> response = super.getRestTemplate().getForEntity(uri, Jeep.class);
		//    
//		    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			  
			  
		    // Given: a valid model, trim and URI
		    JeepModel model = JeepModel.WRANGLER;
		    String trim = "Invalid Value";
		    String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim);
		    
		    // When: a connection is made to the URI
		    @SuppressWarnings("unchecked")
			ResponseEntity<Map<String, Object>> response = super.getRestTemplate().exchange(uri, HttpMethod.GET, null, 
		    			new ParameterizedTypeReference<>() {});
		    // Then: a successs (200) status code is returned
		    
		    //System.out.println(getBaseUri());
		    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		    
		    
		    // And: an error message is returned
		    Map<String, Object> error = response.getBody();
		    assertErrorMessageValid(error, HttpStatus.NOT_FOUND);
		  }
		  
		  
		  
		  
		  
		  
		  @ParameterizedTest
		  @MethodSource("com.promineotech.jeep.controller.FetchJeepTest#parametersForInvalidInput")
		  void testThatAnErrorMessageIsReturnedWhenAnInvalidTrimIsSupplied(
				  String model, String trim, String reason) {
		    
			  
			  
		    // Given: a valid model, trim and URI

		    String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim);
		    
		    // When: a connection is made to the URI
		    @SuppressWarnings("unchecked")
			ResponseEntity<Map<String, Object>> response = super.getRestTemplate().exchange(
					uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
		    // Then: a successs (200) status code is returned
		    
		    //System.out.println(getBaseUri());
		    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		    
		    
		    // And: an error message is returned
		    Map<String, Object> error = response.getBody();
		    
		    
		    assertErrorMessageValid(error, HttpStatus.BAD_REQUEST);
		    
		  }
	}
	
	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
			"classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
			config = @SqlConfig(encoding = "utf-8"))
	class TestsThatPolluteTheApplicationContext extends FetchJeepTestSupport{
		
		@MockBean
		private JeepSalesService jeepSalesService;
		
		  @Test
		  void testThatAnUnplannedErrorResultsInA500Status() {
			  
		    // Given: a valid model, trim and URI
		    JeepModel model = JeepModel.WRANGLER;
		    String trim = "Invalid";
		    String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim);
		    
		    
		    doThrow(new RuntimeException("Ouch!")).when(jeepSalesService).fetchJeeps(model, trim);
		    
		    // When: a connection is made to the URI
		    @SuppressWarnings("unchecked")
			ResponseEntity<Map<String, Object>> response = super.getRestTemplate().exchange(uri, HttpMethod.GET, null, 
		    			new ParameterizedTypeReference<>() {});
		    // Then: an internal server error (500) status is returned
		    
		    
		    // And: an error message is returned
		    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		  }
		 
	}
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Test
	void testDb() {
		int numrows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "customers");
		System.out.println(numrows);
	}
	





  static Stream<Arguments> parametersForInvalidInput(){
	  return Stream.of(
			  arguments("WRANGLER", "@#$%^&&%", "Trim contains non-alpha-numeric characters"),
			  arguments("WRANGLER", "C".repeat(Constants.TRIM_MAX_LENGTH + 1), "Trim length too long"),
			  arguments("INVALID", "Sport", "Model is not enum value")
			  
	  );
  }
  



}