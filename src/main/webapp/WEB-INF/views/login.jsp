<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html ng-app="pgpChat">
  <head>
  	<title>pgpChat</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
        
    <script data-require="jquery@2.1.3" data-semver="2.1.3" src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
    <link data-require="bootstrap@3.3.2" data-semver="3.3.2" rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
    <script data-require="bootstrap@3.3.2" data-semver="3.3.2" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    <script data-require="angular.js@1.3.14" data-semver="1.3.14" src="https://code.angularjs.org/1.3.14/angular.js"></script>
    <script data-require="angular.js@1.3.14" data-semver="1.3.14" src="https://code.angularjs.org/1.3.14/angular-animate.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
    <script src="${pageContext.request.contextPath}/static/js/pgpChat.js"></script>
    <script>
    	var $HOME_PATH = "${pageContext.request.contextPath}";
    </script>
  </head>

  <body>
   <div class="vertical-center">
     <div class="container-fluid login-panel" ng-controller="loginController">
        <div class="login-title">pgpChat</div>
        <form id="loginForm" name="loginForm" ng-model="loginForm" class="form-horizontal" ng-submit="login()">
          <div class="form-group row top-row">
            <div class="col-sm-10 filled">
              <input class="form-control" type="text" id="username" name="username" ng-model="username" required placeholder="Username" ng-focus="setFocus('username')"/>
            </div>
          </div>
          <div class="form-group row">
            <div ng-class="{'col-sm-10 filled has-error':hasPasswordError(), 'col-sm-10 filled':!hasPasswordError()}" id="pnlPassword">
              <input class="form-control" type="password" id="password" name="password" ng-model="password" required placeholder="Password" ng-change="checkPassword()" ng-focus="setFocus('password')"/>
            </div>
          </div>
          <div class="form-group row" ng-show="isRegistered()">
            <div ng-class="{'col-sm-10 filled has-error':hasPasswordError(), 'col-sm-10 filled':!hasPasswordError()}" id="pnlVerifyPassword">
              <input class="form-control" type="password" id="verifypassword" ng-model="verifypassword" placeholder="Confirm Password" ng-required="isRegistered()" ng-change="checkPassword()" ng-focus="setFocus('verifypassword')"/>
            </div>
          </div>
          <div class="form-group row" ng-show="isRegistered()">
          	<div class="col-sm-10 filled">
          		<input class="form-control" type="email" id="email" name="email" ng-model="email" placeholder="Email" ng-required="isRegistered()" ng-focus="setFocus('email')"/>
          	</div>
          </div>
          <div class="form-group row" ng-show="isRegistered()">
          	<div class="col-sm-10 filled">
          		<input class="form-control" type="text" id="firstName" name="firstName" ng-model="firstName" placeholder="First Name" ng-required="isRegistered()" ng-focus="setFocus('firstName')"/>
          	</div>
          </div>
          <div class="form-group row" ng-show="isRegistered()">
          	<div class="col-sm-10 filled">
          		<input class="form-control" type="text" id="lastName" name="lastName" ng-model="lastName" placeholder="Last Name" ng-required="isRegistered()" ng-focus="setFocus('lastName')"/>
          	</div>
          </div>
          <div class="form-group row" ng-show="hasError()">
            <div class="col-sm-10 filled error-message">{{errorMessage}}</div>
          </div>
          <div class="form-group">
            <div class="col-offset-10 col-sm-10">
              <button class="btn btn-primary" type="submit" id="btnLogin" ng-disabled="processing">{{txtLoginButton}}</button>
              <input type="button" ng-class="{'btn btn-primary': isRegistered(), 'btn btn-default': !isRegistered()}" id="btnRegister" ng-click="doRegister()" value="Register" ng-hide="isRegistered()"/>
            </div>
          </div>
          <input type="hidden" id="csrfToken" ng-model="csrfToken" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>       
      </div>
    </div>
  </body>
</html>
    