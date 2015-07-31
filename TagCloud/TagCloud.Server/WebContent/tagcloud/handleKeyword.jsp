<%@ page import="tagcloud.server.controller.TagController"%>

<%
	TagController tcntrl = new TagController();
	String action	= request.getParameter("action");
	
  String hostname = request.getParameter("hostname");
  String keyword = request.getParameter("ign_keyword");
  String id = request.getParameter("key");
	
  if(action.equals("add")){
  		tcntrl.addKeywordToBlacklist(hostname, keyword);
  		response.sendRedirect("index.jsp?host=" + hostname);
  }
  if(action.equals("delete")){
  		tcntrl.delKeywordFromBlacklist(hostname, id);
  		response.sendRedirect("index.jsp?host=" + hostname);
  }
	
	
	
%>