<%@ page import="tagcloud.server.controller.CrawlController" %>
<%@ page import="java.util.List,java.util.ArrayList,java.util.Iterator" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>


<%
	// collect information and start CrawlController 
	String hostname = request.getParameter("crawl_hostname");
	String type = request.getParameter("crawl_type");

	CrawlController ccntl = new CrawlController(response, hostname, type);

	// redirect to tagCloud
	//response.sendRedirect("../tagcloud/?host=" + hostname);

%>

<!-- <!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/crawler.css" />

	<title>IP6 Bachelor Thesis: TagCloud mit Elasticsearch</title>
</head>
<body>
	<div id="form-main">
		
		<div id="logowrapper">
			<img id="logo" src="${pageContext.request.contextPath}/assets/logo.png"/>
		</div>

		<div id="box">
			<h1>Crawler</h1>
			<h3>Index erstellt</h3>
			<p>Datensammlung abgeschlossen.</p>
		</div>
	</div>
</body>
</html>
-->