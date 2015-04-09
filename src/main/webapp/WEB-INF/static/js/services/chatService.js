(function() {
	'use strict';
	var app = angular.module('pgpChat');
	
	//borrow from http://g00glen00b.be/spring-angular-sockjs/
	app.service('chatService', function($q, $timeout) {
		var service = {};
		var listener = $q.defer();
		var socket = {
			client: null, stomp: null
		};
		
		var headers = {
			login: 'pgpChat',
			passcode: 'stompover',
			'X-XSRF-TOKEN': $("meta[name='_csrf']").attr("content")
		};
		
		var messageIds = [];
		
		service.RECONNECT_TIMEOUT= 15000;
		service.SOCKET_URL = $HOME_PATH + "/stomp/message/send";
		service.CHAT_TOPIC = "/user/" + $YOUR_NAME + "/stomp/message/broadcast/get";
		service.CHAT_BROKER = $HOME_PATH + "/stomp/message/send";
		
		service.receive = function() {
			return listener.promise;
		};
		
		service.send = function(message) {
			var id = Math.floor(Math.random() * 10000000);
			message.id = id;
			socket.stomp.send(service.CHAT_BROKER,
					{
						priority: 9
					}, JSON.stringify(message)
			);
			
			messageIds.push(id);
		};
		
		var reconnect = function() {
			$timeout(function(){
				initialize();
			}, this.RECONNECT_TIMEOUT);
		};
		
		var getMessage = function(data) {
			var message = JSON.parse(data), out = {};
			out.message = message.message;
			out.from = message.from;
			out.to = message.to;
			out.time = new Date(message.time);
			if ($.inArray(message.id, messageIds)) {
				out.self = true;
				messageIds = $.grep(messageIds, function(value){
					return value != message.id;
				});
			}
			
			return out;
		};
		
		var startListener = function() {
			socket.stomp.subscribe(service.CHAT_TOPIC, function(data){
				listener.notify(getMessage(data.body));
			});
		};
		
		var initialize = function() {
			socket.client = new SockJS(service.SOCKET_URL);
			socket.stomp = Stomp.over(socket.client);
			socket.stomp.connect(headers, startListener);
			socket.stomp.onclose = reconnect;
		};
		
		var generateKeyPair = function(userName) {
			
		};
		
		initialize();
		
		return service;
	})
})();