package application;

import java.util.List;

/**
 * Represents a question posted by a user.
 * This class contains the details of a question such as its content, the user who posted it,
 * the associated tags, and whether the question has been resolved.
 */
public class AdminTask {
    private int id;
    private String content;
    private User user;
    private boolean isResolved;

    /**
     * Constructs a Question object with the specified details.
     *
     * @param id The unique identifier for the question
     * @param content The content of the question
     * @param user The user who posted the question
     * @param tags The tags associated with the question
     */
    public AdminTask(int id, String content, User user) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.isResolved = false;
    }

    /**
     * Gets the unique identifier of the question.
     * 
     * @return The id of the question
     */
    public int getId() { return id; }

    /**
     * Gets the content of the question.
     * 
     * @return The content of the question
     */
    public String getContent() { return content; }

    /**
     * Gets the user who posted the question.
     * 
     * @return The user who posted the question
     */
    public User getUser() { return user; }

    /**
     * Gets the tags associated with the question.
     * 
     * @return The tags of the question
     */
    
    public boolean isResolved() { return isResolved; }

    /**
     * Sets the resolved status of the question.
     * 
     * @param resolved The new resolved status of the question
     */
    public void setResolved(boolean resolved) { this.isResolved = resolved; }

    /**
     * Returns a string representation of the question.
     * 
     * @return A string representing the question, including its resolved status and tags
     */
     public void setContent(String content) {this.content= content; }
    @Override
    public String toString() {
        return (isResolved ? "✅ " : "") + content;
    }
}
