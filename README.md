# url_shortener
Sample implementation of a URL Shortener service

Main API is in UrlShortenerService.

Requirements:
- store a URL and return a short form
- allow the user to specify the short form if they want
- given a short form URL, return its associated long form

TODO:
- concurrency and locking
- address scalability and performance issues
- ensure error and corner cases are handled
- provide sample file-system and database implementations
- expand the allowable alias characters to include all valid URL characters
- provide a better way to set up config options
	- base URL
	- determine how the user chooses the storage implmentation
	- file-system location
	- database settings

