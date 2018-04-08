package com.example.internal.url_shortener.storage.database;

import java.net.URL;

import com.example.internal.url_shortener.Storage;
import com.example.url_shortener.UrlShortenerException;

/**
 * Database implementation of the storage.
 */
public class DatabaseStorage implements Storage {

	private static final String TABLE_NAME = "URL_SHORTENER";
	private static final String COLUMN_LONGFORM = "LONG_FORM";
	private static final String COLUMN_SHORTFORM = "SHORT_FORM";
	private static final String COLUMN_INDEX = "INDEX";

	public DatabaseStorage() {
		initialize();
	}

	private void initialize() {
		// TODO create DB connection

		// TODO if table exists then return
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_INDEX + " LONG, " + COLUMN_LONGFORM
				+ " VARCHAR(255), " + COLUMN_SHORTFORM + " VARCHAR(255))";
	}

	@Override
	public URL[] getAliases(URL longForm) throws UrlShortenerException {
		String sql = "SELECT " + COLUMN_SHORTFORM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LONGFORM + "='"
				+ longForm + "'";
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL resolve(URL shortForm) throws UrlShortenerException {
		String sql = "SELECT " + COLUMN_LONGFORM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_SHORTFORM + "='"
				+ shortForm + "'";
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(URL longForm, URL shortForm, long index) throws UrlShortenerException {
		// TODO Auto-generated method stub

	}

	private void internalStore(URL longForm, URL shortForm, long index) throws UrlShortenerException {
		String sql = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_INDEX + ", " + COLUMN_LONGFORM + ", "
				+ COLUMN_SHORTFORM + ") VALUES (" + index + ", " + longForm.toExternalForm() + ", "
				+ shortForm.toExternalForm() + ")";
	}

	@Override
	public void store(URL longForm, URL shortForm) throws UrlShortenerException {
		long index = nextAvailableIndex();
		internalStore(longForm, shortForm, index);
	}

	@Override
	public long nextAvailableIndex() throws UrlShortenerException {
		// TODO Auto-generated method stub
		return 0;
	}

}
