<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.CrawlController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.Iterator"%>

<%
	String hostname = request.getParameter("crawl_hostname");

	CrawlController ccntl = new CrawlController();
	ccntl.crawl(hostname);
	response.sendRedirect("tagcloud.jsp?host="+hostname);
	
// 	RetrieveController rcntrl = new RetrieveController();
// 	// testindexsimon
// 	ArrayList<String> al = rcntrl.get(hostname);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
// 		for (String item : al) {
// 			out.print(item);
// 			out.print("<br />");
// 		}
	%>
</body>
</html>