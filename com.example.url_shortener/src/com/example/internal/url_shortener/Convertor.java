package com.example.internal.url_shortener;

import java.util.ArrayList;
import java.util.List;

import com.example.url_shortener.UrlShortenerException;

/**
 * Class used to convert a long number to a string alias, and vice versa.
 */
public class Convertor {

	private int base;
	private static List<Character> mappings = new ArrayList<Character>();

	/*
	 * Initialize our mapping table.
	 */
	static {
		for (char c = '0'; c <= '9'; c++) {
			mappings.add(c);
		}
		for (char c = 'a'; c <= 'z'; c++) {
			mappings.add(c);
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			mappings.add(c);
		}
	}

	/*
	 * Return the maximum allowed size for a base.
	 */
	public static int getMaxBase() {
		return mappings.size();
	}

	/*
	 * Create and return a new convertor of the max size.
	 */
	public Convertor() throws UrlShortenerException {
		this(getMaxBase());
	}

	/*
	 * Create and return a new convertor set to the given base.
	 */
	public Convertor(int base) throws UrlShortenerException {
		if (base < 1) {
			throw new UrlShortenerException("Base must be larger than 1. (" + base + ")");
		}
		if (base > mappings.size()) {
			throw new UrlShortenerException("Base cannot be larger than " + mappings.size());
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
		int result = mappings.indexOf(character);
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
			return mappings.get(i);
		} catch (IndexOutOfBoundsException e) {
			throw new UrlShortenerException(e);
		}
	}
}