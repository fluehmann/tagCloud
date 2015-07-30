<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/crawler.css" />
	<title>IP6 Bachelor Thesis: TagCloud mit Elasticsearch</title>
	<script type="text/javascript">
		function loader() {
    		document.getElementById("loader").style.visibility = "visible"; 
		}
	</script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
</head>
<body>
<!-- sidebar placeholder -->

	<div id="form-main">

		<div id="logowrapper">
			<img id="logo" src="${pageContext.request.contextPath}/assets/logo.png"/>
		</div>

		<div id="box">
			<h1>Crawler</h1>
			<form class="form" id="form1" method="POST" action="docrawl.jsp">
				<p>Dateityp wählen und URL bzw absoluten Dateipfad eingeben.</p>
				<br>
				<div class="dropdown">
					<select class="style-select" name="crawl_type">
  						<option value="website">Website</option>
  						<option value="file">Datei</option>
  						<option disabled="disabled" value="none">...</option>
					</select>
				</div>

				<p class="name">
					<input name="crawl_hostname" type="text" class="crawl-input" placeholder="URL oder Dateipfad eingeben" id="" />
				</p>

				<div class="submit">

					<input type="submit" value="Crawl starten" id="button-blue" onclick="$('#form1').hide(); $('#loading').show(); "/>

				</div>
			</form>
			<div id="loading" style="display:none;">
				<img src="${pageContext.request.contextPath}/assets/ajax-loader.gif"/>
				<h3>Daten werden gesammelt...</h3>
				<p>Dies kann einen Moment dauern.<br>
				Visualisierungen zum gewünschten Datensatz sind bereits verfügbar.</p>
			</div>
		</div>
	</div>
</body>
</html>