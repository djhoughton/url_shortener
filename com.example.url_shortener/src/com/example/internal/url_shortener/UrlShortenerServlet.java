package com.example.internal.url_shortener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.internal.url_shortener.UrlShortenerServiceImpl.KeyValuePair;
import com.example.url_shortener.UrlShortenerException;

/**
 * Servlet class.
 */
public class UrlShortenerServlet extends HttpServlet {

	private static final String ADD = "add";
	private static final String ALIAS = "alias";
	private static final String RESOLVE = "resolve";
	private static final long serialVersionUID = 4027457434371822868L;
	private UrlShortenerServiceImpl service;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		internalExecute(request, response);
	}

	private void internalExecute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		try {
			writer.print("<html><head><title>URL Shortener</title></head><body>");
			writer.println("<a href=\"http://localhost:8080/url_shortener/url_shortener\">Home</a><br>");
			performAction(writer, request);
			printActions(writer);
			printRequest(writer, request);
			printDatabase(writer);
			writer.println("</body></html>");
		} catch (Exception e) {
			error(writer, e);
			// writer.println("<pre>");
			// e.printStackTrace(writer);
			// writer.println("</pre>");
		} finally {
			writer.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		internalExecute(request, response);
	}

	private void performAction(PrintWriter writer, HttpServletRequest request) throws Exception {
		String url = request.getParameter(ADD);
		if (url != null) {
			String alias = request.getParameter(ALIAS);
			if (alias == null || alias.isEmpty()) {
				alias = null;
			}
			writer.println("<h1>Previous</h1>");
			writer.println("Added: " + url + " -&gt; " + add(url, alias));
		}
		url = request.getParameter(RESOLVE);
		if (url != null) {
			writer.println("<h1>Previous</h1>");
			writer.println("Resolved: " + url + " -&gt; " + resolve(url));
		}
	}

	private URL add(String url, String alias) throws Exception, UrlShortenerException {
		return service.store(new URL(url), alias);
	}

	private URL resolve(String url) throws Exception {
		return service.resolve(new URL(url));
	}

	private void printActions(PrintWriter writer) {
		writer.println("<h1>Actions</h1>");
		writer.println(form("Add:", ADD, true));
		writer.println(form("Resolve:", RESOLVE, false));
	}

	private String form(String label, String action, boolean alias) {
		return "<form method=\"post\"><input type=\"submit\" value=\"" + label + "\"><input type=\"text\" name=\""
				+ action + "\">" + (alias ? "<input type=\"text\" name=\"alias\">" : "") + "</form>";
	}

	private UrlShortenerServiceImpl getService() throws UrlShortenerException {
		if (service == null) {
			service = new UrlShortenerServiceImpl();
		}
		return service;
	}

	private void popup(PrintWriter writer, String message) {
		writer.print("<script>window.alert(\"");
		writer.print(message);
		writer.println("\");</script>");
	}

	private void error(PrintWriter writer, Exception e) {
		popup(writer, e.getMessage());
	}

	private void printRequest(PrintWriter writer, HttpServletRequest request) throws IOException {
		writer.println("<h1>Request</h1>");
		// writer.println("Request URI: " + request.getRequestURI() + "<br>");
		// writer.println("Query String: " + request.getQueryString() + "<br>");

		writer.println("<table border=1>");

		writer.println("<tr><td>");
		writer.println("<h2>Parameters:</h2>");
		Map<String, String> params = new TreeMap<String, String>();
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String name = e.nextElement();
			params.put(name, request.getParameter(name));
		}
		writer.println("<code>");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			writer.print("<b>" + entry.getKey() + "=</b>");
			writer.println(entry.getValue() + "<br>");
		}
		writer.println("</code>");
		writer.println("</tr></td>");

		writer.println("<tr><td>");
		writer.println("<h2>Headers:</h2>");
		Map<String, String> headers = new TreeMap<String, String>();
		for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
			String header = e.nextElement();
			headers.put(header, request.getHeader(header));
		}
		writer.println("<code>");
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			writer.print("<b>" + entry.getKey() + "=</b>");
			writer.println(entry.getValue() + "<br>");
		}
		writer.println("</code>");
		writer.println("</tr></td>");

		writer.println("<tr><td>");
		writer.println("<h2>Body:</h2>");
		writer.println("<code>");
		BufferedReader reader = request.getReader();
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			writer.println(reader.readLine());
		}
		writer.println("</code>");
		writer.println("</tr></td>");

		writer.println("<table>");
	}

	private void printDatabase(PrintWriter writer) throws UrlShortenerException {
		writer.println("<h1>Database Contents</h1>");
		writer.println("<table>");
		for (Iterator<KeyValuePair> iter = getService().getEntries(); iter.hasNext();) {
			KeyValuePair entry = iter.next();
			writer.println("<tr><td>");
			writer.println(entry.index);
			// writer.println("</td><td>-&gt;</td><td>");
			// writer.println(service.store(entry.url));
			writer.println("</td><td>-&gt;</td><td>");
			writer.println(entry.url);
			writer.println("</td></tr>");
		}
		writer.println("</table>");
	}
}
