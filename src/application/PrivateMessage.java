package application;

/**
 * Represents a private message in a conversation between users.
 */
public class PrivateMessage {
    /** Unique identifier for the message. */
    private int id;

    /** ID of the conversation this message belongs to. */
    private int conversationId;

    /** The student (user) associated with this message. */
    private User student;

    /** ID of the user who sent the message. */
    private int senderId;

    /** ID of the user who received the message. */
    private int recieverId;

    /** The textual content of the message. */
    private String content;

    /**
     * Constructs a new PrivateMessage object.
     *
     * @param id            the unique message ID
     * @param conversationId the ID of the conversation
     * @param student       the student (user) involved
     * @param senderId      the ID of the sender
     * @param recieverId    the ID of the receiver
     * @param content       the content of the message
     */
    public PrivateMessage(int id, int conversationId, User student, int senderId, int recieverId, String content) {
        this.id = id;
        this.conversationId = conversationId;
        this.student = student;
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.content = content;
    }

    /**
     * Gets the message ID.
     *
     * @return the message ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the conversation ID this message is part of.
     *
     * @return the conversation ID
     */
    public int getConversationId() {
        return conversationId;
    }

    /**
     * Gets the student (user) associated with the message.
     *
     * @return the student user object
     */
    public User getStudent() {
        return student;
    }

    /**
     * Gets the sender's user ID.
     *
     * @return the sender's ID
     */
    public int getSenderId() {
        return senderId;
    }

    /**
     * Gets the receiver's user ID.
     *
     * @return the receiver's ID
     */
    public int getRecieverId() {
        return recieverId;
    }

    /**
     * Gets the content of the message.
     *
     * @return the message content
     */
    public String getContent() {
        return content;
    }
}
