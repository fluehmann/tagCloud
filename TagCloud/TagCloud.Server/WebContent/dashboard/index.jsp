<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.CrawlController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="java.util.List,java.util.Hashtable"%>


<!DOCTYPE html>
<html>
<head>
<title>Bachelor Thesis: Tagcloud mit Elasticsearch</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css" />
</head>
<body>
<!-- Begin Sidebar -->
<%@include file="../sidebar.jsp" %>
<!-- End Sidebar -->
<div class="content">
<h1>TagCloud mit ElasticSearch</h1>
<o>Bachelor Thesis<br>
Thien-An Bui<br>
Simon Flühmann</p>
<p>&copy; Fachhochschule Nordwestschweiz FHNW, 2015</p>
<p>
	<a href="http://www.fhnw.ch" target="_blank">www.fhnw.ch</a>
</p>
<p>
	<img id="logo" width="250" src="${pageContext.request.contextPath}/assets/logo.png"/>
</p>
</div>
</div>
</body>
</html>