package com.example.internal.url_shortener;

import java.net.MalformedURLException;
import java.net.URL;

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
	public URL store(URL longForm) throws UrlShortenerException {
		return store(longForm, null);
	}

	private URL buildUrl(URL base, String alias) throws UrlShortenerException {
		try {
			return new URL(base, alias);
		} catch (MalformedURLException e) {
			throw new UrlShortenerException(e);
		}
	}

	@Override
	public URL store(URL longForm, String alias) throws UrlShortenerException {
		// parameter validation
		if (longForm == null) {
			throw new UrlShortenerException(new IllegalArgumentException("Invalid parameters."));
		}

		// if the user didn't request a specific alias, then just store the URL and
		// return the short form
		if (alias == null) {
			// does it already exist?
			URL[] existing = storage.getAliases(longForm);
			if (existing.length > 0) {
				// might have multiple matches but just return the first one
				return existing[0];
			}
			// otherwise store the URL
			long index = storage.nextAvailableIndex();
			String post = convertor.indexToAlias(index);
			URL shortForm = buildUrl(DEFAULT_BASE, post);
			storage.store(longForm, shortForm, index);
			return shortForm;
		}

		// otherwise see if the requested short form is available.
		// first we check to see if the user gave us something valid
		assertValidAlias(alias);
		URL shortForm = buildUrl(DEFAULT_BASE, alias);
		URL existing = storage.resolve(shortForm);
		if (existing != null) {
			// it already exist so just return
			if (longForm.equals(existing)) {
				return shortForm;
			}
			// it exists but references a different URL
			throw new AliasAlreadyExistsException("Alias already exists: " + shortForm.toExternalForm());
		}

		long index = storage.nextAvailableIndex();
		storage.store(longForm, shortForm, index);
		return shortForm;
	}

	@Override
	public URL resolve(URL shortForm) throws UrlShortenerException {
		// parameter validation
		if (shortForm == null) {
			throw new UrlShortenerException(new IllegalArgumentException("Null parameter"));
		}

		// // strip the alias from the incoming URL
		// String alias = getAlias(shortForm);
		//
		// // convert the alias to an index
		// long index = convertor.aliasToIndex(alias);

		// perform a lookup in the storage and return what is stored at the index
		return storage.resolve(shortForm);
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
}
