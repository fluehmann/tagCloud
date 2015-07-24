<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/crawler.css" />
	<title>IP6 Bachelor Thesis: TagCloud mit Elasticsearch</title>
</head>
<body>
	<div id="form-main">
		<div id="form-div">
			<form class="form" id="form1" method="POST" action="doCrawl.jsp">

				<p class="name">
					<input name="crawl_hostname" type="text" class="crawl-input" placeholder="hostname" id="" />
				</p>

				<div class="submit">
					<input type="submit" value="Beginne Crawl" id="button-blue" />
					<div class="ease"></div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>