<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>

<spring:message code="app.title" var="appTitle" />
<spring:url value="/resources/styles/main.css" var="urlCssMain" />
<spring:url value="/resources/scripts/jquery-1.11.3.min.js"
	var="urlScriptJquery" />
<spring:url value="/resources/scripts/jquerymobile/jquery.mobile-1.4.5.min.css"
	var="urlCssJqueryMobile" />
<spring:url value="/resources/scripts/jquerymobile//jquery.mobile-1.4.5.min.js"
	var="urlScriptJqueryMobile" />
<spring:url value="/resources/scripts/magnific-popup.css"
	var="urlCssPopup" />
<spring:url value="/resources/scripts/jquery.magnific-popup.min.js"
	var="urlScriptPopup" />
<spring:url value="/resources/scripts/star-rating.min.css"
	var="urlCssRate" />
<spring:url value="/resources/scripts/star-rating.min.js"
	var="urlScriptRate" />
<spring:url value="/resources/scripts/cinema.js" var="urlScriptCinema" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- <link href="${urlCssMain}" rel="stylesheet" /> -->
<script src="${urlScriptJquery}"></script>
<link href="${urlCssJqueryMobile}" rel="stylesheet" />
<script src="${urlScriptJqueryMobile}"></script>
<link href="${urlCssPopup}" rel="stylesheet" />
<script src="${urlScriptPopup}"></script>
<link href="${urlCssRate}" rel="stylesheet" />
<script src="${urlScriptRate}"></script>
<script src="${urlScriptCinema}"></script>
<title>${appTitle}</title>
</head>
<body>
	<div data-role="page" id="pageWrapper">
		<div data-role="header" id="headerWrapper">
			<tiles:insertAttribute name="header" ignore="true" />
		</div>
		<div role="main" class="ui-content">
			<tiles:insertAttribute name="body" />
		</div>
		<div data-role="footer">
			<tiles:insertAttribute name="footer" ignore="true" />
		</div>
	</div>
</body>
</html>