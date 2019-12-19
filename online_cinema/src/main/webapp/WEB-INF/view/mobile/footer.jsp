<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<spring:url value="/movies" var="urlHome" />
<spring:message code="app.home" var="labelHome"/>

<spring:url value="/movies/catalog" var="urlCatalog" />
<spring:message code="movies.catalog" var="labelCatalog"/>
<spring:url value="?site_preference=normal" var="urlNormal" />
<spring:message code="page.normal" var="labelPageNormal"/>

<div id="footer">
	<a href="${urlHome}" data-ajax="false">${labelHome}</a>&nbsp;
	<a href="${urlCatalog}" data-ajax="false">${labelCatalog}</a>&nbsp;
	<a href="${urlNormal}" data-ajax="false">${labelPageNormal}</a>&nbsp;
</div>