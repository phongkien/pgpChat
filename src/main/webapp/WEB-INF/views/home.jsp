<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<form action="${pageContext.request.contextPath}/j_spring_security_logout" method="post" id="logoutForm" name="logoutForm">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>
<if test="${pageContext.request.userPrincipal.name != null}">
<h2>
	Welcome : ${pageContext.request.userPrincipal.name} | <a
		href="javascript:document.logoutForm.submit()"> Logout</a>
</h2>
</if>