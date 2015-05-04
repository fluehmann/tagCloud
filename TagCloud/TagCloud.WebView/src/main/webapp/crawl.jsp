
<jsp:useBean id="url" class="model.UrlBean" scope="session" />
<jsp:setProperty name="url" property="startUrl" value="${param.urltocrawl}" />
${urltransmit.getStartUrl} wird gecrawlt
${urltransmit.startCrawl}

<jsp:getProperty property="startUrl" name="url" />
