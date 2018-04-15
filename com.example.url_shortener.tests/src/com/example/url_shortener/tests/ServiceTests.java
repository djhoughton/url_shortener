package com.example.url_shortener.tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.example.internal.url_shortener.Storage;
import com.example.internal.url_shortener.UrlShortenerServiceImpl;
import com.example.internal.url_shortener.UrlShortenerServiceImpl.KeyValuePair;
import com.example.url_shortener.AliasAlreadyExistsException;
import com.example.url_shortener.UrlShortenerException;
import com.example.url_shortener.UrlShortenerService;

import junit.framework.TestCase;

/*
 * Tests for the URL Shortener service.
 */
public class ServiceTests extends TestCase {

	private UrlShortenerService service;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.service = new UrlShortenerServiceImpl();
	}

	@Override
	protected void tearDown() throws Exception {
		this.service = null;
		super.tearDown();
	}

	/*
	 * Basic tests. Create some URLs, store them, and then retrieve them via their
	 * alias.
	 */
	public void test_basic() throws Exception {
		// create the URLs to store
		List<URL> data = new ArrayList<URL>();
		for (int i = 0; i < 500; i++) {
			data.add(getRandomURL());
		}

		URL u = getRandomURL();
		URL shortForm = service.store(u, "abc");
		assertEquals(u, service.resolve(shortForm));

		for (URL url : data) {
			// store them and then look them up by their alias
			URL alias = service.store(url);
			URL longForm = service.resolve(alias);
			assertEquals(url, longForm);

			// try storing again - should return the same alias
			assertEquals(alias, service.store(url));

			// calculate the short alias the service used
			String base = service.getBase().toExternalForm();
			String externalForm = alias.toExternalForm();
			assertTrue(externalForm.startsWith(base));
			String same = externalForm.substring(base.length());

			// try storing another URL at the same location
			try {
				service.store(getRandomURL(), same);
				fail("Should not be able to store 2 URLs at the same alias");
			} catch (AliasAlreadyExistsException e) {
				// expected
			}
		}
	}

	public void test_iterator() throws Exception {
		List<URL> urls = new ArrayList<URL>();

		URL url = getRandomURL();
		urls.add(url);
		service.store(url);

		url = getRandomURL();
		urls.add(url);
		service.store(url, "abc");

		url = new URL("http://tsn.ca");
		urls.add(url);
		service.store(url, "tsn");

		int count = 0;
		for (Iterator<KeyValuePair> iter = ((UrlShortenerServiceImpl) service).getEntries(); iter.hasNext();) {
			assertTrue(urls.contains(iter.next().url));
			count++;
		}
		assertEquals(urls.size(), count);
	}

	/*
	 * Return a random URL for testing purposes.
	 */
	private URL getRandomURL() throws MalformedURLException {
		return new URL("http://foo.com/" + UUID.randomUUID() + "/" + UUID.randomUUID());
	}

	private URL getRandomAlias(URL base) throws MalformedURLException {
		return new URL(base, "abc");
	}

	/*
	 * Storing the same URL twice should give you the same alias.
	 */
	public void test_shared() throws Exception {
		URL url = getRandomURL();
		URL shortForm = service.store(url);
		URL shortForm2 = service.store(url);
		assertEquals(shortForm, shortForm2);
	}

	/*
	 * Test that we get an error when the storage reaches the maximum number of
	 * allowed entries.
	 * 
	 * TODO: note this is disabled as it will take a long time to run so we don't
	 * want to do it every time.
	 */
	public void _test_max() throws Exception {
		for (int i = 0; i < Storage.MAX_SIZE; i++) {
			service.store(getRandomURL());
		}
		// one more should fail
		try {
			service.store(getRandomURL());
			fail("Database should be full.");
		} catch (UrlShortenerException e) {
			// expected
		}
	}

	/*
	 * Test some error conditions.
	 */
	public void test_errors() throws Exception {
		// should be null if we haven't stored anything yet
		assertNull(service.resolve(getRandomAlias(service.getBase())));

		// invalid null parameter
		try {
			service.resolve(null);
			fail("Should fail with null parameter.");
		} catch (UrlShortenerException e) {
			// expected
		}

		// invalid null parameter
		try {
			service.store(null);
			fail("Invalid null parameter");
		} catch (UrlShortenerException e) {
			// expected
		}

		// invalid null parameter
		try {
			service.store(null, UUID.randomUUID().toString());
			fail("Invalid null parameter");
		} catch (UrlShortenerException e) {
			// expected
		}
	}

	/*
	 * Test invalid alias arguments.
	 */
	public void test_invalid_alias() throws Exception {
		String valid = service.getValidAliasCharacters();
		String invalidAlias = null;
		for (char c = '!'; c <= '~'; c++) {
			if (valid.indexOf(c) == -1) {
				invalidAlias = Character.toString(c);
			}
		}
		// if we hit this then we need to adjust our formula for finding an invalid
		// character
		assertNotNull(invalidAlias);

		try {
			service.store(getRandomURL(), invalidAlias);
			fail("Should have failed with an invalid alias.");
		} catch (UrlShortenerException e) {
			// expected
		}
	}
}
