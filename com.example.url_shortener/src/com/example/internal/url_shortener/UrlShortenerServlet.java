package com.example.internal.url_shortener;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet class.
 */
public class UrlShortenerServlet extends HttpServlet {

	private static final long serialVersionUID = 4027457434371822868L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (PrintWriter stream = response.getWriter()) {
			stream.print("<html><head></head><body><h1>hi, ");
			stream.print(request.getParameter("name"));
			stream.println("!</h1></body></html>");
		}
	}
}
