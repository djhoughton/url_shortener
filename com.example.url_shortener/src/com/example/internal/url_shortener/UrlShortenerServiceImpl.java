package com.example.internal.url_shortener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.example.internal.url_shortener.storage.memory.MemoryStorage;
import com.example.url_shortener.AliasAlreadyExistsException;
import com.example.url_shortener.UrlShortenerException;
import com.example.url_shortener.UrlShortenerService;

/**
 * Implementation of the URL Shortener Service.
 */
public class UrlShortenerServiceImpl implements UrlShortenerService {

	private URL DEFAULT_BASE;
	private Storage storage;
	private Convertor convertor;

	public UrlShortenerServiceImpl() throws UrlShortenerException {
		try {
			DEFAULT_BASE = new URL("http://short/");
		} catch (MalformedURLException e) {
			throw new UrlShortenerException(e);
		}
		convertor = new Convertor();
		storage = new MemoryStorage();
	}

	@Override
	public URL store(URL url) throws UrlShortenerException {
		return store(url, null);
	}

	/*
	 * Build a new URL from the given base and alias.
	 */
	private URL buildUrl(URL base, String alias) throws UrlShortenerException {
		try {
			return new URL(base, alias);
		} catch (MalformedURLException e) {
			throw new UrlShortenerException(e);
		}
	}

	/*
	 * Build a new URL from the given base and index. Convert the index to an alias
	 * first.
	 */
	private URL buildUrl(URL base, long index) throws UrlShortenerException {
		String alias = convertor.indexToAlias(index);
		return buildUrl(base, alias);
	}

	/*
	 * Get the index from the given short form URL. Strip off the common short form
	 * base, get the alias, and convert it to an index.
	 */
	private long getIndex(URL url) throws UrlShortenerException {
		String base = DEFAULT_BASE.toExternalForm();
		String u = url.toExternalForm();
		if (u.startsWith(base)) {
			String alias = u.substring(base.length());
			return convertor.aliasToIndex(alias);
		} else {
			throw new UrlShortenerException("Invalid short form URL: " + url.toExternalForm());
		}
	}

	@Override
	public URL store(URL url, String alias) throws UrlShortenerException {
		// parameter validation
		if (url == null) {
			throw new UrlShortenerException(new IllegalArgumentException("Invalid parameters."));
		}

		// if the user didn't request a specific alias, then just store the URL and
		// return the short form
		if (alias == null) {
			// does it already exist?
			long existing = storage.getIndex(url);
			if (existing != -1) {
				// build a url from the existing index
				return buildUrl(DEFAULT_BASE, existing);
			}
			// otherwise store the URL
			long index = storage.store(url);
			return buildUrl(DEFAULT_BASE, index);
		}

		// otherwise see if the requested short form is available

		// first we check to see if the user gave us something valid
		assertValidAlias(alias);
		long index = convertor.aliasToIndex(alias);
		URL existing = storage.getUrl(index);
		if (existing != null) {
			// it already exist so just return
			URL result = buildUrl(DEFAULT_BASE, alias);
			if (url.equals(existing)) {
				return result;
			}
			// it exists but references a different URL
			throw new AliasAlreadyExistsException("Alias already exists: " + result.toExternalForm());
		}

		index = storage.store(url, index);
		return buildUrl(DEFAULT_BASE, index);
	}

	@Override
	public URL resolve(URL url) throws UrlShortenerException {
		// parameter validation
		if (url == null) {
			throw new UrlShortenerException(new IllegalArgumentException("Null parameter"));
		}

		// strip the alias from the incoming URL
		long index = getIndex(url);

		// perform a lookup in the storage and return what is stored at the index
		return storage.getUrl(index);
	}

	@Override
	public URL getBase() {
		return DEFAULT_BASE;
	}

	/*
	 * Ensure the given alias is valid and throw an exception if it is not.
	 */
	private void assertValidAlias(String alias) throws UrlShortenerException {
		if ((alias == null) || (alias.length() > MAX_ALIAS_LENGTH)) {
			throw new UrlShortenerException(new IllegalArgumentException());
		}
		String valid = getValidAliasCharacters();
		for (int i = 0; i < alias.length(); i++) {
			if (valid.indexOf(alias.charAt(i)) == -1) {
				throw new UrlShortenerException(new IllegalArgumentException());
			}
		}
	}

	@Override
	public String getValidAliasCharacters() {
		return convertor.getValidCharacters();
	}

	class KeyValuePair {
		long index;
		URL url;

		KeyValuePair(long index, URL url) {
			this.index = index;
			this.url = url;
		}

		@Override
		public String toString() {
			return index + "->" + url;
		}
	}

	public Iterator<KeyValuePair> getEntries() {
		return new Iterator<KeyValuePair>() {
			long nextIndex = 0;
			boolean done = false;
			KeyValuePair next = null;

			private void fetchNext() throws UrlShortenerException {
				// no more results
				if (done || next != null) {
					return;
				}

				// get the next entry
				for (long index = nextIndex; index < Storage.MAX_SIZE; index++) {
					URL url = storage.getUrl(index);
					if (url != null) {
						nextIndex = index + 1;
						next = new KeyValuePair(index, url);
						return;
					}
				}

				// no more results
				done = true;
				return;
			}

			@Override
			public boolean hasNext() {
				try {
					fetchNext();
				} catch (UrlShortenerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return next != null;
			}

			@Override
			public KeyValuePair next() {
				try {
					fetchNext();
				} catch (UrlShortenerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (next == null) {
					throw new NoSuchElementException();
				}
				KeyValuePair result = next;
				next = null;
				return result;
			}
		};
	}
}
