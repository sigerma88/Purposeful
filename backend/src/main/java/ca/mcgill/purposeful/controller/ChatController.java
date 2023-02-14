package ca.mcgill.purposeful.controller;

import ca.mcgill.purposeful.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Controller that handles all the chat messages sent from the client to the server, prefixed by
 * /app The reason why it's a controller and not a REST controller is because it doesn't expose any
 * endpoints, from my understanding of it All the messages sent from clients with a destination
 * starting with /app will be routed to these message handling methods annotated
 * with @MessageMapping. For example, a message with destination /app/chat.sendMessage will be
 * routed to the sendMessage() method.
 *
 * @author Wassim Jabbour
 */
@Controller
public class ChatController {

  /**
   * The @MessageMapping annotation ensures that if a message is sent to destination
   * /app/chat.sendMessage, then the sendMessage() method is called. The @SendTo annotation ensures
   * that if a message is returned from sendMessage(), it is sent to the message broker which will
   * itself broadcast it to all the connected clients. This endpoint basically does nothing but is
   * necessary for the server to be able to receive messages from one client and broadcast it to all
   * connected clients Basically, when the sender sends a message, it waits for the broker to
   * broadcast it back to all the connected clients including itself before displaying the message
   * in the frontend
   *
   * @param chatMessage The message sent from the client - @Payload annotation ensures that the
   *     message is deserialized from JSON to a ChatMessage object and that the header containing
   *     message type, etc... is deleted
   * @return The message that will be sent to the message broker and then consequently to the client
   * @author Wassim Jabbour
   */
  @MessageMapping("/chat.send")
  @SendTo("/topic/public")
  public ChatMessage sendMessage(@Payload final ChatMessage chatMessage) {
    return chatMessage;
  }

  /**
   * Similar to the sendMessage() method, but this one is called when a new user joins the chat In
   * this case, we not only need to take the chatMessage as input (Payload of the received JSON),
   * but also a header accessor that allows us to access and modify the session attributes of the
   * message Using the header accessor, we can add the username of the user to the session
   * attributes The reason why we don't need to do this for the sendMessage() method is because the
   * username is already in the session attributes from the previous call to this method (A user
   * must join the chat before sending a message)
   *
   * @param chatMessage The message received from the client
   * @param headerAccessor An object that allows us to access and modify the session attributes of
   *     the message (And all subsequent messages sent by the same client)
   * @return The message that will be sent to the message broker and then consequently to the client
   * @author Wassim Jabbour
   */
  @MessageMapping("/chat.newUser")
  @SendTo("/topic/public")
  public ChatMessage newUser(
      @Payload final ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

    // Makes sure every user gets registered correctly with a username
    // This is done by adding the username to the session attributes
    // The session attributes are accessible from the headerAccessor
    // The username is then used in the frontend to display the name of the user who sent the
    // message
    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

    return chatMessage;
  }
}
