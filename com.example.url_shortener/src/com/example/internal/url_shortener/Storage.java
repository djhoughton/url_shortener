package com.example.internal.url_shortener;

import java.net.URL;

import com.example.url_shortener.UrlShortenerException;

/**
 * Interface used to represent the front end to the storage.
 * 
 * Could be a database, dispatcher, or more.
 */
public interface Storage {

	/**
	 * Total number of allowed entries in the storage.
	 */
	public static final long MAX_SIZE = 100000;

	/**
	 * Return the index associated with the given url.
	 * 
	 * If the url is not found in the storage then return -1.
	 * 
	 * @param url
	 *            the url to check
	 * @return the associated index
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public long getIndex(URL url) throws UrlShortenerException;

	/**
	 * Return the URL associated with the given index.
	 * 
	 * Return <code>null</code> if the index is not found in the storage.
	 * 
	 * @param index
	 *            the index to lookup
	 * @return the associated URL or <code>null</code>
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public URL getUrl(long index) throws UrlShortenerException;

	/**
	 * Store the given URL at the specified index.
	 * 
	 * The index must be unique across the storage. If a different URL is already
	 * associated with this index, an exception will be thrown.
	 * 
	 * Return the index.
	 * 
	 * @param url
	 *            the URL to store
	 * @param index
	 *            the index to use
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public long store(URL url, long index) throws UrlShortenerException;

	/**
	 * Store the given URL in the database.
	 * 
	 * Return the index that will be associated with the URL.
	 * 
	 * @param url
	 *            the URL to store
	 * @return the associated index
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public long store(URL url) throws UrlShortenerException;
}
