package com.example.internal.url_shortener.storage;

import java.net.URL;

/**
 * Class represents a row in the table.
 * 
 */
public class StoredData {
	public long index;
	public URL shortForm;
	public URL longForm;

	public StoredData(long index, URL shortForm, URL longForm) {
		this.index = index;
		this.shortForm = shortForm;
		this.longForm = longForm;
	}
}