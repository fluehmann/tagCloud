<%@ page import="tagcloud.server.controller.CrawlController" %>
<%@ page import="java.util.List,java.util.ArrayList,java.util.Iterator" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%
	String hostname = request.getParameter("crawl_hostname");

	CrawlController ccntl = new CrawlController();
	ccntl.crawl(hostname);
	response.sendRedirect("tagcloud.jsp?host=" + hostname);
%>