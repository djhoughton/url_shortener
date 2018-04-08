package com.example.url_shortener;

/**
 * Class used to represent a problem in the URL Shortener Service
 */
public class UrlShortenerException extends Exception {

	private static final long serialVersionUID = 1L;

	public UrlShortenerException(String message) {
		super(message);
	}

	public UrlShortenerException(String message, Throwable cause) {
		super(message, cause);
	}

	public UrlShortenerException(Throwable cause) {
		super(cause);
	}

}
