package application;

/**
 * Represents feedback given by a reviewer on an answer submitted by a student.
 */
public class Feedback {
    private int id;
    private User student;
    private int reviewerId;
    private int answerId;
    private String content;

    /**
     * Constructs a Feedback object.
     *
     * @param id         the unique ID of the feedback
     * @param student    the student who submitted the answer
     * @param reviewerId the ID of the reviewer who provided the feedback
     * @param answerId   the ID of the answer being reviewed
     * @param content    the feedback content
     */
    public Feedback(int id, User student, int reviewerId, int answerId, String content) {
        this.id = id;
        this.student = student;
        this.reviewerId = reviewerId;
        this.answerId = answerId;
        this.content = content;
    }

    /**
     * Gets the feedback ID.
     *
     * @return the feedback ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the student who submitted the answer.
     *
     * @return the student
     */
    public User getStudent() {
        return student;
    }

    /**
     * Gets the ID of the reviewer who gave the feedback.
     *
     * @return the reviewer ID
     */
    public int getReviewerId() {
        return reviewerId;
    }

    /**
     * Gets the ID of the answer being reviewed.
     *
     * @return the answer ID
     */
    public int getAnswerId() {
        return answerId;
    }

    /**
     * Gets the content of the feedback.
     *
     * @return the feedback content
     */
    public String getContent() {
        return content;
    }
}
