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
	public URL[] getAliases(URL longForm) throws UrlShortenerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL resolve(URL shortForm) throws UrlShortenerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(URL longForm, URL shortForm, long index) throws UrlShortenerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(URL longForm, URL shortForm) throws UrlShortenerException {
		// TODO Auto-generated method stub

	}

	@Override
	public long nextAvailableIndex() throws UrlShortenerException {
		// TODO Auto-generated method stub
		return 0;
	}

}
