package com.phongkien.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.phongkien.model.MessageModel;
import com.phongkien.utils.UtilsFunctions;

@Controller
@RequestMapping("/")
public class StompMessageController {
	private SimpMessagingTemplate messageTemplate;
	
	@Autowired
	public StompMessageController(SimpMessagingTemplate template) {
		this.messageTemplate = template;
	}
	
	@MessageMapping("/stomp/message/send")
	public void stompSendMessage(MessageModel message, Principal principal) throws Exception {
		MessageModel broadCastMessage = new MessageModel();
		String fromUser = principal.getName();

		if (!UtilsFunctions.isNull(fromUser)) {
			message.copyTo(broadCastMessage);
			broadCastMessage.setFrom(fromUser);
		}

		//return broadCastMessage;
		messageTemplate.convertAndSendToUser(message.getTo(), "/stomp/message/broadcast/get", broadCastMessage);
	}
}
