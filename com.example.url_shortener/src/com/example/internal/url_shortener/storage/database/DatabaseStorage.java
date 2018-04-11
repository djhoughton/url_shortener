package com.example.internal.url_shortener.storage.database;

import java.net.URL;

import com.example.internal.url_shortener.Storage;
import com.example.url_shortener.UrlShortenerException;

/**
 * Database implementation of the storage.
 */
public class DatabaseStorage implements Storage {

	private static final String TABLE_NAME = "URL_SHORTENER";
	private static final String COLUMN_URL = "URL";
	private static final String COLUMN_INDEX = "INDEX";

	public DatabaseStorage() {
		initialize();
	}

	private void initialize() {
		// TODO create DB connection

		// TODO if table exists then return
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_INDEX + " LONG, " + COLUMN_URL + " VARCHAR(255))";
	}

	@Override
	public long[] getIndexes(URL url) throws UrlShortenerException {
		String sql = "SELECT " + COLUMN_INDEX + " FROM " + TABLE_NAME + " WHERE " + COLUMN_URL + "='" + url + "'";
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL resolve(long index) throws UrlShortenerException {
		String sql = "SELECT " + COLUMN_URL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_INDEX + "=" + index;
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long store(URL url, long index) throws UrlShortenerException {
		return internalStore(url, index);
	}

	private long internalStore(URL url, long index) throws UrlShortenerException {
		String sql = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_INDEX + ", " + COLUMN_URL + ") VALUES (" + index + ", "
				+ url.toExternalForm() + ")";
		// TODO
		return -1;
	}

	@Override
	public long store(URL url) throws UrlShortenerException {
		long index = nextAvailableIndex();
		return internalStore(url, index);
	}

	@Override
	public long nextAvailableIndex() throws UrlShortenerException {
		// TODO Auto-generated method stub
		return 0;
	}

}
