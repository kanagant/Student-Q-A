package application;

public class ReviewerScorecard {
    private int id;
    private User reviewer;
    private User instructor;
    private String feedback;
    private int score;

    // Constructor
    public ReviewerScorecard(int id, User reviewer, User instructor, String feedback, int score) {
        this.id = id;
        this.reviewer = reviewer;
        this.instructor = instructor;
        this.feedback = feedback;
        this.score = score;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public User getInstructor() {
        return instructor;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getScore() {
        return score;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
