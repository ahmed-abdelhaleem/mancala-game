package com.bol.mancala.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.bol.mancala.model.ActionRequest;
import com.bol.mancala.model.ActionResponse;
import com.bol.mancala.service.MancalaService;

@Controller
public class MancalaController {

	@Autowired
	private MancalaService mancalaService;

	@MessageMapping("/action")
	@SendToUser("/queue/client")
	public ActionResponse play(@Payload ActionRequest action, SimpMessageHeaderAccessor headerAccessor)
			throws ExecutionException {

		String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();

		return mancalaService.getActionResponse(sessionId, action.getPitNumber());
	}

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
	}
}
