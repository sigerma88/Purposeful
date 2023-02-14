package ca.mcgill.purposeful.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration for websockets
 * INSPIRED FROM: https://www.youtube.com/watch?v=U4lqTmFmbAM&ab_channel=ShaneLee
 * THERE IS A FRONTEND CODE FILE LINKED THAT WE CAN ALSO USE AS A STARTING POINT
 *
 * @author Wassim Jabbour
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

  /**
   * Register the endpoint for listening to incoming messages by the server
   * @param registry Registry of endpoints
   *
   * @author Wassim Jabbour
   */
  @Override
  public void registerStompEndpoints(final StompEndpointRegistry registry) {

    // Set the endpoint for listening to incoming messages by the server to be /chat-example
    // This is what the Stomp library used in the frontend will connect to before sending requests
    // withSockJS() designates that we'll be using the sockJS frontend library to send in web-socket
    // like objects
    // This enables fallback options for browsers that don't support websockets
    registry.addEndpoint("/chat-example").withSockJS();
  }

  /**
   * Configure the message broker
   * @param registry Registry of message brokers
   *
   * @author Wassim Jabbour
   */
  @Override
  public void configureMessageBroker(final MessageBrokerRegistry registry) {

    // From the client, messages that are sent with the prefix /app will be routed to the
    // message-handling controller methods
    registry.setApplicationDestinationPrefixes("/app");

    // From the client, messages that are sent with the prefix /topic will be routed to the message
    // broker
    // The message broker will broadcast messages to all the connected clients that are subscribed
    // to a particular topic
    // Simple broker means that the broker uses a simple in-memory queue to store messages
    // To use a more robust message broker, we can switch to a full-fledged message broker like
    // RabbitMQ
    // That will require a lot more configuration, but it's a good idea to do so if we're expecting
    // a lot of traffic
    // It will also need launching a separate server for the message broker
    registry.enableSimpleBroker("/topic");
  }
}
