<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.CrawlController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="java.util.List,java.util.Hashtable"%>

<%
	String hostname = request.getParameter("host");

	RetrieveController rcntrl = new RetrieveController();
	ArrayList<String> al = rcntrl.getIndeces();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
		out.print("<li><a class='blue folder' href='../tagcloud/index.jsp?host=" + index + "' target='_blank'>" + index + "</a></li>");
	}
  %>
	</ul>
  
</div>
</body>
</html>