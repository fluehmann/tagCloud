<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.CrawlController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="java.util.List,java.util.Hashtable"%>

<%
	String hostname = request.getParameter("host");

	RetrieveController rcntrl = new RetrieveController();
	ArrayList<Hashtable<String, String>> al = rcntrl.getSigTerms(hostname);
	//rcntrl.getSigTerms(hostname);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jqcloud.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqcloud.min.js"></script>

<title>Insert title here</title>
</head>
<body>
<!-- Begin Sidebar -->
<%@include file="../sidebar.jsp" %>
<!-- End Sidebar -->
<content>
	<h1>
		<% out.print("Gefundene URLs zu " + hostname); %>
	</h1>

	<div id="cloudtest">
	</div>
	<div id="cloud">
		<% rcntrl.getSigTerms(hostname); %>
	</div>
</content>
</body>
<script>
var words = [
		/*{text: "Lorem", weight: 13},
		{text: "Ipsum", weight: 10.5},
		{text: "Dolor", weight: 9.4},
		{text: "Sit", weight: 8},
		{text: "Amet", weight: 6.2},
		{text: "Consectetur", weight: 5},
		{text: "Adipiscing", weight: 5},*/
		<%
		for (Hashtable<String, String> item : al) {
			String keyword = item.get("keyword");
			String score = item.get("score");
			String link = "indexDetail.jsp?host=" + hostname + "&keyword=" + keyword;
			
			out.println("{text: '" + keyword + "', weight: " + score + ", link: '" + link + "'},");
//			out.print("{text: " + item.get("keyword") + ", weight: " + 10 + ", link: '" + item.get("url") + "'},");
		}
		%>
];
           
$('#cloudtest').jQCloud(words, {
	  width: 1000,
	  height: 600
	});
</script>
<script src="http://d3js.org/d3.v3.min.js"></script>
</html>