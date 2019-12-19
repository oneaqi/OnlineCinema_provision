<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="header">
	<spring:url value="/j_spring_security_check" var="loginUrl" />
	<spring:url value="/j_spring_security_logout" var="logoutUrl" />
	<spring:url value="/users/register" var="urlRegister" />
	<spring:message var="labelUsername" code="user.username" />
	<spring:message var="labelPassword" code="user.password" />
	<spring:message var="labelLogin" code="user.login" />
	<spring:message var="labelLogout" code="user.logout" />
	<spring:message var="labelRegister" code="user.register" />
	<spring:message var="messageWelcome" code="user.welcome" />

	<spring:url value="/resources/images/bosnia32.png" var="imageBosnia" />
	<spring:url value="/resources/images/italia32.png" var="imageItalia" />
	<spring:url value="/resources/images/usa32.png" var="imageEngland" />
	<spring:url value="?lang=bs" var="urlBos" />
	<spring:url value="?lang=it" var="urlIta" />
	<spring:url value="?lang=en" var="urlEng" />

	<security:authorize access="isAnonymous()">
		<div class="login">
			<form name="loginForm" action="${loginUrl}" method="post">
				<fieldset>
					<label for="username">${labelUsername}:</label>
					<input type="text" name="j_username" id="username"/>
				</fieldset>
				<fieldset>
					<label for="password">${labelPassword}</label>
					<input type="password" name="j_password" id="password"/>
				</fieldset>
				<input name="submit" type="submit" value="${labelLogin}" class="button"/>
				<a class="button" href="${urlRegister}">${labelRegister}</a>
				<div class="languages">
					<a href="${urlBos}"> <img alt="bos" src="${imageBosnia}" /></a> 
					<a href="${urlIta}"> <img alt="bos" src="${imageItalia}" /></a>
					<a href="${urlEng}"> <img alt="bos" src="${imageEngland}" /></a>
				</div>
			</form>
		</div>
	</security:authorize>
	<security:authorize access="isAuthenticated()">
		<div class="login">
				<label class="welcome">${messageWelcome}
					<security:authentication
						property="principal.username" /> </label>
			<a class="button" href="${logoutUrl}">${labelLogout}</a>
			<div class="languages">
					<a href="${urlBos}"> <img alt="bos" src="${imageBosnia}" /></a> 
					<a href="${urlIta}"> <img alt="bos" src="${imageItalia}" /></a>
					<a href="${urlEng}"> <img alt="bos" src="${imageEngland}" /></a>
			</div>
		</div>
	</security:authorize>

</div>