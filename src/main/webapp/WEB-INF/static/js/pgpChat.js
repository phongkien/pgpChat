var app = angular.module('pgpChat', [ 'ngAnimate']);

app.controller("loginController", function($scope, $http, $filter) {
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
});

app.controller('chatController', function($scope, $http) {
	this.sideBarExpanded = false;
	
	this.sideBarToggle = function() {
		$("#menu").hide();
		this.sideBarExpanded = !this.sideBarExpanded;
		
		if (this.sideBarExpanded) {
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
});