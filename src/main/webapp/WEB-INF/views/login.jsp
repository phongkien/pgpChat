<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html ng-app="pgpChat">
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script data-require="jquery@2.1.3" data-semver="2.1.3" src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
    <link data-require="bootstrap@3.3.2" data-semver="3.3.2" rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
    <script data-require="bootstrap@3.3.2" data-semver="3.3.2" src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    <script data-require="angular.js@1.3.14" data-semver="1.3.14" src="https://code.angularjs.org/1.3.14/angular.js"></script>
    <script data-require="angular.js@1.3.14" data-semver="1.3.14" src="https://code.angularjs.org/1.3.14/angular-animate.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
    <script src="${pageContext.request.contextPath}/static/js/app.js"></script>
  </head>

  <body>
   <div class="vertical-center">
     <div class="container-fluid login-panel" ng-controller="loginController">
        <div class="login-title">pgpChat</div>
        <form id="loginForm" name="loginForm" ng-model="loginForm" class="form-horizontal" ng-submit="login()">
          <div class="form-group row top-row">
            <div class="col-sm-10 filled">
              <input class="form-control" type="text" id="username" ng-model="username" required placeholder="Username"/>
            </div>
          </div>
          <div class="form-group row">
            <div class="col-sm-10 filled" id="pnlPassword">
              <input class="form-control" type="password" id="password" ng-model="password" required placeholder="Password"/>
            </div>
          </div>
          <div class="form-group row" ng-show="isRegistered()">
            <div class="col-sm-10 filled" id="pnlVerifyPassword">
              <input class="form-control" type="password" id="verifypassword" ng-model="verifypassword" placeholder="Confirm Password" ng-required="isRegistered()" />
            </div>
          </div>
          <div class="form-group row" ng-show="hasError()">
            <div class="col-sm-10 filled text-danger" ng-model="errorMessag" id="errorMessage"></div>
          </div>
          <div class="form-group">
            <div class="col-offset-10 col-sm-10">
              <button class="btn btn-primary" type="submit" id="btnLogin" ng-show="!isRegistered()">Login</button>
              <input type="button" ng-class="{'btn btn-primary': isRegistered(), 'btn btn-default': !isRegistered()}" id="btnRegister" ng-click="doRegister()" value="Register"/>
            </div>
          </div>
        </form>
      </div>
    </div>
  </body>

</html>
