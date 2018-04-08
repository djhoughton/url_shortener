package com.example.internal.url_shortener.storage.memory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.internal.url_shortener.Storage;
import com.example.internal.url_shortener.storage.StoredData;
import com.example.url_shortener.UrlShortenerException;

/**
 * An example implementation where the data is stored in memory.
 */
public class MemoryStorage implements Storage {

	// the list of tables - will be more than one if we page large numbers of
	// entries
	private List<Table> tables = new ArrayList<Table>();

	// use this to record the last index so it is quicker when searching for a new
	// empty one
	private long lastCreate = 0;

	public MemoryStorage() {
		tables.add(new Table());
	}

	private Table getTableForIndex(long index) {
		// TODO partition - this is where we would split the data into multiple tables
		return tables.get(0);
	}

	public long nextAvailableIndex() throws UrlShortenerException {
		// TODO this is ugly - is there an easier way to find the next available index
		// in the storage? It really depends on how the tables are split up.
		for (long i = lastCreate; i < MAX_SIZE; i++) {
			boolean found = false;
			for (Table table : tables) {
				if (table.containsIndex(i)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return i;
			}
		}
		throw new UrlShortenerException("Database full.");
	}

	@Override
	public void store(URL longForm, URL shortForm, long index) throws UrlShortenerException {
		internalStore(longForm, shortForm, index);
	}

	private void internalStore(URL longForm, URL shortForm, long index) throws UrlShortenerException {
		Table table = getTableForIndex(index);
		// will throw an exception if it already exists under another URL
		table.put(longForm, shortForm, index);
	}

	@Override
	public URL resolve(URL shortForm) throws UrlShortenerException {
		// TODO need a better way to than searching across all tables
		for (Table t : tables) {
			StoredData data = t.getFromShortForm(shortForm);
			if (data != null) {
				return data.longForm;
			}
		}
		return null;
	}

	@Override
	public URL[] getAliases(URL longForm) throws UrlShortenerException {
		// TODO need a better way to than searching across all tables
		List<URL> result = new ArrayList<URL>();
		for (Table t : tables) {
			for (StoredData data : t.getFromLongForm(longForm)) {
				result.add(data.shortForm);
			}
		}
		return result.toArray(new URL[result.size()]);
	}

	@Override
	public void store(URL longForm, URL shortForm) throws UrlShortenerException {
		long index = nextAvailableIndex();
		internalStore(longForm, shortForm, index);
		// record the index so we don't have to start from the beginning next time we
		// search for an unused one
		lastCreate = index;
	}
}
