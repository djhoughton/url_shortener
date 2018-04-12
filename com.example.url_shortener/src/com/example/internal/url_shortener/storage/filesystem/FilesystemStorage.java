package com.example.internal.url_shortener.storage.filesystem;

import java.io.File;
import java.net.URL;

import com.example.internal.url_shortener.Storage;
import com.example.url_shortener.UrlShortenerException;

/**
 * Class representing a simple file-system implementation.
 */
public class FilesystemStorage implements Storage {

	private static final String VALID_URL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~:/?#[]@!$&'()*+,;=`.";

	private File root = new File("/Users/dj/testing");

	public FilesystemStorage() {
		root.mkdirs();
	}

	@Override
	public long getIndex(URL url) throws UrlShortenerException {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public URL getUrl(long index) throws UrlShortenerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long store(URL url, long index) throws UrlShortenerException {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public long store(URL url) throws UrlShortenerException {
		// TODO Auto-generated method stub
		return -1;
	}
}
