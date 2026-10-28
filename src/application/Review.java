package application;

/**
 * Represents a review posted by a reviewer for a particular answer.
 * A review may also be a reply to another review.
 */
public class Review {
    private int id;
    private String content;
    private User reviewer;
    private Answer answer;
    private Review originalReview;

    /**
     * Constructs a Review object with the specified details.
     *
     * @param id The unique identifier for the review
     * @param content The content of the review
     * @param reviewer The user who posted the review
     * @param answer The answer that is being reviewed
     * @param originalReview The original review, if this is a reply to another review
     */
    public Review(int id, String content, User reviewer, Answer answer, Review originalReview) {
        this.id = id;
        this.content = content;
        this.reviewer = reviewer;
        this.answer = answer;
        this.originalReview = originalReview;
    }

    /**
     * Gets the unique identifier of the review.
     * 
     * @return The id of the review
     */
    public int getId() { return id; }

    /**
     * Gets the content of the review.
     * 
     * @return The content of the review
     */
    public String getContent() { return content; }

    /**
     * Gets the reviewer who posted the review.
     * 
     * @return The reviewer who posted the review
     */
    public User getReviewer() { return reviewer; }

    /**
     * Gets the answer that is being reviewed.
     * 
     * @return The answer that is being reviewed
     */
    public Answer getAnswer() { return answer; }

    /**
     * Gets the original review if this review is a reply to another review.
     * 
     * @return The original review, or null if this review is not a reply
     */
    public Review getOriginalReview() { return originalReview; }

    /**
     * Sets the content of the review.
     * 
     * @param content The new content for the review
     */
    public void setContent(String content) { this.content = content; }
}
