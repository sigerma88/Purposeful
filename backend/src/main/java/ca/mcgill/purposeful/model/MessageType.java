package ca.mcgill.purposeful.model;

/**
 * 3 types of messages (Chat for a conversation message, connect for when a user joins the
 * conversation, disconnect for when a user leaves the conversation)
 *
 * @author Wassim Jabbour
 */
public enum MessageType {
  CHAT, // Has contents
  CONNECT, // When a user connects to the conversation (X joined the conversation)
  DISCONNECT // When a user disconnects from the conversation (X left the conversation)
}
