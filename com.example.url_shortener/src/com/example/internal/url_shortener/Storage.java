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
	 * Return the list of short form URLs associated with the given long form URL.
	 * If one does not exist then return an empty list.
	 * 
	 * Note: we need to return a list here since it is valid for the user to store
	 * the same long form URL in the storage associated with different aliases.
	 * 
	 * @param url
	 *            the URL to lookup
	 * @return the associated short form URLs
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public URL[] getAliases(URL url) throws UrlShortenerException;

	/**
	 * Return the long form URL associated with the given alias URL. Return
	 * <code>null</code> if one does not exist.
	 * 
	 * @param alias
	 *            the short from URL
	 * @return the associated URL or <code>null</code>
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public URL resolve(URL alias) throws UrlShortenerException;

	/**
	 * Store the given URL at the specified index.
	 * 
	 * The index must be unique across the storage. If a different URL is already
	 * associated with this index, an exception will be thrown.
	 * 
	 * @param url
	 *            the long form URL to store
	 * @param alias
	 *            the short form URL to store
	 * @param index
	 *            the index to use
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public void store(URL url, URL alias, long index) throws UrlShortenerException;

	/**
	 * Store the given URL in the database.
	 * 
	 * @param url
	 *            the long form URL to store
	 * @param alias
	 *            the short form URL to store
	 * @return the associated index
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public void store(URL url, URL alias) throws UrlShortenerException;

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
