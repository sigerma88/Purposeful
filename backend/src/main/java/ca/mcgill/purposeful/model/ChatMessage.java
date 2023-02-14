package ca.mcgill.purposeful.model;

/**
 * Model class to represent a message
 *
 * @author Wassim Jabbour
 */
public class ChatMessage {

  private MessageType type; // Type of message

  private String content; // Content of the message

  // TODO: change to an actual user (For now just a string sent from the frontend, should be a way
  // to integrate this with tokens)
  private String sender; // Sender of the message

  // TODO: Change to a Time type (For now just a string, simpler) and save to database
  private String time;

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
