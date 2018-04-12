package com.example.internal.url_shortener.storage.memory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.example.url_shortener.AliasAlreadyExistsException;

/**
 * This class represents the equivalent of a table in a relational database.
 * 
 * It stores a Long associated with a URL.
 */
public class Table {

	private Map<Long, URL> data = new HashMap<Long, URL>();

	/**
	 * Return the data associated with this index or <code>null</code> if one does
	 * not exist.
	 * 
	 * SQL equivalent: SELECT URL FROM TABLE WHERE INDEX=index;
	 * 
	 * @param index
	 *            the index
	 * @return the associated URL or <code>null</code>
	 */
	public URL getUrl(long index) {
		return data.get(index);
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
		return data.get(index) != null;
	}

	/**
	 * Return the index of the url in the table. Return -1 if the url does not
	 * exist.
	 * 
	 * SQL equivalent: SELECT INDEX FROM TABLE WHERE URL='url';
	 * 
	 * @param url
	 *            the url
	 * @return the index for the url or -1
	 */
	public long getIndex(URL url) {
		for (Map.Entry<Long, URL> entry : data.entrySet()) {
			if (entry.getValue().equals(url)) {
				return entry.getKey().longValue();
			}
		}
		return -1;
	}

	/**
	 * Store the given URL at the specified index.
	 * 
	 * If there is already a URL stored at this index, an exception will be thrown.
	 * 
	 * @param url
	 *            the url to store
	 * @param index
	 *            the index to use
	 * @param index
	 *            the index where the URL is stored
	 * @throws AliasAlreadyExistsException
	 *             if a URL is already stored at this index
	 */
	public long put(URL url, long index) throws AliasAlreadyExistsException {
		URL existing = getUrl(index);
		if (existing != null) {
			if (existing.equals(url)) {
				return index;
			}
			// another URL is stored at this index
			throw new AliasAlreadyExistsException("Alias already exists: " + index);
		}
		data.put(index, url);
		return index;
	}
}
