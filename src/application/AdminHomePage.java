package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The AdminHomePage class provides the admin interface with options to manage users,
 * assign roles, invite others, and switch roles.
 */
public class AdminHomePage {
    private final DatabaseHelper databaseHelper;

    /**
     * Constructs an AdminHomePage with the specified database helper.
     *
     * @param databaseHelper the helper used for database operations
     */
    public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Admin Home Page interface with various administrative controls.
     *
     * @param primaryStage the stage on which to set the scene
     * @param user         the currently logged-in user
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(5);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label adminLabel = new Label("Hello, Admin!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> userDropdown = new ComboBox<>();
        ArrayList<String> users = databaseHelper.getAllUsernames();
        userDropdown.getItems().addAll(users);

        Button oneTimePasswordButton = new Button("Set One-Time Password");
        Label otpResultLabel = new Label();

        oneTimePasswordButton.setOnAction(e -> {
            String selectedUser = userDropdown.getValue();
            if (selectedUser == null) {
                otpResultLabel.setText("Please select a user.");
                return;
            }

            String generatedOtp = UUID.randomUUID().toString().substring(0, 6);
            try {
                databaseHelper.setOneTimePassword(selectedUser, generatedOtp);
                otpResultLabel.setText("One-Time Password set for " + selectedUser + ": " + generatedOtp);
            } catch (SQLException ex) {
                otpResultLabel.setText("Error setting OTP.");
                ex.printStackTrace();
            }
        });

        Button inviteButton = new Button("Invite Others");
        inviteButton.setOnAction(a -> new InvitationPage().show(databaseHelper, primaryStage, user));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            databaseHelper.closeConnection();
            new UserLoginPage(databaseHelper).show(primaryStage);
        });

        String username = user.getUserName();
        ArrayList<String> roles = databaseHelper.getUserRoles(username);
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll(roles);
        if (!roles.isEmpty()) {
            roleDropdown.setValue(roles.get(0));
        }
        
        
        Button openAdminTasks = new Button("Click to view open requested admin tasks");
        openAdminTasks.setOnAction(e->
        {
        	showOpenAdminTasks(primaryStage, user);
        });
        Button closedAdminTasks = new Button("Click to view closed requested admin tasks");
        closedAdminTasks.setOnAction(e->
        {
        	showClosedAdminTasks(primaryStage, user);
        });
        
        Button switchRole = new Button("Switch Role");
        switchRole.setOnAction(e -> {
            String selectedRole = roleDropdown.getValue();
            if (selectedRole != null) {
                switch (selectedRole.toLowerCase()) {
                    case "student":
                        new StudentHomePage(databaseHelper).show(primaryStage, user);
                        break;
                    case "staff":
                        new StaffHomePage(databaseHelper).show(primaryStage, user);
                        break;
                    case "instructor":
                        new InstructorHomePage(databaseHelper).show(primaryStage, user);
                        break;
                    case "admin":
                        new AdminHomePage(databaseHelper).show(primaryStage, user);
                        break;
                    case "reviewer":
                        new ReviewerHomePage(databaseHelper).show(primaryStage, user);
                        break;
                    case "user":
                        new UserHomePage(databaseHelper).show(primaryStage);
                        break;
                    default:
                        System.out.println("Unknown role selected: " + selectedRole);
                }
            }
        });

        Button listUsersButton = new Button("List All Users");
        listUsersButton.setOnAction(e -> new UserListPage(databaseHelper, user).show(primaryStage));

        Button assignRoleButton = new Button("Assign User Role");
        assignRoleButton.setOnAction(e -> new AssignRolesPage(databaseHelper).show(primaryStage, user));

        Button deleteButton = new Button("Delete Users");
        deleteButton.setOnAction(e -> new adminDeletePage(databaseHelper).show(primaryStage, user));

        layout.getChildren().addAll(
                adminLabel, userDropdown, oneTimePasswordButton, otpResultLabel,
                listUsersButton, assignRoleButton, inviteButton, deleteButton, roleDropdown, switchRole, openAdminTasks,closedAdminTasks, logoutButton
        );

        Scene adminScene = new Scene(layout, 800, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Page");
        primaryStage.show();
    }
    private void showOpenAdminTasks(Stage primaryStage, User admin) 
	{
		VBox layout = new VBox(5);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	
	    Label titleLabel = new Label("Requested admin tasks");
	    ScrollPane scrollPane = new ScrollPane();
	    VBox taskContainer = new VBox(1);
	    int k = 0;
	    try {
	    	//System.out.println("About to retrieve questions");
	    	ArrayList<String> userNames = databaseHelper.getAllUsernames();
	    	int j = 1;
	    	for(String username : userNames)
	    	{
	    		User u = databaseHelper.getUserByUserName(username);
	    		if(!u.getRoles().contains("Instructor")) {continue;}
	            List<AdminTask> tasks = databaseHelper.getAdminTaskByUser(u.getId());
	           
	            //System.out.println("Retrieved the questions");
	            
	            VBox taskBox = new VBox(10);
	            //int size = questions.size();
	            //System.out.println("questions list is size " + size)
	            for (AdminTask task : tasks) 
	            {
	            	if(task.isResolved()) {continue;}
	            	String uName = task.getUser().getUserName();
	            	
	                String taskString = task.isResolved() ? "✅ " + task.getContent() +  "(" + uName + ")" : task.getContent() + "(" + uName + ")"; 
	                //System.out.println(questionText);
	              
	                
	                Label taskLabel = new Label("Task " + j + ": " + taskString);
	                
	                Button resolvedButton = new Button(task.isResolved() ? "Resolved" : "Mark as Resolved");
	                resolvedButton.setDisable(task.isResolved());
	                
	                resolvedButton.setOnAction(e -> {
	                	try {
	                		databaseHelper.markAdminTaskResolved(task.getId());
	                		showOpenAdminTasks(primaryStage,admin);
	                	}catch(Exception ex)
	                	{
	                		ex.printStackTrace();
	                	}
	                });

			    TextField actionField = new TextField();
	                actionField.setPromptText("Describe admin action...");
	                
	                Button logActionButton = new Button("Log Action");
	                logActionButton.setOnAction(ev -> {
	                    String actionText = actionField.getText();
	                    if (actionText == null || actionText.trim().isEmpty()) {
	                        return;
	                    }
	                    try {
	                        databaseHelper.logAdminTaskAction(task.getId(), actionText);
	                        actionField.clear();
	                    } catch (SQLException ex) {
	                        ex.printStackTrace();
	                    }
	                });

	                VBox actionLogBox = new VBox(5);
	                Label actionLogTitle = new Label("Logged Actions:");
	                actionLogTitle.setStyle("-fx-font-weight: bold;");
	                actionLogBox.getChildren().add(actionLogTitle);

	                try {
	                    List<String> actions = databaseHelper.getAdminTaskActions(task.getId());
	                    for (String action : actions) {
	                        Label actionLabel = new Label(action);
	                        actionLabel.setStyle("-fx-font-size: 11;");
	                        actionLogBox.getChildren().add(actionLabel);
	                    }
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                }
	                //Hbox taskRow = new Hbox(10,taskLabel,revo)
	                taskBox.getChildren().addAll(taskLabel,resolvedButton, actionField, logActionButton, actionLogBox);
	                taskBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #f5f5f5;");
	                j++;
	            }
	            taskContainer.getChildren().add(taskBox);
	            
	    	}
	     } catch (Exception ex) {
	           System.out.println("ERROR processing task at index " + k + ": " + ex.getMessage());
	           ex.printStackTrace();
	    }
	
	    scrollPane.setContent(taskContainer);
	    scrollPane.setFitToWidth(true);  
	
	    Label resultLabel = new Label();
	    
	    
	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> show(primaryStage, admin));
	
	    layout.getChildren().addAll(titleLabel, scrollPane, resultLabel, backButton);
	    primaryStage.setScene(new Scene(layout, 800, 400));
	}
    
    private void showClosedAdminTasks(Stage primaryStage, User admin) 
	{
		VBox layout = new VBox(5);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	
	    Label titleLabel = new Label("Requested admin tasks");
	    ScrollPane scrollPane = new ScrollPane();
	    VBox taskContainer = new VBox(1);
	    int k = 0;
	    try {
	    	//System.out.println("About to retrieve questions");
	    	ArrayList<String> userNames = databaseHelper.getAllUsernames();
	    	int j = 1;
	    	for(String username : userNames)
	    	{
	    		User u = databaseHelper.getUserByUserName(username);
	    		if(!u.getRoles().contains("Instructor")) {continue;}
	            List<AdminTask> tasks = databaseHelper.getAdminTaskByUser(u.getId());
	           
	            //System.out.println("Retrieved the questions");
	            
	            VBox taskBox = new VBox(10);
	            //int size = questions.size();
	            //System.out.println("questions list is size " + size)
	            for (AdminTask task : tasks) 
	            {
	            	if(!task.isResolved()) {continue;}
	            	String uName = task.getUser().getUserName();
	            	
	                String taskString = task.isResolved() ? "✅ " + task.getContent() +  "(" + uName + ")" : task.getContent() + "(" + uName + ")"; 
	                //System.out.println(questionText);
	              
	                
	                Label taskLabel = new Label("Task " + j + ": " + taskString);
	                
	                
	               
	                //Hbox taskRow = new Hbox(10,taskLabel,revo)
	                taskBox.getChildren().addAll(taskLabel);
	                taskBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #f5f5f5;");
	                j++;
	            }
	            taskContainer.getChildren().add(taskBox);
	            
	    	}
	     } catch (Exception ex) {
	           System.out.println("ERROR processing task at index " + k + ": " + ex.getMessage());
	           ex.printStackTrace();
	    }
	
	    scrollPane.setContent(taskContainer);
	    scrollPane.setFitToWidth(true);  
	
	    Label resultLabel = new Label();
	    
	    
	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> show(primaryStage, admin));
	
	    layout.getChildren().addAll(titleLabel, scrollPane, resultLabel, backButton);
	    primaryStage.setScene(new Scene(layout, 800, 400));
	}
}
