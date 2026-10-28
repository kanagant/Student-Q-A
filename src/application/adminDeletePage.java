
package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional; // Import required for Optional<ButtonType>

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * UI page for admin users to delete other users from the system.
 * Displays a dropdown of all registered usernames and allows the admin to delete a selected user.
 * After deletion, the page redirects to the admin home page.
 * 
 * 
 */

public class adminDeletePage { // Class name should start with uppercase
    private final DatabaseHelper databaseHelper;

    /**
     * Constructs the Admin Delete Page with a reference to the database helper.
     * 
     * @param databaseHelper Helper for database operations
     */
    public adminDeletePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * Displays the Admin Delete Page allowing the admin to delete a user from the database.
     * If deletion is confirmed, the user is removed and the admin is redirected to the home page.
     * 
     * @param primaryStage The primary JavaFX stage
     * @param user The currently logged-in admin user
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(10); // Add spacing between elements
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label for title
        Label adminLabel = new Label("Admin Delete Page");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Dropdown to select user
        ComboBox<String> userDropdown = new ComboBox<>();
        ArrayList<String> users = databaseHelper.getAllUsernames();
        userDropdown.getItems().addAll(users);

        // Labels for success and error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Label userDeleted = new Label();
        userDeleted.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");

        // Delete button
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(a -> {
            String userName = userDropdown.getValue();
            
            if (userName.isEmpty()) {
                errorLabel.setText("Please enter a username.");
                return;
            }

            // Show confirmation dialog
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, 
                    "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
            confirmDelete.setTitle("Confirm Deletion");
            confirmDelete.setHeaderText(null);

            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                String error = databaseHelper.deleteUser(userName);
                
                if (!error.isEmpty()) {
                    errorLabel.setText(error);
                } else {
                    System.out.println("User " + userName + " deleted successfully.");
                    userDeleted.setText("User " + userName + " deleted successfully.");
                    
                    // Go back to admin home page after successful deletion
                    new AdminHomePage(databaseHelper).show(primaryStage, user);
                }
            }
        });

        // Back button to return to admin home page
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new AdminHomePage(databaseHelper).show(primaryStage, user));

        layout.getChildren().addAll(adminLabel, userDropdown, deleteButton, errorLabel, userDeleted, backButton);

        Scene adminScene = new Scene(layout, 800, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Delete Page");
    }
}