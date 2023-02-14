package ca.mcgill.purposeful.controller;

import ca.mcgill.purposeful.model.ChatMessage;
import ca.mcgill.purposeful.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Listens to websocket events on the configured web socket server
 *
 * @author Wassim Jabbour
 */
@Component
public class WebSocketEventListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

  @Autowired private SimpMessageSendingOperations sendingOperations;

  /**
   * When a user connects to the websocket, this method is called The difference with ChatController
   * is that this is not explicitely called by the client, but rather by StompJS in the frontend
   * when a connexion is established Following this call (Connexion), the client will send a message
   * to the server with the type CONNECT and the username of the user to the /chat.newUser
   * controller endpoint For now this just prints to the console that a new web socket connexion was
   * established
   *
   * @param event The connection event
   * @author Wassim Jabbour
   */
  @EventListener
  public void handleWebSocketConnectListener(final SessionConnectedEvent event) {
    LOGGER.info("Received a new web socket connection");
  }

  /**
   * Same as above but for when a user disconnects from the websocket (Without a request, just
   * closes the browser for example, hence why separate from the controller)
   *
   * @param event The disconnect event
   * @author Wassim Jabbour
   */
  @EventListener
  public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
    final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    final String username =
        (String)
            headerAccessor
                .getSessionAttributes()
                .get("username"); // Map of <String, Object>, needs to be casted

    // Create a disconnect message with the username of the person that disconnected
    final ChatMessage chatMessage = new ChatMessage();
    chatMessage.setType(MessageType.DISCONNECT);
    chatMessage.setSender(username);

    sendingOperations.convertAndSend(
        "/topic/public", chatMessage); // Send a message to all users with the disconnect message
  }
}
