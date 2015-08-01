<%@ page import="tagcloud.server.controller.TagController"%>
<%@ page import="tagcloud.server.controller.RetrieveController"%>
<%@ page import="tagcloud.server.controller.TagController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="java.util.List,java.util.Hashtable"%>

<%
	String hostname = request.getParameter("host");

	RetrieveController rcntrl = new RetrieveController();
	ArrayList<Hashtable<String, String>> al = rcntrl.getSigTerms(hostname, 50);
	
	TagController tcntrl = new TagController();
	Hashtable<Integer, String> table = tcntrl.getKeywordFromBlacklist(hostname);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jqcloud.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/modal.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/addtoblacklist.css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqcloud.min.js"></script>

<title>Insert title here</title>
</head>
<body>
<!-- Begin Sidebar -->
<%@include file="../sidebar.jsp" %>
<!-- End Sidebar -->
<div class="content">
	<h1>
		<% out.print("Gefundene URLs zu " + hostname); %>
	</h1>

	<div id="cloudtest">	
	</div>
	
	<div class='container'>
	  <h1>Ignore keyword</h1>
	  <form method="post" action="handleKeyword.jsp?action=add">
	  	<input name="hostname" type="hidden" value="<% out.print(hostname); %>">
	  	<input class="search" name="ign_keyword" type="search">
	  </form>
	</div>
	
	<a href="#openModal">Open Modal</a>
	<div id="openModal" class="modalDialog">
	  <dialog>
	    <a href="#" title="Close" class="close">X</a>
	    <h2>Modal Box</h2>
	    <%  for (int key : table.keySet()) {
						out.print("<p><a href='handleKeyword.jsp?action=delete&hostname="+ hostname +"&key=" + key + "'>" + table.get(key) + "</a></p>");
	    		}
	    %>
	  </dialog>
	</div>
	
</div>
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
		}
		%>
];
           
$('#cloudtest').jQCloud(words, {
	  width: 1000,
	  height: 600
	});
</script>
</html>