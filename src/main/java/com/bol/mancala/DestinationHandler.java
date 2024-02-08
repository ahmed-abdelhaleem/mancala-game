package com.bol.mancala;

import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.user.UserDestinationMessageHandler;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.stereotype.Component;

@Component
public class DestinationHandler extends UserDestinationMessageHandler{

	public DestinationHandler(SubscribableChannel clientInboundChannel, SubscribableChannel brokerChannel,
			UserDestinationResolver resolver) {
		super(clientInboundChannel, brokerChannel, resolver);
	}

}
