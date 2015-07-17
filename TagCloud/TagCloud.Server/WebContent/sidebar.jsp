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
<aside class="sidebar">
  <ul class="no-list sb_top_lv">
    <li><a>
        <div class="icon"><i class="fa fa-dashboard"></i></div><span>Home</span></a>
    </li>
    <li><a href="index.jsp">
        <div class="icon"><i class="fa fa-download"></i></div><span>Crawl</span></a></li>
    <li><a>
        <div class="icon"><i class="fa fa-database"></i></div><span>Show Indeces</span></a></li>
    <li><a>
        <div class="icon"><i class="fa fa-tasks"></i></div><span>Status</span></a></li>
    <li><a>
        <div class="icon"><i class="fa fa-lock"></i></div><span>Log Out</span></a></li>
  </ul>
</aside>
<content>

	<ul class="sticker-tiles">
	<% 
	for(String index : al){
		out.print("<li><a class='blue folder' href='tagcloud.jsp?host=" + index + "' target='_blank'>" + index + "</a></li>");
	}
  %>
	</ul>
  
</content>
</body>
</html>