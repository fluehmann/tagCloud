<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.CrawlController"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="java.util.List,java.util.Hashtable"%>
<%@ page import="java.net.URLDecoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");
	String hostname = request.getParameter("host");
  String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");

	RetrieveController rcntrl = new RetrieveController();
	ArrayList<Hashtable<String, String>> al = rcntrl.get(hostname, keyword);
%>
<!DOCTYPE html>
<html>
<head>
<title>Links zu "<% out.print(keyword); %>" auf <% out.print(hostname); %> | Tagcloud mit Elasticsearch</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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