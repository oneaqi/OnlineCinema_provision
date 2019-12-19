
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<spring:message code="movies.edit.title" arguments="${movie.id}" var="titleEdit"/>
<spring:message code="movies.new.title" arguments="${movie.id}" var="titleNew"/>
<spring:eval expression="movie.id == 0 ? titleNew : titleEdit" var="title"/>

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
	function downloadInformations(){
		var imdbId = prompt("Imdb ID:","tt1631867");
		var params = 'imdbId='+imdbId;
		$.ajax({
			url: "${urlGetInfo}",
			data:params,
			success: function(data){
				console.dir(data);
				if (data.Title != null){
					//console.dir(data);
					$('#name').val(data.Title);
					$('#description').val(data.Plot);
					$('#poster').attr('src',data.Poster);
					$('#fileUrl').val(data.Poster);
					
					$('#trailerUrl').val('http://www.youtube.com');
					
					$('#actors').val(data.Actors);
					$('#director').val(data.Director);
					
					var date = new Date(data.Released);
					$('#releaseDate').val(date.getDate() + "." + date.getMonth() + "." + date.getFullYear());
					
				} 
			}
		});
	}
</script>

<div class="movieEdit">
	<h2>${title}</h2>
	<form:form modelAttribute="movie" class="editMovieForm" method="post" enctype="multipart/form-data">
	
		<c:if test="${not empty message}">
			<div id="message">${message}</div>
		</c:if>
		
		<div class="poster">
			<img id="poster" src="${urlPhoto}/${movie.id}"	height="300" width="200" />
		</div>
		
		<fieldset>
			<form:label path="name">${labelName}</form:label>
			<form:input path="name" />
			<form:errors path="name" cssClass="error" />
		</fieldset>
		
		<fieldset>
			<form:label path="releaseDate">${labelDate}</form:label>
			<form:input class="date" path="releaseDate" />
			<form:errors path="releaseDate" cssClass="error" />
		</fieldset>
		
		<fieldset>
			<form:label path="description">${labelDescription}</form:label>
			<form:textarea path="description" rows="10" cols="50"/>	
			<form:errors path="description" cssClass="error" />
		</fieldset>
		
		<fieldset>
			<form:label path="actors" >${labelActors}</form:label>
			<form:input path="actors"/>
			<form:errors path="actors" cssClass="error" />	
		</fieldset>
		
		<fieldset>
			<form:label path="director">${labelDirector}</form:label>
			<form:input path="director"/>
			<form:errors path="director" cssClass="error" />
		</fieldset>
		
		<fieldset>
			<form:label path="trailerUrl">${labelTrailer}</form:label>	
			<form:input path="trailerUrl"/>
			<form:errors path="trailerUrl" cssClass="error" />
		</fieldset>
		
		<fieldset>
			<form:label path="image">${labelImage}</form:label>
			<input name="file" type="file"/>	
			<input id="fileUrl" name="fileUrl" type="hidden" />
			<form:errors path="image" cssClass="error" />
		</fieldset>
		
		<form:hidden path="version" />
		
		
		<button type="submit" class="button">${labelSave}</button>
		<button type="reset" class="button">${labelReset}</button>
		<button type="button" class="button" onclick="downloadInformations()">${labelWS}</button>
		
		
	</form:form>
</div>
