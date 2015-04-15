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
        
    <script data-require="jquery@2.1.3" data-semver="2.1.3" src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
    <link data-require="bootstrap@3.3.2" data-semver="3.3.2" rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
    <script data-require="bootstrap@3.3.2" data-semver="3.3.2" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    <script data-require="angular.js@1.3.14" data-semver="1.3.14" src="https://code.angularjs.org/1.3.14/angular.js"></script>
    <script data-require="angular.js@1.3.14" data-semver="1.3.14" src="https://code.angularjs.org/1.3.14/angular-animate.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/sockjs.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/stomp.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
	<script src="${pageContext.request.contextPath}/static/js/pgpChat.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/services/chatService.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/controllers/chatController.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/openpgp.worker.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/openpgp.js"></script>
    <script>
    	var $HOME_PATH = "${pageContext.request.contextPath}";
    	var $YOUR_NAME = "${pageContext.request.userPrincipal.name}";
    </script>
  </head>
  <body ng-controller="chatController as chat">
	<div class="container-fluid">
		<header id="header" ng-show="!chat.desktopNotification">
			<div class="row" id="notificationPanel">
				<div class="col-md-6 col-md-offset-3" id="notification" ng-model="notification">
					Desktop Notification if OFF, click <a href="#" ng-click="chat.requestNotificationPermission()">here</a> to turn it on.
				</div>
			</div>
		</header>
		<div class="row">
			<div id="sideBarTitleRight">
				<button id="menu" type="button" class="btn btn-info" ng-click="chat.sideBarToggle()">
					<span class="glyphicon glyphicon-align-justify"></span>
				</button>
			</div>
			<div class="filled-vertical" id="content">
				<div id="chatToUser">{{chat.chatTo}}</div>
				<div id="chatContainer" class="container">
					<div class="chatMessageContainer col-xs-12 col-md-12" ng-repeat="chatMessage in chat.chatMessages">
						<div class="fromUser">{{chatMessage.from}}</div>
						<div class="chatMessage">{{chatMessage.message}}</div>
					</div>
				</div>
			</div>
			<div class="filled-vertical" id="side-bar" ng-model="chat.sideBar">
				<if test="${pageContext.request.userPrincipal.name != null}">
					<div class="footer">
						<a id="sideBarFooter" href="#" class="col-xs-12 btn btn-danger" ng-click="chat.logout()"><span class="glyphicon glyphicon-log-out"></span> Logout</a>
					</div>
				</if>
				<div id="sideBarContent" class="container">
					<div ng-repeat="user in chat.activeUsers">
						<a href="#" ng-click="chat.setChatToUser(user.userName)"><div class="col-xs-12">{{user.userName}}</div></a>
					</div>
				</div>
			</div>
		</div>
		<footer id="chatFooter" ng-show="chat.chatToUser">
			<div class="navbar navbar-inverse navbar-fixed-bottom">
				<div class="container">
					<div class="navbar-collapse" id="footer-body">
						<div>
							<form ng-submit="chat.sendStompMessage()">
								<input id="message" name="message" class="form-control" type="text" ng-model="chat.message" placeholder="Type your message here" ng-maxlength="200"/>
								<input type="submit" style="display:none"/>					
							</form>
						</div>
					</div>
				</div>
			</div>
		</footer>
	</div>
	<input type="hidden" id="txtMenu" value="{{chat.sideBarExpanded}}"/>
	<form action="${pageContext.request.contextPath}/j_spring_security_logout" method="post" id="logoutForm" name="logoutForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
  </body>
</html>
