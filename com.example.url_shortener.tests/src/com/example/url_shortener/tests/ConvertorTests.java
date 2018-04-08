package com.example.url_shortener.tests;

import com.example.internal.url_shortener.Convertor;
import com.example.url_shortener.UrlShortenerException;

import junit.framework.TestCase;

public class ConvertorTests extends TestCase {

	/*
	 * Some basic tests.
	 */
	public void test_basic() throws Exception {
		long maxNum = 10000;
		for (int base = 2; base <= Convertor.getMaxBase(); base++) {
			Convertor convertor = new Convertor(base);
			for (long num = 0; num < maxNum; num++) {
				String shortForm = convertor.indexToAlias(num);
				long longForm = convertor.aliasToIndex(shortForm);
				assertEquals(num + " != " + longForm, num, longForm);

				// if the base is less than Character.MAX_RADIX then double check
				String radixConversion = null;
				if (base <= Character.MAX_RADIX) {
					radixConversion = Long.toString(num, (int) base);
					assertEquals(shortForm + " != " + radixConversion, shortForm, radixConversion);
				}
				// System.out.println(num + " -> " + shortForm + " -> " + longForm
				// + (radixConversion == null ? "" : " -> " + radixConversion));
			}
		}
	}

	/*
	 * Test some error cases.
	 */
	public void test_errors() throws Exception {
		Convertor convertor;
		// try to have a base which is too large
		try {
			convertor = new Convertor(Convertor.getMaxBase() + 1);
			fail("Should fail because base is too large.");
		} catch (UrlShortenerException e) {
			// expected
		}

		// invalid characters in the alias
		convertor = new Convertor();
		try {
			// TODO find a better invalid char by getting the list of valid characters and
			// making sure we have something not in the list
			StringBuffer buffer = new StringBuffer();
			for (char c = '!'; c <= '~'; c++) {
				buffer.append(c);
			}
			convertor.aliasToIndex(buffer.toString());
		} catch (UrlShortenerException e) {
			// expected
		}
	}
}
