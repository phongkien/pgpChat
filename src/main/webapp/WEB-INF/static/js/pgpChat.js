var app = angular.module('pgpChat', [ 'ngAnimate']);
var colors = ["#ADD8E6"];

app.controller("loginController", ['$scope', '$http', '$filter', function($scope, $http, $filter) {
	$scope.registered = false;
	$scope.username = "";
	$scope.password = "";
	$scope.verifypassword = "";
	$scope.error = false;
	$scope.errorMessage = "";
	$scope.txtLoginButton = "Login";
	$scope.passwordError = false;
	$scope.csrfToken = "";
	$scope.processing = false;
	$scope.lastFocus = "";
	
	$scope.setFocus = function(focusElement) {
		$scope.lastFocus = focusElement;
	};
	
	$scope.focusLastElement = function() {
		if ($scope.lastFocus) {
			$("#" + $scope.lastFocus).focus();
		}
	};

	$scope.doRegister = function() {
		$scope.registered = true;
		$scope.txtLoginButton = "Register";
		$scope.focusLastElement();
	};

	$scope.checkPassword = function() {
		if ($scope.password && $scope.verifypassword && $scope.password != $scope.verifypassword) {
			$scope.invalidPassword();
		} else {
			$scope.resetError();
		}
	}

	$scope.invalidPassword = function() {
		$scope.errorMessage = "Password mis-matched";
		$scope.error = true;
		$scope.passwordError = true;
	}

	$scope.resetError = function() {
		$scope.errorMessage = "";
		$scope.error = false;
		$scope.passwordError = false;
	}

	$scope.hasPasswordError = function() {
		return $scope.passwordError;
	}

	$scope.isRegistered = function() {
		return $scope.registered;
	}

	$scope.hasError = function() {
		return $scope.error;
	}

	$scope.loginFailure = function() {
		$scope.errorMessage = "Invalid username and/or password.";
		$scope.error = true;
	}

	$scope.login = function() {
		var submitValue = "";
		var url = "";
		var $csrfToken = $("#csrfToken").val();

		$scope.error = false;
		$scope.passwordError = false;
		$scope.processing = true;

		if ($scope.isRegistered() && $scope.username !== "" && $scope.password !== "" && $scope.password === $scope.verifypassword && $scope.firstName !== "" && $scope.lastName !== "") {
			submitValue = {
				username : $scope.username,
				password : $scope.password,
				verifyPassword : $scope.verifypassword,
				email : $scope.email,
				firstName: $scope.firstName,
				lastName: $scope.lastName
			};

			url = $HOME_PATH + "/register";

			var submitStatus = $http({
				method: 'POST',
				url: url,
				headers : {
					'X-XSRF-TOKEN': $("meta[name='_csrf']").attr("content")
				},
				data: submitValue
			});

			submitStatus.success(function(data, status, headers, config) {
				if (data && data.statusText == "User is successfully created") {
					//window.open($HOME_PATH + "/home", "_self");
					//$("#form2").submit();
					$scope.registered = false;
					$scope.login();
				} else {
					$scope.errorMessage = data.statusText;
					$scope.error = true;
				}
				
				$scope.processing = false;
			});

			submitStatus.error(function(data, status, headers, config) {
				$scope.errorMessage = status;
				$scope.error = true;
				$scope.processing = false;
			});
		} else if (!$scope.isRegistered() && $scope.username !== "" && $scope.password !== "") {
			url = $HOME_PATH + "/j_spring_security_check";
			submitValue = $.param({
				username : $scope.username,
				password : $scope.password,
				_csrf : $("#csrfToken").val(),
				'X-XSRF-TOKEN': $("meta[name='_csrf']").attr("content")
			});

			var submitStatus = $http({
				url : url,
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				},
				method : 'POST',
				data : submitValue
			});

			submitStatus.success(function(data, status, headers, config) {
				if (data && data.statusText == "Login Success") {
					window.open($HOME_PATH + "/home", "_self");
				} else if (data && data.statusText == "Invalid username and/or password") {
					$scope.loginFailure();
				}
				
				$scope.processing = false;
			});

			submitStatus.error(function(data, status, headers, config) {
				$scope.errorMessage = status;
				$scope.error = true;
				$scope.processing = false;
			});
		} else {
			$scope.invalidPassword();
			$scope.processing = false;
		}
	}
}]);

app.controller('chatController', ['$scope', '$http', '$interval', '$anchorScroll', '$location', function($scope, $http, $interval, $anchorScroll, $location) {
	var chat = this;
	this.sideBarExpanded = false;
	this.currentIdx = 0;
	this.myIdx = -1;
	this.currentUser = "";
	this.activeUsers = [];
	this.chatMessages = [];
	this.chatToUser = "";
	this.chatTo = "";
	this.chatListener = "";
	this.isBusy = false;
	this.containerHash = "";
	
	this.sideBarToggle = function() {
		$("#menu").hide();
		chat.sideBarExpanded = !chat.sideBarExpanded;
		
		if (chat.sideBarExpanded) {
			$("#side-bar").show("fast", function() {
				$("#sideBarTitleRight").css("left", "160px");
				$("#menu").show();
			});
		} else {
			$("#side-bar").hide("fast", function(){
				$("#sideBarTitleRight").css("left", "0px");
				$("#menu").show();
			});
		}
	};
	
	this.logout = function() {
		$("#logoutForm").submit();
	}
	
	this.getActiveUsers = function() {
		var url = $HOME_PATH + "/user/activeUsers";

		var submitStatus = $http({
			method: 'GET',
			url: url,
			headers : {
				'X-XSRF-TOKEN': $("meta[name='_csrf']").attr("content")
			}
		});

		submitStatus.success(function(data, status, headers, config) {
			if (data && data[0] && data[0].userName) {
				chat.activeUsers = data;
			}
		});
		
		submitStatus.error(function(data, status, headers, config) {
			chat.activeUsers = [];
		});
	}
	
	this.sendMessage = function() {
		if (chat.message && chat.chatToUser) {
			var message = {
					from: $YOUR_NAME,
					to: chat.chatToUser,
					message: chat.message
			};
			
			var url = $HOME_PATH + "/message/send";

			var submitStatus = $http({
				method: 'POST',
				url: url,
				headers : {
					'X-XSRF-TOKEN': $("meta[name='_csrf']").attr("content")
				},
				data: message
			});

			submitStatus.success(function(data, status, headers, config) {
				if (data && data.statusText == "success") {
					chat.pushMessage(message);
					chat.message = "";
				} else {
					chat.sendFail();
				}
			});
			
			submitStatus.error(function(data, status, headers, config) {
				chat.sendFail();
			});			
			
		} else {
			alert("Well you can't talk to yourself!\nSelect some one to talk to!");
		}
	};
	
	this.getMessages = function() {
		if (!chat.isBusy) {
			chat.isBusy = true;
			var url = $HOME_PATH + "/message/get";
	
			var submitStatus = $http({
				method: 'GET',
				url: url,
				headers : {
					'X-XSRF-TOKEN': $("meta[name='_csrf']").attr("content")
				}
			});
	
			submitStatus.success(function(data, status, headers, config) {
				if (data && data[0] && data[0].message) {
					angular.forEach(data, function(value, key){
						chat.pushMessage(value);
					});
				}
				
				chat.isBusy = false;
			});
			
			submitStatus.error(function(data, status, headers, config) {
				chat.isBusy = false;
			});
		}		
	};
	
	this.pushMessage = function($value) {
		var fromUser = "";
		var keepGoing = true;
		
		if (chat.chatToUser) {
			keepGoing = false;
		}
		
		chat.chatMessages.push($value);
		
		angular.forEach($value, function(value, key){
			if (keepGoing && value) {
				if (value.from && fromUser == "") {
					fromUser = value.fromUser;
				} else if (key == "from" && fromUser == "") {
					fromUser = value;
				} else if (value.fromUser && fromUser != value.fromUser) {
					fromUser = "";
					keepGoing = false;
				} else if (key == "from" && fromUser != value) {
					fromUser = "";
					keepGoing = false;
				}
			}
		});
		
		if (fromUser) {
			chat.setChatToUser(fromUser);
		}
		
		var div = document.getElementById("chatContainer")
		div.scrollTop = div.scrollHeight + 100;
	};
	
	this.sendFail = function() {
		alert("Fail to send message");
	};
	
	this.setChatToUser = function($chatToUser) {
		chat.chatToUser = $chatToUser;
		chat.chatTo = "You are currently chat with >>> " + $chatToUser;
		
		if (chat.sideBarExpanded) {
			chat.sideBarToggle();
		}
	};
	
	this.getActiveUsers();
	
	chat.chatListener = $interval(chat.getMessages, 1000);
}]);