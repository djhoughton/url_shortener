package com.example.url_shortener;

/**
 * Class used to represent an exception when the user requests a URL be
 * associated with a specific alias but that alias is already being used by
 * another URL.
 */
public class AliasAlreadyExistsException extends UrlShortenerException {

	private static final long serialVersionUID = 1L;

	public AliasAlreadyExistsException(String message) {
		super(message);
	}

	public AliasAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public AliasAlreadyExistsException(Throwable cause) {
		super(cause);
	}
}
