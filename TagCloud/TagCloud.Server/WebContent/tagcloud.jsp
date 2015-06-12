<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.CrawlController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.Iterator"%>

<%
	String hostname = request.getParameter("host");

	RetrieveController rcntrl = new RetrieveController();
	ArrayList<String> al = rcntrl.get(hostname);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
<title>Insert title here</title>
</head>
<body>
<h1><% out.print("Gefundene URLs zu " + hostname); %></h1>
	<%
		for (String item : al) {
			out.print(item);
			out.print("<br />");
		}
	%>
</body>
</html>