package com.example.url_shortener;

import java.net.URL;

/**
 * A service used to associate a long form URL with a short form one.
 */
public interface UrlShortenerService {

	/**
	 * Constant defining the maximum allowed length for an alias.
	 */
	public static final int MAX_ALIAS_LENGTH = 5;

	/**
	 * Return the list of valid characters which can be used in an alias.
	 * 
	 * @return the list of valid characters
	 */
	public String getValidAliasCharacters();

	/**
	 * Store the given long form URL and return the short form URL equivalent.
	 * 
	 * @param url
	 *            the URL to store
	 * @return the alias for the URL
	 */
	public URL store(URL url) throws UrlShortenerException;

	/**
	 * Store the given URL and associate it with the given alias.
	 * 
	 * If the given alias is <code>null</code> then associate a random alias with
	 * this URL.
	 * 
	 * @param url
	 *            the URL to store
	 * @param alias
	 *            the alias to use, may be <code>null</code>
	 * @return the short form URL
	 * @throws AliasAlreadyExistsException
	 *             if the alias is already associated with another URL
	 * @throws UrlShortenerException
	 *             wrapping an IllegalArgumentException if the alias is invalid
	 */
	public URL store(URL url, String alias) throws UrlShortenerException;

	/**
	 * Return the base URL used for all generated short form URLs.
	 * 
	 * @return the base URL
	 */
	public URL getBase();

	/**
	 * Resolve the given short form URL to its associated long form equivalent.
	 * 
	 * If there is no association then return <code>null</code>.
	 * 
	 * @param alias
	 *            the short form URL
	 * @return the long form URL or <code>null</code>
	 */
	public URL resolve(URL alias) throws UrlShortenerException;
}
