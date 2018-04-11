package com.example.internal.url_shortener.storage.memory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.url_shortener.AliasAlreadyExistsException;

/**
 * This class represents the equivalent of a table in a relational database.
 * 
 * It stores a Long associated with a URL.
 */
public class Table {

	private List<Long> indexes = new ArrayList<Long>();
	private List<URL> urls = new ArrayList<URL>();

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
		int i = indexes.indexOf(index);
		// not found
		if (i == -1) {
			return null;
		}
		return urls.get(i);
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
	 * SQL equivalent: SELECT INDEX FROM TABLE WHERE URL='url';
	 * 
	 * @param url
	 *            the url
	 * @return the list of indexes
	 */
	public long[] getIndexes(URL url) {
		List<Long> result = new ArrayList<Long>();
		for (int i = 0; i < urls.size(); i++) {
			URL longForm = urls.get(i);
			if (longForm.equals(url)) {
				result.add(indexes.get(i));
			}
		}
		return result.stream().mapToLong(l -> l).toArray();
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
		indexes.add(index);
		urls.add(url);
		return index;
	}
}
