package com.example.internal.url_shortener.storage.memory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.internal.url_shortener.Storage;
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
	public long store(URL url, long index) throws UrlShortenerException {
		return internalStore(url, index);
	}

	private long internalStore(URL url, long index) throws UrlShortenerException {
		Table table = getTableForIndex(index);
		// will throw an exception if it already exists under another URL
		return table.put(url, index);
	}

	@Override
	public URL resolve(long index) throws UrlShortenerException {
		// TODO need a better way to than searching across all tables
		for (Table t : tables) {
			URL url = t.getUrl(index);
			if (url != null) {
				return url;
			}
		}
		return null;
	}

	@Override
	public long getIndex(URL url) throws UrlShortenerException {
		// TODO need a better way to than searching across all tables
		for (Table t : tables) {
			long index = t.getIndex(url);
			if (index != -1) {
				return index;
			}
		}
		return -1;
	}

	@Override
	public long store(URL url) throws UrlShortenerException {
		long index = nextAvailableIndex();
		internalStore(url, index);
		// record the index so we don't have to start from the beginning next time we
		// search for an unused one
		lastCreate = index;
		return index;
	}
}
