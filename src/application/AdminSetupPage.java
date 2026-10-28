package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import databasePart1.DatabaseHelper;

/**
 * The AdminSetupPage class handles the initial administrator setup process.
 * It allows the first user to create an admin account with username, password, and email.
 */
public class AdminSetupPage {

    private final DatabaseHelper databaseHelper;

    /**
     * Constructs the AdminSetupPage with the given DatabaseHelper instance.
     *
     * @param databaseHelper the helper for database operations
     */
    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the admin setup interface on the primary stage.
     *
     * @param primaryStage the main application stage
     */
    public void show(Stage primaryStage) {
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin Username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        emailField.setMaxWidth(250);

        Label userNameErrorLabel = new Label();
        Label passwordErrorLabel = new Label();
        Label emailErrorLabel = new Label();

        userNameErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        passwordErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        emailErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button setupButton = new Button("Setup");

        setupButton.setOnAction(a -> {
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();

            String userNameError = UserNameRecognizer.checkForValidUserName(userName);
            String passwordError = PasswordEvaluator.evaluatePassword(password);

            boolean hasErrors = false;

            if (!userNameError.isEmpty()) {
                userNameErrorLabel.setText("Username Error: " + userNameError);
                hasErrors = true;
            } else {
                userNameErrorLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                userNameErrorLabel.setText("Username is valid.");
            }

            if (!passwordError.isEmpty()) {
                passwordErrorLabel.setText("Password Error: " + passwordError);
                hasErrors = true;
            } else {
                passwordErrorLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                passwordErrorLabel.setText("Password is valid.");
            }

            if (email.isEmpty() || !email.contains("@")) {
                emailErrorLabel.setText("Invalid Email Address");
                hasErrors = true;
            } else {
                emailErrorLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                emailErrorLabel.setText("Email is valid.");
            }

            if (hasErrors) return;

            try {
                ArrayList<String> roles = new ArrayList<>(Arrays.asList("admin"));
                databaseHelper.increment_num_users();
                User user = new User(databaseHelper.get_num_users(), userName, password, email, roles);
                databaseHelper.register(user, roles);
                System.out.println("Administrator setup completed.");
                new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, userNameField, passwordField, emailField, setupButton,
                userNameErrorLabel, passwordErrorLabel, emailErrorLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
