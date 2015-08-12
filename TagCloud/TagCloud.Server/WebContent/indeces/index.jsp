<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.CrawlController"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="java.util.List,java.util.Hashtable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	//String hostname = request.getParameter("host");

	RetrieveController rcntrl = new RetrieveController();
	ArrayList<String> al = rcntrl.getIndeces();
	ArrayList<String> al2 = rcntrl.getHostnames();
%>
<!DOCTYPE html>
<html>
<head>
<title>Hostnames | Tagcloud mit Elasticsearch</title>
<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css" />
</head>
<body>
<!-- Begin Sidebar -->
<%@include file="../sidebar.jsp" %>
<!-- End Sidebar -->
<div class="content">

	<ul class="sticker-tiles">
	<% 
	for(String index : al){
		//out.print("<li><a class='blue folder' href='../tagcloud/index.jsp?host=" + index + "' target='_blank'>" + index + "</a></li>");
	}
  %>
	</ul>
	
	
	<ul class="sticker-tiles">
	<% 
	for(String hostname : al2){
		out.print("<li><a class='blue folder' href='../tagcloud/index.jsp?host=" + hostname + "' target='_blank'>" + hostname + "</a></li>");
	}
  %>
	</ul>
  
</div>
</body>
</html>