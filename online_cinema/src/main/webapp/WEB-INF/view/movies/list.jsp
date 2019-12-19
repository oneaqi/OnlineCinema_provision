
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

<script type="text/javascript">
	function rateMovie(movieId,userId,rating){
		var params = 'movieId='+movieId+'&userId='+userId+'&rating='+rating;
		$.ajax({
			url: '${urlRating}',
			data:params,
			success: function(data){
				
			}
		});
	}
</script>

<div id="moviesList">

	<c:if test="${not empty message}">
			<div id="message">${message}</div>
	</c:if>
		
	<h2>${messageAvailable}</h2>
	
	<div class="action">
	<security:authorize access="hasRole('ROLE_ADMIN')">
		<a href="${urlNew}" class="button">${labelNew}</a>
	</security:authorize>
	</div>

	<c:forEach var="movie" items="${movies}">
		<div class="movie">
			<div class="movieImage">
				<a class="popup-trailer"
					<security:authorize access="isAuthenticated()">
						href="${movie.trailerUrl}"
					</security:authorize>>
					<img src="${urlPhoto}/${movie.id}" alt="${movie.name}"/>
				</a>
				<security:authorize
						access="isAuthenticated()">
						<security:authorize access="hasRole('ROLE_USER')">
							<input type="hidden" class="rating" data-size="sm" data-step="1"
								data-glyphicon="false" data-show-caption="false"
								value="${ratings[movie.id]}"
								onchange="rateMovie(${movie.id},'${username}',this.value)" />
						</security:authorize>
						<security:authorize access="hasRole('ROLE_ADMIN')">
							<a href="${urlEdit}/${movie.id}" class="button">${labelEdit}</a>
						</security:authorize>
					</security:authorize>
			</div>
			<h3 class="title">${movie.id}.${movie.name}
			<security:authorize access="isAuthenticated()">
				<a href="${urlLike}/${movie.id}"> <img alt="Like" src="${imageLike}" /></a>
			</security:authorize>
			</h3>
			<p>${movie.description}</p>
			<p>${movie.wiki}</p>
			<em>${movie.actors}</em>
		</div>
	</c:forEach>
</div>
<div class="pagesList">
	<c:forEach var="page" items="${pages}">
		<a href="${urlPage}/${page}">${page}-${page+pageSize-1}</a>&nbsp;
		</c:forEach>
</div>

