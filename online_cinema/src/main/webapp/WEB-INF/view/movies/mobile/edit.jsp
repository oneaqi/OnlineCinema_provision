
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<spring:message code="movies.edit.title" arguments="${movie.id}"
	var="titleEdit" />
<spring:message code="movies.new.title" arguments="${movie.id}"
	var="titleNew" />
<spring:eval expression="movie.id == 0 ? titleNew : titleEdit"
	var="title" />

<spring:message code="movies.name" var="labelName" />
<spring:message code="movies.releaseDate" var="labelDate" />
<spring:message code="movies.description" var="labelDescription" />
<spring:message code="movies.trailer" var="labelTrailer" />
<spring:message code="movies.image" var="labelImage" />
<spring:message code="movies.actors" var="labelActors" />
<spring:message code="movies.director" var="labelDirector" />
<spring:message code="form.save" var="labelSave" />
<spring:message code="form.reset" var="labelReset" />
<spring:message code="form.ws" var="labelWS" />

<spring:url value="/movies/photo" var="urlPhoto" />
<spring:url value="/movies/getinfo" var="urlGetInfo" />

<script type="text/javascript">
	function downloadInformations() {
		$('#poster,#file').fadeOut();
		var imdbId = prompt("Imdb ID:", "tt1631867");
		$.mobile.loading('show')
		var params = 'imdbId=' + imdbId;
		$.ajax({
			url : "${urlGetInfo}",
			data : params,
			cache: false,
			success : function(data) {
				$.mobile.loading('hide')
				if (data.Title != null) {
					$('#name').val(data.Title);
					$('#description').val(data.Plot);
					
					var timestamp = new Date().getTime();
					$('#poster').attr('src', data.Poster + '?' + timestamp);
					$('#fileUrl').val(data.Poster);

					$('#trailerUrl').val('http://www.youtube.com');

					$('#actors').val(data.Actors);
					$('#director').val(data.Director);

					var date = new Date(data.Released);
					$('#releaseDate').val(
							date.getDate() + "." + date.getMonth() + "."
									+ date.getFullYear());

				}
			}
		});
	}
</script>

<div class="movieEdit">
	<h2>${title}</h2>
	<!-- IMPORTANT: when using jquery-mobile in forms with files we must use data-ajax="false"
	     if we do not set this attribute, we will get not multipart exception! 
	-->
	<form:form modelAttribute="movie" id="editMovieForm" method="post"
		enctype="multipart/form-data" data-ajax="false">

		<c:if test="${not empty message}">
			<div id="message">${message}</div>
		</c:if>

		<form:label path="name">${labelName}</form:label>
		<form:input path="name" />
		<form:errors path="name" cssClass="error" />
		

		<form:label path="releaseDate">${labelDate}</form:label>
		<form:input path="releaseDate" />
		<form:errors path="releaseDate" cssClass="error" />


		<form:label path="description">${labelDescription}</form:label>
		<form:textarea path="description" rows="10" cols="50" />
		<form:errors path="description" cssClass="error" />



		<form:label path="actors">${labelActors}</form:label>
		<form:input path="actors" />
		<form:errors path="actors" cssClass="error" />



		<form:label path="director">${labelDirector}</form:label>
		<form:input path="director" />
		<form:errors path="director" cssClass="error" />



		<form:label path="trailerUrl">${labelTrailer}</form:label>
		<form:input path="trailerUrl" />
		<form:errors path="trailerUrl" cssClass="error" />


		
		<form:label path="image" />${labelImage}
		<input name="file" type="file" id="file"/>
		<input id="fileUrl" name="fileUrl" type="hidden" />

		<form:errors path="image" cssClass="error" />

		<img id="poster" src="${urlPhoto}/${movie.id}" height="300"
			width="200" />
		<form:hidden path="version" />

		<button type="submit">${labelSave}</button>
		<button type="reset">${labelReset}</button>
		<button type="button" onclick="downloadInformations()">${labelWS}</button>
	</form:form>
</div>
