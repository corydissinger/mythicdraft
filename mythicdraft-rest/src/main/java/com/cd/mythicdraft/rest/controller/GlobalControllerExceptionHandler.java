package com.cd.mythicdraft.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	private static final Logger logger = Logger.getLogger(GlobalControllerExceptionHandler.class);	
	
	@ExceptionHandler(Exception.class) 
	public @ResponseBody String errorHandler(HttpServletRequest req, Exception exc){
		logger.error(ExceptionUtils.getStackTrace(exc));
		
		return "Error";
	}
	
}
