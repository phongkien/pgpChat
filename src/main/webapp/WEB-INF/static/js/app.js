// Code goes here
var app = angular.module('pgpChat', ['ngAnimate']);
app.controller("loginController", function($scope) {
  $scope.registered = false;
  $scope.username = "";
  $scope.password = "";
  $scope.verifypassword = "";
  $scope.error = false;
  
  $scope.doRegister = function() {
    $scope.registered = true;
    $("#btnRegister").attr("type", "submit");
  };
  
  $scope.isRegistered = function() {
    return $scope.registered;
  }
  
  $scope.hasError = function() {
    return $scope.error;
  }
  
  $scope.login = function() {
    var submitValue = "";
    var url = "";
    
    $scope.error = false;
    $("#pnlPassword").removeClass("has-error");
    $("#pnlVerifyPassword").removeClass("has-error");
    
    if ($scope.isRegistered() && $scope.username !== "" && $scope.password !== "" && $scope.password === $scope.verifypassword) {
      submitalue = {
        username: $scope.username,
        password: $scope.password,
        verifyPassword: $scope.verifypassword
      };
      
      url = "/pgpChat/register";
    } else if (!$scope.isRegistered() && $scope.username !== "" && $scope.password !== "") {
      submitValue = {
        username: $scope.username,
        password: $scope.password
      };
      
      url = "/pgpChat/login";
    } else if ($scope.isRegistered() && $scope.password !== $scope.verifypassword) {
      $scope.error = true;
      $("#errorMessage").html("Password mis-matched.");
      $("#pnlPassword").addClass("has-error");
      $("#pnlVerifyPassword").addClass("has-error");
    }
    
    if (submitValue !== "") {
      var submitStatus = $http.post(url, submitValue);
      submitStatus.success(function(dataFromServer, status, headers, config){
        alert("success!");
      });
      
      submitStatus.error(function(dataFromServer, status, headers, config) {
        alert("crap!");
      });
    }
  }
});