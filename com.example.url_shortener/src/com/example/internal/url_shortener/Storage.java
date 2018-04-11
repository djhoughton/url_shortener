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
	 * Return the list of indexes associated with the given URL. If one does not
	 * exist then return an empty list.
	 * 
	 * Note: we need to return a list here since it is valid for the user to store
	 * the same long form URL in the storage associated with different aliases.
	 * 
	 * @param url
	 *            the URL to lookup
	 * @return the associated indexes
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public long[] getIndexes(URL url) throws UrlShortenerException;

	/**
	 * Return the URL associated with the given index. Return <code>null</code> if
	 * one does not exist.
	 * 
	 * @param index
	 *            the index to lookup
	 * @return the associated URL or <code>null</code>
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public URL resolve(long index) throws UrlShortenerException;

	/**
	 * Store the given URL at the specified index.
	 * 
	 * The index must be unique across the storage. If a different URL is already
	 * associated with this index, an exception will be thrown.
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
	 * @param url
	 *            the URL to store
	 * @return the associated index
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public long store(URL url) throws UrlShortenerException;

	/**
	 * Return the next available index.
	 * 
	 * If the storage is full then an exception will be thrown.
	 * 
	 * @return the next available index
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public long nextAvailableIndex() throws UrlShortenerException;
}
