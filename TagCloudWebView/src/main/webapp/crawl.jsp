
<jsp:useBean id="urlTransmit" class="com.tagCloud.urlTransmit" scope="session"/>
<jsp:setProperty name="urlTransmit" property="url" value="${param.urltocrawl}" />
${urltransmit.getEmail} wird gecrawlt
${urltransmit.startCrawl}
