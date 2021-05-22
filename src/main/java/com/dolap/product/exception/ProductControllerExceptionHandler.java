package com.dolap.product.exception;

import com.dolap.product.dto.ErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ProductControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers, HttpStatus status,
																  WebRequest request) {
		List<String> details = new ArrayList<>();
		for(ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		ErrorDTO error = new ErrorDTO(ex.getMessage(), details);
		return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(UpdatingProductNotExistsException.class)
	public ResponseEntity<ErrorDTO> handleUpdatingProductNotExistsException(UpdatingProductNotExistsException ex) {
		ErrorDTO error = new ErrorDTO(ex.getMessage(), Collections.singletonList(ex.getMessage()));
		return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
	}
}
