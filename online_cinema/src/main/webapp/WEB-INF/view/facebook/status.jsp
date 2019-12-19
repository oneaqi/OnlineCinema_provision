
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<spring:message code="movies.edit.title" arguments="${movie.id}"
	var="titleEdit" />
<spring:message code="movies.new.title" arguments="${movie.id}"
	var="titleNew" />

<c:url var="urlConnect" value="/connect" />
<c:url var="urlLike" value="/movies/list" />

<div id="facebookStatus">
	<c:forEach items="${providerIds}" var="provider">
		<c:if test="${not empty connectionMap[provider]}">
			Povezani ste na ${provider}. <a href="${urlLike}">Nastavite</a>
		</c:if>
		<c:if test="${empty connectionMap[provider]}">
			Niste povezani na ${provider}. Kliknite <a href="${urlConnect}">ovdje</a> da se povežete.
		</c:if>
	</c:forEach>
</div>
