
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<spring:url value="/movies/photo" var="urlPhoto" />
<spring:message code="movies.title" var="messageTitle" />
<spring:message code="movies.available" var="messageAvailable" />

<spring:url value="/movies/edit" var="urlEdit" />
<spring:message code="movies.edit" var="labelEdit" />
<spring:url value="/movies/new" var="urlNew" />
<spring:message code="movies.new" var="labelNew" />

<spring:url value="/movies/page" var="urlPage" />
<spring:url value="/movies/rating" var="urlRating" />

<spring:url value="/resources/images/like32.png" var="imageLike" />
<spring:url value="/movies/like" var="urlLike" />

<div class="moviesList">

	<p>${message}</p>
	<h2>${messageAvailable}</h2>

	<security:authorize access="hasRole('ROLE_ADMIN')">
		<a href="${urlNew}" data-role="button">${labelNew}</a>
	</security:authorize>

	<c:forEach var="movie" items="${movies}">
		<div>${movie.id}.${movie.name} </br></div>
		<div> <a class="popup-trailer"
			<security:authorize access="isAuthenticated()">
				href="${movie.trailerUrl}"
			</security:authorize>>
				<img src="${urlPhoto}/${movie.id}" height="150" width="100" />
		</a>
		</div>
		<div> ${movie.description} </div>
		<div class="cast">${movie.actors}</div>
		<security:authorize access="hasRole('ROLE_ADMIN')">
					<a href="${urlEdit}/${movie.id}" data-role="button">${labelEdit}</a>
				</security:authorize>
		<br />
	</c:forEach>
</div>
<div class="pagesList">
	<c:forEach var="page" items="${pages}">
		<a href="${urlPage}/${page}">${page}-${page+pageSize-1}</a>&nbsp;
		</c:forEach>
</div>

