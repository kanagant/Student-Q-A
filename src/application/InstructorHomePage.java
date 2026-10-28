package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InstructorHomePage class represents the user interface for the instructor user.
 * This page displays a welcome message for the instructor and provides options
 * to review users' questions and answers, switch roles, and log out.
 */
public class InstructorHomePage {
		
	private DatabaseHelper databaseHelper;
	 /**
     * Constructs an InstructorHomePage instance.
     * 
     * @param databaseHelper The DatabaseHelper instance for database interactions.
     */
	public InstructorHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        //this.user = user;
    }
	/**
     * Displays the instructor page in the provided primary stage.
     * 
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The logged-in user.
     */
    public void show(Stage primaryStage, User user) 
    {
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the instructor 
	    Label instructorLabel = new Label("Hello, Instructor!");
	    
	    instructorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	 // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            databaseHelper.closeConnection();  // Close database connection
            new UserLoginPage(databaseHelper).show(primaryStage);  // Redirect to login
        });
        /** 
         * drop down to select user to get questions and answers
         */
        ComboBox<String> userDropdown = new ComboBox<>();
        ArrayList<String> users = databaseHelper.getAllUsernames();
        for(String userName : users)
        {
        	try{
        		User u = databaseHelper.getUserByUserName(userName);
        		if(u.getRoles().contains("Student"))
        		{
        			userDropdown.getItems().add(userName);
        		}
        	}catch(Exception e)
        	{
        		e.printStackTrace();
        		System.out.println(e.getMessage());
        	}
        }
        
        Button scorecardButton = new Button("Create or Edit Reviewer Scorecard");
        scorecardButton.setOnAction(e -> showScorecardEditor(primaryStage, user));
        layout.getChildren().add(scorecardButton);

        
        Button selectUser = new Button("Please select Student to review their question's and answers");
        
    
        selectUser.setOnAction(e -> 
        {
        	String userName = userDropdown.getValue();
        	try
            {
            	//System.out.println("Trying to get User object for " + userName);
            	User u = databaseHelper.getUserByUserName(userName);
            	if(u == null)
            	{
            		System.out.println("U object is null");
            	}else if(user == null)
            	{
            		System.out.println("User object is null");
            	}
            	
            	showQuestionsAndAnswerPageByUser(primaryStage, u ,user);
            }catch(Exception ex)
            {
            	return;
            }
        });
        
        ComboBox<String> userDropdownForMessages = new ComboBox<>();
        ArrayList<String> users2 = databaseHelper.getAllUsernames();
        for(String userName : users2)
        {
        	if(userName.equals(user.getUserName())) {continue;}
        	try{
        		User u = databaseHelper.getUserByUserName(userName);
        		if( u.getRoles().contains("Instructor") || u.getRoles().contains("Staff") ) 
        		{
        			userDropdownForMessages.getItems().add(userName);
        		}
        	}catch(Exception e)
        	{
        		e.printStackTrace();
        		System.out.println(e.getMessage());
        	}
        }
        
        
        
        Button selectStaffOrInstructor = new Button("Please select a staff member or instructor to view your messages");
        
        selectStaffOrInstructor.setOnAction(e -> 
        {
        	String userName = userDropdownForMessages.getValue();
        	try
            {
            	//System.out.println("Trying to get User object for " + userName);
            	User u = databaseHelper.getUserByUserName(userName);
            	if(u == null)
            	{
            		System.out.println("U object is null");
            	}else if(user == null)
            	{
            		System.out.println("User object is null");
            	}
            	
            	viewPrivateMessages(primaryStage, u, user);
            }catch(Exception ex)
            {
            	return;
            }
        });
     // Switch Role Button
        String username= user.getUserName();
        ArrayList<String> roles= databaseHelper.getUserRoles(username);
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll(roles);
        if (!roles.isEmpty()) {
            roleDropdown.setValue(roles.get(0)); // Select the first role by default
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
        Button RequestAdminTasks = new Button("Click here to request admins to perform a task");
        RequestAdminTasks.setOnAction(e->
        {
        	showRequestAdminTasks(primaryStage, user);
        });
        Button switchRole = new Button("Switch Role");
        switchRole.setOnAction(e -> {String selectedRole = roleDropdown.getValue(); // Get the selected role

        if (selectedRole != null) {
            switch (selectedRole.toLowerCase()) { // Convert to lowercase for case-insensitive matching
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
                    System.out.println("Unknown role selected: " +  selectedRole);
            }
        }
    });
        Label students = new Label();
        
        Button getReviewerRequests = new Button("Click here to view all students requesting to be reviewers");
        getReviewerRequests.setOnAction(e-> {
        	try{
        		List<String> studentsList = databaseHelper.getStudentsRequestingReviewerRole();
        		String studentString = "";
        		for(String student : studentsList)
        		{
        			studentString = studentString + student + "\n";
        		}
        		if(studentString == "")
        		{
        			students.setText("There is no students requesting to be a reviewer at this time\n");
        		}else {
        			students.setText(studentString);
        		}
        	}catch(Exception ex)
        	{
        		System.out.println(ex.getMessage());
        	}
        	
        });
        
        
	    layout.getChildren().addAll(instructorLabel,userDropdown, selectUser, userDropdownForMessages, selectStaffOrInstructor, logoutButton, roleDropdown, switchRole, openAdminTasks, closedAdminTasks, RequestAdminTasks,getReviewerRequests, students);
	    Scene instructorScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(instructorScene);
	    primaryStage.setTitle("instructor Page");
    
	    
    }
    /**
     * Displays the questions and answers of a selected user.
     * 
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param u The user whose questions and answers will be displayed.
     * @param instructor The logged-in instructor.
     */
	    private void showQuestionsAndAnswerPageByUser(Stage primaryStage, User u, User instructor) 
	    {
	        VBox layout = new VBox(20);
	        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	
	        Label titleLabel = new Label("Users Questions List");
	        ScrollPane scrollPane = new ScrollPane();
	        VBox questionContainer = new VBox(10);
	        int i = 1;
	        try {
	        	//System.out.println("About to retrieve questions");
	            List<Question> questions = databaseHelper.getQuestionsByUser(u.getId());
	            //System.out.println("Retrieved the questions");
	            
	            VBox questionBox = new VBox(10);
	            //int size = questions.size();
	            //System.out.println("questions list is size " + size);
	            for (Question q : questions) 
	            {
	                String questionText = q.isResolved() ? "✅ " + q.getContent() : q.getContent(); 
	                //System.out.println(questionText);
	                Label questionLabel = new Label(i + ": " + questionText + " [" + q.getTags() + "]");
	                i++;
	                questionBox.getChildren().addAll(questionLabel);
	                questionBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #f5f5f5;");
	                //questionContainer.getChildren().add(questionBox);
	            }
	            questionContainer.getChildren().add(questionBox);
	         } catch (Exception ex) {
	             System.out.println("ERROR processing question at index " + i + ": " + ex.getMessage());
	             ex.printStackTrace();
	         }
	                
	        
	
	                // Fetch answers for this question
	        Label titleLabelAnswers = new Label("Users Answers List");
	        ScrollPane scrollPaneAnswers = new ScrollPane();
	        VBox answerContainer = new VBox(10);
	        int j = 1;
	          try{
	        	  List<Answer> answers = databaseHelper.getAnswersByUser(u.getId());
	        	  VBox answerBox = new VBox(10);
	          for (Answer a : answers) {
	              String answerText = a.isSolution() ? "✅ " + a.getContent() : a.getContent();
	              String questionText = a.getQuestion().getContent();
	              Label answerLabel = new Label(j + ". Question = " + questionText + "\n	Answer -> " + answerText);
	              j++;
	              answerBox.getChildren().addAll(answerLabel);
	              answerBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #f5f5f5;");
	              
	              }
	          answerContainer.getChildren().add(answerBox);	
	        } catch (SQLException ex) {
	        	System.out.println("ERROR processing question at index " + j + ": " + ex.getMessage());
	            ex.printStackTrace();
	        }
	          
	        scrollPane.setContent(questionContainer);
	        scrollPane.setFitToWidth(true);  
	        scrollPaneAnswers.setContent(answerContainer);
	        scrollPaneAnswers.setFitToWidth(true);
	        Label resultLabel = new Label();
	        
	        Button makeReviewerButton = new Button("Click here to make the student a reviewer");
	        makeReviewerButton.setOnAction(e->
	        {
	        	try {
	                // Retrieve existing roles
	                ArrayList<String> roles = databaseHelper.getUserRoles(u.getUserName());

	                // Add the new role if not already assigned
	                if (!roles.contains("Reviewer")) {
	                    databaseHelper.addUserRole(u.getUserName(), "Reviewer"); // Use SQL method
	                    resultLabel.setText(u.getUserName() + " is now a Reviewer!");
	                } else {
	                    resultLabel.setText(u.getUserName() + " already has the reviewer role.");
	                }
	                if(databaseHelper.requestedReviewer(u))
	                {
	                	databaseHelper.removeReviewerRequest(u);
	                }
	            } catch (SQLException ex) {
	                resultLabel.setText("Error assigning role.");
	                ex.printStackTrace();
	            }
	        });
	        Button backButton = new Button("Back");
	        backButton.setOnAction(e -> show(primaryStage,instructor));
	
	        layout.getChildren().addAll(titleLabel, scrollPane, titleLabelAnswers, scrollPaneAnswers, makeReviewerButton, resultLabel, backButton);
	        primaryStage.setScene(new Scene(layout, 800, 400));
	    }
	    
	    /**
	     * Displays private messages between the instructor and another staff or instructor.
	     * Allows the instructor to send a new private message.
	     *
	     * @param primaryStage the primary stage for displaying scenes
	     * @param u the staff or instructor user to message
	     * @param instructor the logged-in instructor
	     */
	    private void viewPrivateMessages(Stage primaryStage, User u, User instructor)
	    {
	    	VBox layout = new VBox(20);
	        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	
	        Label titleLabel = new Label("Message between you and " + u.getUserName());
	        
	        ScrollPane scrollPane = new ScrollPane();
	        VBox messageContainer = new VBox(10);
	        try {
	        	//System.out.println("About to retrieve questions");
	            List<PrivateMessage> messages = databaseHelper.getMessages(instructor.getId(), u.getId());
	            //System.out.println("Retrieved the questions");
	            
	            VBox messageBox = new VBox(10);
	            //int size = messages.size();

	            for (PrivateMessage pm : messages) 
	            {
	                String message = databaseHelper.getUserById(pm.getSenderId()).getUserName() + ": " + pm.getContent() + "\n"; 
	                //System.out.println(questionText);
	                Label messageLabel = new Label(message);
	                messageBox.getChildren().addAll(messageLabel);
	                messageBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #f5f5f5;");
	                //questionContainer.getChildren().add(questionBox);
	            }
	            if(messages.size() == 0)
	            {
	            	Label noMessages = new Label("No messages between you and " + u.getUserName());
	            	messageBox.getChildren().addAll(noMessages);
	            }
	            messageContainer.getChildren().add(messageBox);
	         } catch (Exception ex) {
	             System.out.println("ERROR processing question at index "+ ex.getMessage());
	             ex.printStackTrace();
	         }
	        
	        
	        scrollPane.setContent(messageContainer);
	        scrollPane.setFitToWidth(true);  
	        
	        Label resultLabel = new Label();
	        Label errorLabel = new Label();
	        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
	        
	        TextField newMessageField = new TextField();
	        newMessageField.setPromptText("Enter your message here");
	        Button submitButton = new Button("Submit");
	        Button backButton = new Button("Back");

	        submitButton.setOnAction(e -> {
	            String content = newMessageField.getText().trim();
	            // **Input Validation**
	            if (content.isEmpty()) {
	                errorLabel.setText("Error: Question cannot be empty.");
	                return;
	            }
	            databaseHelper.sendPrivateMessage(instructor, u, content);
	            viewPrivateMessages(primaryStage, u, instructor);
	        });

	        backButton.setOnAction(e -> show(primaryStage,instructor));
	
	        layout.getChildren().addAll(titleLabel, scrollPane, newMessageField, resultLabel, submitButton, errorLabel, backButton);
	        primaryStage.setScene(new Scene(layout, 800, 400));
	    
	        }
	     

	    /**
	     * Displays all open administrative tasks requested by instructors.
	     *
	     * @param primaryStage the primary stage for displaying scenes
	     * @param instructor the logged-in instructor
	     */

		private void showOpenAdminTasks(Stage primaryStage, User instructor) 
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
		    backButton.setOnAction(e -> show(primaryStage, instructor));
		
		    layout.getChildren().addAll(titleLabel, scrollPane, resultLabel, backButton);
		    primaryStage.setScene(new Scene(layout, 800, 400));
		}
	
		
		 /**
	     * Displays a form to allow an instructor to request an administrative task.
	     *
	     * @param PrimaryStage the primary stage for displaying scenes
	     * @param instructor the logged-in instructor
	     */
		public void showRequestAdminTasks(Stage PrimaryStage, User instructor)
		{
			VBox layout = new VBox(15);
	        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

	        Label titleLabel = new Label("Request Admins to perform a task");
	        TextField taskField = new TextField();
	        taskField.setPromptText("Write out your request here...");

	        Label errorLabel = new Label();
	        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");



	        Button submitButton = new Button("Submit");
	        Button backButton = new Button("Back");

	        submitButton.setOnAction(e -> {
	            String content = taskField.getText().trim();
	            
	            
	            // **Input Validation**
	            if (content.isEmpty()) {
	                errorLabel.setText("Error: Question cannot be empty.");
	                return;
	            }

	            try {
	                User updatedUser = databaseHelper.getUserByUserName(instructor.getUserName());
	                if (updatedUser == null) {
	                    errorLabel.setText("Error: User not found.");
	                    return;
	                }

	                AdminTask task = new AdminTask(0, content, updatedUser);
	                databaseHelper.addAdminTask(task);
	                show(PrimaryStage,instructor);
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        });

	        backButton.setOnAction(e -> show(PrimaryStage,instructor));

	        layout.getChildren().addAll(titleLabel, taskField, submitButton, errorLabel, backButton);
	        PrimaryStage.setScene(new Scene(layout, 800, 400));
	    }
		
		 /**
	     * Displays a list of closed (resolved) administrative tasks.
	     * Allows instructors to reopen or edit closed tasks.
	     *
	     * @param primaryStage the primary stage for displaying scenes
	     * @param admin the logged-in instructor (admin role)
	     */
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
		              
		                HBox taskRow = new HBox(10);
		                Label taskLabel = new Label("Task " + j + ": " + taskString);

		                Button reopenButton = new Button("Reopen");
		                reopenButton.setOnAction(ev -> {
		                    try {
		                        task.setResolved(false);
		                        databaseHelper.markAdminTaskResolved(task.getId(), false);
		                        showClosedAdminTasks(primaryStage, admin); // Refresh view
		                    } catch (SQLException ex) {
		                        ex.printStackTrace();
		                    }
		                });

		                Button editButton = new Button("Edit");
		                editButton.setOnAction(ev -> showEditAdminTaskForm(primaryStage, task, admin));

		                taskRow.getChildren().addAll(taskLabel, reopenButton, editButton);
		                taskBox.getChildren().add(taskRow);
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
		
		/**
	     * Displays a form to edit the content of a closed administrative task.
	     *
	     * @param primaryStage the primary stage for displaying scenes
	     * @param task the administrative task to edit
	     * @param instructor the logged-in instructor
	     */
		private void showEditAdminTaskForm(Stage primaryStage, AdminTask task, User instructor) {
		    VBox layout = new VBox(15);
		    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		    
		    Label instructionLabel = new Label("Edit Admin Task:");
		    TextArea contentArea = new TextArea(task.getContent());
		    contentArea.setPrefRowCount(4);

		    Label resultLabel = new Label();

		    Button saveButton = new Button("Save");
		    saveButton.setOnAction(e -> {
		        String newContent = contentArea.getText().trim();
		        if (!newContent.isEmpty()) {
		            try {
		                boolean success = databaseHelper.updateAdminTask(task.getId(), newContent);
		                if (success) {
		                    resultLabel.setText("Task updated successfully.");
		                } else {
		                    resultLabel.setText("Update failed.");
		                }
		                showClosedAdminTasks(primaryStage, instructor);
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                resultLabel.setText("Error updating task.");
		            }
		        }
		    });

		    Button backButton = new Button("Back");
		    backButton.setOnAction(e -> showClosedAdminTasks(primaryStage, instructor));

		    layout.getChildren().addAll(instructionLabel, contentArea, saveButton, resultLabel, backButton);
		    primaryStage.setScene(new Scene(layout, 800, 400));
		}

		 /**
	     * Displays a form for instructors to create or edit a scorecard for reviewers.
	     * Allows multiple scores and reasons to be entered and calculates an average score.
	     *
	     * @param primaryStage the primary stage for displaying scenes
	     * @param instructor the logged-in instructor
	     */
		 private void showScorecardEditor(Stage primaryStage, User instructor) {
		        VBox layout = new VBox(15);
		        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

		        Label titleLabel = new Label("Create/Edit Reviewer Scorecard");
		        TextField reviewerUsernameField = new TextField();
		        reviewerUsernameField.setPromptText("Enter reviewer username");

		        VBox scoreEntryContainer = new VBox(10);
		        ScrollPane scrollPane = new ScrollPane(scoreEntryContainer);
		        scrollPane.setFitToWidth(true);

		        Button addScoreReasonButton = new Button("Add Score + Reason");
		        addScoreReasonButton.setOnAction(e -> {
		            HBox scoreReasonRow = new HBox(10);
		            Spinner<Integer> scoreSpinner = new Spinner<>(0, 100, 50);
		            scoreSpinner.setEditable(true);
		            TextField reasonField = new TextField();
		            reasonField.setPromptText("Reason");
		            Button removeButton = new Button("Remove");
		            removeButton.setOnAction(ev -> scoreEntryContainer.getChildren().remove(scoreReasonRow));
		            scoreReasonRow.getChildren().addAll(scoreSpinner, reasonField, removeButton);
		            scoreEntryContainer.getChildren().add(scoreReasonRow);
		        });

		        Label resultLabel = new Label();
		        resultLabel.setStyle("-fx-text-fill: green;");

		        Button submitButton = new Button("Submit");
		        submitButton.setOnAction(e -> {
		            String reviewerUsername = reviewerUsernameField.getText().trim();
		            if (reviewerUsername.isEmpty() || scoreEntryContainer.getChildren().isEmpty()) {
		                resultLabel.setText("All fields are required.");
		                resultLabel.setStyle("-fx-text-fill: red;");
		                return;
		            }

		            try {
		                User reviewer = databaseHelper.getUserByUserName(reviewerUsername);
		                if (reviewer == null || !reviewer.getRoles().contains("Reviewer")) {
		                    resultLabel.setText("Reviewer not found or not a valid reviewer.");
		                    resultLabel.setStyle("-fx-text-fill: red;");
		                    return;
		                }

		                int totalScore = 0;
		                int count = 0;
		                StringBuilder feedbackBuilder = new StringBuilder();

		                for (javafx.scene.Node node : scoreEntryContainer.getChildren()) {
		                    if (node instanceof HBox) {
		                        HBox row = (HBox) node;
		                        Spinner<Integer> scoreSpinner = (Spinner<Integer>) row.getChildren().get(0);
		                        TextField reasonField = (TextField) row.getChildren().get(1);
		                        int score = scoreSpinner.getValue();
		                        String reason = reasonField.getText().trim();
		                        if (!reason.isEmpty()) {
		                            feedbackBuilder.append("Reason: ").append(reason).append(" | Score: ").append(score).append("\n");
		                            totalScore += score;
		                            count++;
		                        }
		                    }
		                }

		                if (count == 0) {
		                    resultLabel.setText("At least one score with reason is required.");
		                    resultLabel.setStyle("-fx-text-fill: red;");
		                    return;
		                }

		                int averageScore = totalScore / count;
		                String feedback = feedbackBuilder.toString();
		                databaseHelper.createScorecard(instructor.getId(), reviewer.getId(), feedback, averageScore);

		                resultLabel.setText("Scorecard successfully saved. Avg Score: " + averageScore);
		                resultLabel.setStyle("-fx-text-fill: green;");

		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                resultLabel.setText("Error saving scorecard.");
		                resultLabel.setStyle("-fx-text-fill: red;");
		            }
		        });

		        Button backButton = new Button("Back");
		        backButton.setOnAction(e -> show(primaryStage, instructor));

		        layout.getChildren().addAll(titleLabel, reviewerUsernameField, addScoreReasonButton, scrollPane, submitButton, resultLabel, backButton);
		        primaryStage.setScene(new Scene(layout, 800, 600));
		    }

		}

		        
