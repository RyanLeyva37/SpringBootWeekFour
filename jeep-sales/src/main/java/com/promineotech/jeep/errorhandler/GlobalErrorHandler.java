package com.promineotech.jeep.errorhandler;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.request.ServletWebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
	
	private enum LogStatus{
		STACK_TRACE, MESSAGE_ONLY
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request){
		return createExceptionMessage(e, HttpStatus.BAD_REQUEST, request, LogStatus.MESSAGE_ONLY);
	
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handlerConstraintVoilationException(ConstraintViolationException e, WebRequest webRequest){
		return createExceptionMessage(e, HttpStatus.BAD_REQUEST, webRequest, LogStatus.MESSAGE_ONLY);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, Object> handleNoSuchElementException(NoSuchElementException e, WebRequest request){
		return createExceptionMessage(e, HttpStatus.NOT_FOUND, request, LogStatus.MESSAGE_ONLY);
		 
	}

	private Map<String, Object> createExceptionMessage(Exception e, HttpStatus status, WebRequest request,
			LogStatus logStatus) {
		Map<String, Object> error = new HashMap<>();
		String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		
		
		if(request instanceof ServletWebRequest) {
			error.put("uri", 
					((ServletWebRequest)request).getRequest().getRequestURI());
		}
		
		
		error.put("message", e.toString());
		error.put("status code", status.value());
		error.put("timestamp", timestamp);
		error.put("reason", status.getReasonPhrase());
		
		if(logStatus == LogStatus.MESSAGE_ONLY) {
			log.error("Exception: {}", e.toString());
		}else {
			log.error("Exception: {}", e);
		}
		
		return error;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public  Map<String, Object> handleException(Exception e, WebRequest request){
		return createExceptionMessage(e, HttpStatus.INTERNAL_SERVER_ERROR, request, LogStatus.STACK_TRACE);
	}
	

}
