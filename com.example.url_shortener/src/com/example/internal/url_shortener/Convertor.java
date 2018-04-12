package com.example.internal.url_shortener;

import java.util.ArrayList;
import java.util.List;

import com.example.url_shortener.UrlShortenerException;

/**
 * Class used to convert a long number to a string alias, and vice versa.
 */
public class Convertor {

	/*
	 * Hard-coded to be the size of the mappings table.
	 */
	public static final int MAX_BASE = 62;

	private int base;
	private List<Character> mappings;

	/*
	 * Initialize our mapping table.
	 */
	private void initialize() {
		mappings = new ArrayList<Character>();
		for (char c = '0'; c <= '9'; c++) {
			mappings.add(c);
		}
		for (char c = 'a'; c <= 'z'; c++) {
			mappings.add(c);
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			mappings.add(c);
		}
		// double-check the max size
		if (mappings.size() != MAX_BASE) {
			throw new IllegalStateException();
		}
	}

	/*
	 * Lazy-initialize mappings list.
	 */
	private List<Character> getMappings() {
		if (mappings == null) {
			initialize();
		}
		return mappings;
	}

	/*
	 * Create and return a new convertor of the max size.
	 */
	public Convertor() throws UrlShortenerException {
		this.base = MAX_BASE;
	}

	/*
	 * Create and return a new convertor set to the given base.
	 */
	public Convertor(int base) throws UrlShortenerException {
		if (base < 1) {
			throw new UrlShortenerException("Base must be larger than 1. (" + base + ")");
		}
		if (base > MAX_BASE) {
			throw new UrlShortenerException("Base cannot be larger than " + MAX_BASE);
		}
		this.base = base;
	}

	/*
	 * Return the list of valid character that can be used in an alias.
	 */
	public String getValidCharacters() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < base; i++) {
			try {
				buffer.append(getChar(i));
			} catch (UrlShortenerException e) {
				// shouldn't happen
				throw new IllegalStateException();
			}
		}
		return buffer.toString();
	}

	/**
	 * Convert the given number to an alias and return it.
	 * 
	 * @param num
	 *            the number to convert
	 * @return the alias
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public String indexToAlias(long num) throws UrlShortenerException {
		StringBuffer s = new StringBuffer();
		while (true) {
			long mod = num % base;
			s.append(getChar((int) mod));
			long div = num / base;
			if (div == 0) {
				break;
			} else if (div < base) {
				s.append(getChar((int) div));
				break;
			} else {
				num = div;
			}
		}
		return s.reverse().toString();
	}

	/**
	 * Convert the given alias to a number and return it.
	 * 
	 * @param alias
	 *            the alias
	 * @return the number
	 * @throws UrlShortenerException
	 *             if an error occurred
	 */
	public long aliasToIndex(String alias) throws UrlShortenerException {
		long sum = 0;
		int power = 0;
		for (int i = alias.length() - 1; i >= 0; i--) {
			int j = getInt(Character.valueOf(alias.charAt(i)));
			sum += j * Math.pow(base, power);
			power++;
		}
		return sum;
	}

	/*
	 * Return the integer mapping of the given character or throw an exception if it
	 * is invalid.
	 */
	private int getInt(Character character) throws UrlShortenerException {
		int result = getMappings().indexOf(character);
		if (result == -1) {
			throw new UrlShortenerException("Unknown character in alias: " + character);
		}
		return result;
	}

	/*
	 * Return the character associated with the given number or throw an exception
	 * if it is invalid.
	 */
	private Character getChar(int i) throws UrlShortenerException {
		try {
			return getMappings().get(i);
		} catch (IndexOutOfBoundsException e) {
			throw new UrlShortenerException(e);
		}
	}
}