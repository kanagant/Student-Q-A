package application;

import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, email, and roles.
 */
public class User {
    private int id = -1;
    private String userName;
    private String password;
    private String email; // Added email field
    private List<String> roles; // Changed role from String to List<String> to support multiple roles
    private List<Question> questions;
    private List<Answer> answers;
    
    /**
     * Constructs a new User object with the specified details.
     * 
     * @param id The unique identifier of the user
     * @param userName The username of the user
     * @param password The password of the user
     * @param email The email address of the user
     * @param roles The list of roles assigned to the user
     */
    public User(int id, String userName, String password, String email, List<String> roles) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.roles = new ArrayList<>(roles); // Ensure safe copying of roles
        this.questions = new ArrayList<>();
        this.answers = new ArrayList<>();
    }
    
    /**
     * Constructs a new User object with only an id and userName.
     * This constructor is intended for users that are not yet fully initialized.
     * 
     * @param id The unique identifier of the user
     * @param userName The username of the user
     */
    public User(int id, String userName) {
        this.id = id;
        this.userName = userName;
        this.password = "";
    }

    /**
     * Sets the roles of the user.
     * 
     * @param roles The list of roles to be assigned to the user
     */
    public void setRoles(List<String> roles) {
        this.roles = new ArrayList<>(roles);
    }

    /**
     * Adds a single role to the user, if not already present.
     * 
     * @param role The role to be added to the user
     */
    public void addRole(String role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    /**
     * Removes a specific role from the user.
     * 
     * @param role The role to be removed from the user
     */
    public void removeRole(String role) {
        roles.remove(role);
    }

    /**
     * Gets the unique identifier of the user.
     * 
     * @return The id of the user
     */
    public int getId() { return id; }

    /**
     * Gets the username of the user.
     * 
     * @return The username of the user
     */
    public String getUserName() { return userName; }

    /**
     * Gets the password of the user.
     * 
     * @return The password of the user
     */
    public String getPassword() { return password; }

    /**
     * Gets the email address of the user.
     * 
     * @return The email address of the user
     */
    public String getEmail() { return email; }

    /**
     * Gets the list of roles assigned to the user.
     * 
     * @return The list of roles assigned to the user
     */
    public List<String> getRoles() { return roles; }

    /**
     * Gets the list of questions posted by the user.
     * 
     * @return The list of questions posted by the user
     */
    public List<Question> getQuestions() { return questions; }

    /**
     * Gets the list of answers posted by the user.
     * 
     * @return The list of answers posted by the user
     */
    public List<Answer> getAnswers() { return answers; }

    /**
     * Sets the unique identifier of the user.
     * 
     * @param id The new id of the user
     */
    public void setId(int id) { this.id = id; }

    /**
     * Adds a question to the user's list of questions.
     * 
     * @param question The question to be added to the user's list
     */
    public void addQuestion(Question question) {
        questions.add(question);
    }

    /**
     * Adds an answer to the user's list of answers.
     * 
     * @param answer The answer to be added to the user's list
     */
    public void addAnswer(Answer answer) {
        answers.add(answer);
    }
}
