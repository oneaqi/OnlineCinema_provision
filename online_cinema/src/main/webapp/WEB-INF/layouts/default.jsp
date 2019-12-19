<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>

<spring:message code="app.title" var="appTitle" />
<spring:url value="/resources/styles/reset.css" var="urlCssReset"/>
<spring:url value="/resources/styles/main.css" var="urlCssMain"/>
<spring:url value="/resources/scripts/jquery-1.11.3.min.js" var="urlScriptJquery"/>
<spring:url value="/resources/scripts/jqueryui/jquery-ui.min.css" var="urlCssJqueryUI"/>
<spring:url value="/resources/scripts/jqueryui/jquery-ui.min.js" var="urlScriptJqueryUI"/>
<spring:url value="/resources/scripts/magnific-popup.css" var="urlCssPopup"/>
<spring:url value="/resources/scripts/jquery.magnific-popup.min.js" var="urlScriptPopup"/>
<spring:url value="/resources/scripts/star-rating.min.css" var="urlCssRate"/>
<spring:url value="/resources/scripts/star-rating.min.js" var="urlScriptRate"/>
<spring:url value="/resources/scripts/cinema.js" var="urlScriptCinema"/>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${urlCssReset}" rel="stylesheet" />
<link href="${urlCssMain}" rel="stylesheet" />
<script src="${urlScriptJquery}"></script>
<link href="${urlCssJqueryUI}" rel="stylesheet" />
<script src="${urlScriptJqueryUI}"></script>
<link href="${urlCssPopup}" rel="stylesheet" />
<script src="${urlScriptPopup}"></script>
<link href="${urlCssRate}" rel="stylesheet" />
<script src="${urlScriptRate}"></script>
<script src="${urlScriptCinema}"></script>
<title>${appTitle}</title>
<script type="text/javascript">
	$(document).ready(function(){
		$('.date').datepicker({'dateFormat':'dd.mm.yy'});
	});
</script>
</head>
<body>
	<div class="page">
		<h1>${appTitle}</h1>
		<div class="headerWrapper">
			<tiles:insertAttribute name="header" ignore="true" />
		</div>
		<div class="contentWrapper">
			<tiles:insertAttribute name="menu" ignore="true" />
			<div class="main">
				<tiles:insertAttribute name="body" />
				<tiles:insertAttribute name="footer" ignore="true" />
			</div>
		</div>
	</div>
</body>
</html>