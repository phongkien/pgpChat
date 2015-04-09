(function(){
	'use strict';
	var app = angular.module('pgpChat');
	app.controller('chatController', ['$scope', '$http', '$interval', 'chatService', '$document', function($scope, $http, $interval, chatService, $document) {
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
		this.passPhrase = "";
		this.userId = "";
		
		angular.element(document).ready(function(){
			chat.getPassPhrase();
		});
		
		this.sideBarToggle = function() {
			$("#menu").hide();
			chat.sideBarExpanded = !chat.sideBarExpanded;
			this.getActiveUsers();
			
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
			chat.activeUsers = [];
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
		
		this.getFriendPublicKey = function(friendName) {
			var url = $HOME_PATH + "/friend/public/key/get";
	
			var submitStatus = $http({
				method: 'GET',
				url: url,
				headers : {
					'X-XSRF-TOKEN': $("meta[name='_csrf']").attr("content")
				}
			});
	
			submitStatus.success(function(data, status, headers, config) {
				//TODO
			});
			
			submitStatus.error(function(data, status, headers, config) {
				//TODO
			});
		};
		
		this.getPassPhrase = function() {
			var url = $HOME_PATH + "/chatuser/info/get";
			
			var submitStatus = $http({
				method: 'GET',
				url: url,
				headers : {
					'X-XSRF-TOKEN': $("meta[name='_csrf']").attr("content")
				}
			});
	
			submitStatus.success(function(data, status, headers, config) {
				if (data && data.statusText == "success") {
					chat.passPhrase = data.statusValue;
					chat.userId = data.userId;
				} else {
					chat.showPassPhraseError();
				}
			});
			
			submitStatus.error(function(data, status, headers, config) {
				chat.showPassPhraseError();
			});
		};
		
		this.showPassPhraseError = function() {
			//TODO show better message
			alert("Unable to retrieve passPhrase, message will not be encrypted");
		}
		
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
		
		this.sendStompMessage = function() {
			var message = {
					from: $YOUR_NAME,
					to: chat.chatToUser,
					message: chat.message
			};
			
			chatService.send(message);
			chat.message = "";
		};
		
		chatService.receive().then(null, null, function(message) {
			chat.chatMessages.push(message);
			chat.flash();
		});
		
		var interval = "";
		
		this.flash = function() {
			if (interval) {
				$interval.cancel(interval);
			}
			
			var originalTitle = $document[0].title;
			var currentTitle = "";
			interval = $interval(setTitle, 1000);
			
			function setTitle() {
				if (currentTitle == originalTitle) {
					currentTitle = "";
				} else {
					currentTitle = originalTitle;
				}
				
				$document[0].title = currentTitle;
			};
			
			$document[0].onmousemove = function() {
				$interval.cancel(interval);
				$document[0].title = originalTitle;
			};
		};
		
		this.getActiveUsers();
	}])
})();