package com.example.internal.url_shortener.storage.memory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.internal.url_shortener.storage.StoredData;
import com.example.url_shortener.AliasAlreadyExistsException;
import com.example.url_shortener.UrlShortenerException;

/**
 * This class represents the equivalent of a table in a relational database.
 * 
 * It stores a Long associated with a URL.
 */
public class Table {

	private List<Long> indexes = new ArrayList<Long>();
	private List<URL> shortForms = new ArrayList<URL>();
	private List<URL> longForms = new ArrayList<URL>();

	/**
	 * Return the data associated with this index or <code>null</code> if one does
	 * not exist.
	 * 
	 * SQL equivalent: SELECT * FROM TABLE WHERE INDEX=index;
	 * 
	 * @param index
	 *            the index
	 * @return the associated URL or <code>null</code>
	 */
	private StoredData getDataAtIndex(long index) {
		int i = indexes.indexOf(index);
		// not found
		if (i == -1) {
			return null;
		}
		return new StoredData(index, shortForms.get(i), longForms.get(i));
	}

	/**
	 * Return a boolean value indicating whether or not the given index is stored in
	 * the table.
	 * 
	 * @param index
	 *            the index
	 * @return <code>true</code> if the table contains the index and
	 *         <code>false</code> otherwise
	 */
	public boolean containsIndex(long index) {
		return indexes.contains(index);
	}

	/**
	 * Return the list data objects in the table associated with the given long form
	 * URL or an empty list if it does not exist.
	 * 
	 * SQL equivalent: SELECT * FROM TABLE WHERE LONG_FORM='url';
	 * 
	 * @param url
	 *            the url
	 * @return the list of data
	 */
	public StoredData[] getFromLongForm(URL url) {
		List<StoredData> result = new ArrayList<StoredData>();
		for (int i = 0; i < longForms.size(); i++) {
			URL longForm = longForms.get(i);
			if (longForm.equals(url)) {
				result.add(new StoredData(indexes.get(i), shortForms.get(i), longForms.get(i)));
			}
		}
		return result.toArray(new StoredData[result.size()]);
	}

	/**
	 * Return the data in the table associated with the given short form URL or
	 * <code>null</code> if it does not exist.
	 * 
	 * SQL equivalent: SELECT * FROM TABLE WHERE SHORT_FORM='url';
	 * 
	 * @param url
	 *            the short for URL to search for
	 * @return the associated long for URL
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public StoredData getFromShortForm(URL url) {
		int i = shortForms.indexOf(url);
		// not found
		if (i == -1) {
			return null;
		}
		return new StoredData(indexes.get(i), shortForms.get(i), longForms.get(i));
	}

	/**
	 * Store the given URL at the specified index.
	 * 
	 * If there is already a URL stored at this index, an exception will be thrown.
	 * 
	 * @param longForm
	 *            the long form url
	 * @param shortForm
	 *            the short form URL
	 * @param index
	 *            the index to use
	 * @throws AliasAlreadyExistsException
	 *             if a URL is already stored at this index
	 */
	public void put(URL longForm, URL shortForm, long index) throws AliasAlreadyExistsException {
		StoredData existing = getDataAtIndex(index);
		if (existing != null) {
			if (existing.longForm.equals(longForm) && existing.shortForm.equals(shortForm)) {
				return;
			}
			// another URL is stored at this index
			throw new AliasAlreadyExistsException("Alias already exists: " + index);
		}
		indexes.add(index);
		longForms.add(longForm);
		shortForms.add(shortForm);
	}
}
