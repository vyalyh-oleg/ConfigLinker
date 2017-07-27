package com.configlinker.exceptions;


public class AnnotationAnalyzeException extends ConfigLinkerRuntimeException {
	public AnnotationAnalyzeException(String message) {
		super(message);
	}

	public AnnotationAnalyzeException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnnotationAnalyzeException(Throwable cause) {
		super(cause);
	}
}
