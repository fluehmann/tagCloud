<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.CrawlController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="java.util.List,java.util.Hashtable"%>

<%
	String hostname = request.getParameter("host");
  String keyword = request.getParameter("keyword");

	RetrieveController rcntrl = new RetrieveController();
	ArrayList<Hashtable<String, String>> al = rcntrl.get(hostname, keyword);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jqcloud.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>

<title>Insert title here</title>
</head>
<body>
<!-- Begin Sidebar -->
<%@include file="../sidebar.jsp" %>
<!-- End Sidebar -->
<div class="content">
	<h1>
		<% out.print("Gefundene URLs zu " + keyword); %>
	</h1>

	<div>
		<ol>
		<% for (Hashtable<String, String> item : al) {
		 			String link = item.get("url");
		 			String content = item.get("highlight");
		%>
			<li>
				<a href="<% out.print(link); %>" target="_blank"><% out.print(link); %></a>
				<p><% out.print(content); %></p>
			</li>
		<% 
			 }
		%>
		</ol>
	</div>
</div>
</body>
</html>