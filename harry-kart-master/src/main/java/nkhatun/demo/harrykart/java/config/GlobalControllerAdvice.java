package nkhatun.demo.harrykart.java.config;

import java.net.BindException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
@ControllerAdvice
@RestController
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler{
	private static Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);
	
	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex,
			final WebRequest request) {
		return new ResponseEntity<Object>(
				"Invalid field value/values present in request", new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidDefinitionException.class)
	public final ResponseEntity<Void> handleInvalidDefinitionException(final InvalidDefinitionException ex,
			final WebRequest request) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {BindException.class})
	public @ResponseBody ResponseEntity<Object> handleException(BindException e) {
		logger.error("Global Validation Exception", e);
		return new ResponseEntity<Object>(
				"Request parametr is missing", new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	protected ResponseEntity<Object> handleHttpMediaNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("HttpMediaTypeNotSupportedException", ex);
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(
				" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
		return new ResponseEntity<Object>(
				builder.substring(0, builder.length() - 2), new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	@ExceptionHandler(value = MethodNotAllowedException.class)
	public final ResponseEntity<Object> handleInvalidDefinitionException(final MethodNotAllowedException ex,
																	   final WebRequest request) {
		logger.info("Global Exception: MethodNotAllowedException", ex.getMessage());
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getHttpMethod());
		builder.append(
				" method is not allowed. Allowed methods are ");
		ex.getSupportedMethods().forEach(t -> builder.append(t).append(", "));
		return new ResponseEntity<Object>(
				builder.substring(0, builder.length() - 2), new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(value = Exception.class)
	public @ResponseBody
	ResponseEntity<Object> handleException(Exception e) {
		logger.error("Global Exception", e);
		return new ResponseEntity<Object>(
				"An exception occurred. Please check with Support.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
