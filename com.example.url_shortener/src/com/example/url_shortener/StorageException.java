package com.example.url_shortener;

/**
 * Class used to represent a problem with the service's storage.
 * 
 * This could be issues with fetching or saving items.
 */
public class StorageException extends UrlShortenerException {

	private static final long serialVersionUID = 1L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}

}
