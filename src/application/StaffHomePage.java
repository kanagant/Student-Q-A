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
/**
 * StaffHomePage represents the home screen UI for users with the "Staff" role.
 * Provides functionality to review student questions/answers, switch roles,
 * view messages with instructors/staff, and promote users.
 */

public class StaffHomePage {
	/**
     * Displays the staff page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	
	private DatabaseHelper databaseHelper;
	
	/**
     * Constructor to initialize the StaffHomePage with the database helper.
     *
     * @param databaseHelper an instance of DatabaseHelper for database operations
     */
	public StaffHomePage(DatabaseHelper databaseHelper) {
		System.out.println("hello"); 
        this.databaseHelper = databaseHelper;
        //this.user = user;
    }

    /**
     * Displays the Staff home screen UI.
     *
     * @param primaryStage the main application window
     * @param user         the currently logged-in user
     */
    public void show(Stage primaryStage, User user) {
    	
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the staff
	    Label staffLabel = new Label("Hello, Staff!");
	    
	    staffLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

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
        
        Button questionsAnswersFeedback = new Button("Click here to review a list of all questions, answers, and private feedback");
        questionsAnswersFeedback.setOnAction(e->
        {
        	showAllQuestionsAnswersPrivateFeedback(primaryStage, user);
        });
        
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
     // Switch Role Button
        String username= user.getUserName();
        ArrayList<String> roles= databaseHelper.getUserRoles(username);
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll(roles);
        if (!roles.isEmpty()) {
            roleDropdown.setValue(roles.get(0)); // Select the first role by default
        }
        
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

        
	    //layout.getChildren().addAll(staffLabel, logoutButton, roleDropdown, switchRole);
        layout.getChildren().addAll(staffLabel,userDropdown,selectUser, questionsAnswersFeedback, userDropdownForMessages, selectStaffOrInstructor, openAdminTasks,closedAdminTasks, logoutButton, roleDropdown, switchRole);
        Scene staffScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(staffScene);
	    primaryStage.setTitle("RPage");
    }
    /**
     * Displays the questions and answers posted by the selected student.
     *
     * @param primaryStage the main application window
     * @param u   the user whose Q&A data will be shown
     * @param staff  the logged-in staff member
     */
	    private void showQuestionsAndAnswerPageByUser(Stage primaryStage, User u, User staff) 
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
	        backButton.setOnAction(e -> show(primaryStage, staff));
	
	        layout.getChildren().addAll(titleLabel, scrollPane, titleLabelAnswers, scrollPaneAnswers, makeReviewerButton, resultLabel, backButton);
	        primaryStage.setScene(new Scene(layout, 800, 400));
	    }
	    
	    
	    /**
	     * Displays all questions, answers, and private feedback for staff review.
	     *
	     * @param primaryStage the main application window
	     * @param staff         the currently logged-in staff user
	     */
	    private void showAllQuestionsAnswersPrivateFeedback(Stage primaryStage, User staff) 
	    {
	    	VBox layout = new VBox(5);
	        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	
	        Label titleLabel = new Label("Users Questions List");
	        ScrollPane scrollPane = new ScrollPane();
	        VBox questionContainer = new VBox(1);
	        int k = 0;
	        try {
	        	//System.out.println("About to retrieve questions");
	        	ArrayList<String> userNames = databaseHelper.getAllUsernames();
	        	int j = 1;
	        	for(String username : userNames)
	        	{
	        		User u = databaseHelper.getUserByUserName(username);
	        		if(!u.getRoles().contains("Student")) {continue;}
		            List<Question> questions = databaseHelper.getQuestionsByUser(u.getId());
		           
		            //System.out.println("Retrieved the questions");
		            
		            VBox questionBox = new VBox(10);
		            //int size = questions.size();
		            //System.out.println("questions list is size " + size)
		            for (Question q : questions) 
		            {
		            	String uName = q.getUser().getUserName();
		            	
		                String questionAnswers = q.isResolved() ? "✅ " + q.getContent() + "[" + q.getTags() + "] " + "(" + uName + ")" : q.getContent() + "[" + q.getTags() + "] " + "(" + uName + ")"; 
		                //System.out.println(questionText);
		              
		                List<Answer> answers = databaseHelper.getAnswersByQuestionId(q.getId());
		                int i = 1;
		                for(Answer a : answers)
		                {
		                	String aUname = a.getUser().getUserName();
		                	String ans = "\n	Answer " + i + ": " + a.getContent() + " (" + aUname + ")";
		                	i++;
		                	questionAnswers = questionAnswers + ans;
		                	List<Review> reviews = databaseHelper.getReviewsByAnswerId(a.getId());
		                	int m = 1;
		                	for(Review r : reviews)
		                	{
		                		String RUname = r.getReviewer().getUserName();
		                		String review = "\n		   Review " + m + ": " + r.getContent() + " (" + RUname + ")";	
		                		questionAnswers = questionAnswers + review;
		                		
		                		List<Feedback> feedback = databaseHelper.getFeedbackForReview(r.getId());
		                		int size = feedback.size();
		                		System.out.println("feedback size = " + size); 
		                		int s = 1;
		                		for(Feedback f : feedback)
		                		{
		                			String feedUname = f.getStudent().getUserName();
		                			String feed = "\n			Feedback for review " + s + ": " + f.getContent() + " (" + feedUname + ")";
		                			s++;
		                			questionAnswers = questionAnswers + feed;
		                		}
		                	}
		                }
		                Label questionWithAnswers = new Label("Question " + j + ": " + questionAnswers);
		                questionBox.getChildren().addAll(questionWithAnswers);
		                questionBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #f5f5f5;");
		                j++;
		            }
		            questionContainer.getChildren().add(questionBox);
		            
	        	}
		     } catch (Exception ex) {
		           System.out.println("ERROR processing question at index " + k + ": " + ex.getMessage());
		           ex.printStackTrace();
		    }

	        scrollPane.setContent(questionContainer);
	        scrollPane.setFitToWidth(true);  

	        Label resultLabel = new Label();
	        
	        
	        Button backButton = new Button("Back");
	        backButton.setOnAction(e -> show(primaryStage, staff));
	
	        layout.getChildren().addAll(titleLabel, scrollPane, resultLabel, backButton);
	        primaryStage.setScene(new Scene(layout, 800, 400));
	    }
	    
	    
	    /**
	     * Displays the private messages between the current user and the selected staff/instructor.
	     *
	     * @param primaryStage the main application window
	     * @param u   the user to view messages with
	     * @param staff  the logged-in user
	     */
	    private void viewPrivateMessages(Stage primaryStage, User u, User staff)
	    {
	    	VBox layout = new VBox(20);
	        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	
	        Label titleLabel = new Label("Message between you and " + u.getUserName());
	        
	        ScrollPane scrollPane = new ScrollPane();
	        VBox messageContainer = new VBox(10);
	        try {
	        	//System.out.println("About to retrieve questions");
	            List<PrivateMessage> messages = databaseHelper.getMessages(staff.getId(), u.getId());
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
	            databaseHelper.sendPrivateMessage(staff, u, content);
	            viewPrivateMessages(primaryStage, u, staff);
	        });

	        backButton.setOnAction(e -> show(primaryStage,staff));
	
	        layout.getChildren().addAll(titleLabel, scrollPane, newMessageField, resultLabel, submitButton, errorLabel, backButton);
	        primaryStage.setScene(new Scene(layout, 800, 400));
	    
	        }
	     

		private void showOpenAdminTasks(Stage primaryStage, User staff) 
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
				VBox actionLogBox = new VBox(5);
		                Label actionLogTitle = new Label("Logged Actions:");
		                actionLogTitle.setStyle("-fx-font-weight: bold;");
		                actionLogBox.getChildren().add(actionLogTitle);

		                try {
		                    List<String> actions = databaseHelper.getAdminTaskActions(task.getId());
		                    if (actions.isEmpty()) {
		                        actionLogBox.getChildren().add(new Label("No actions logged yet."));
		                    } else {
		                        for (String action : actions) {
		                            Label actionLabel = new Label(action);
		                            actionLabel.setStyle("-fx-font-size: 11;");
		                            actionLogBox.getChildren().add(actionLabel);
		                        }
		                    }
		                } catch (SQLException ex) {
		                    ex.printStackTrace();
		                    actionLogBox.getChildren().add(new Label("⚠️ Failed to load actions."));
		                }
				    
		                taskBox.getChildren().addAll(taskLabel, actionLogBox);
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
		    backButton.setOnAction(e -> show(primaryStage, staff));
		
		    layout.getChildren().addAll(titleLabel, scrollPane, resultLabel, backButton);
		    primaryStage.setScene(new Scene(layout, 800, 400));
		}
		
		private void showClosedAdminTasks(Stage primaryStage, User admin) {
		    VBox layout = new VBox(10);
		    layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

		    Label titleLabel = new Label("Requested Admin Tasks");
		    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		    ScrollPane scrollPane = new ScrollPane();
		    scrollPane.setFitToWidth(true);

		    VBox taskContainer = new VBox(10);
		    taskContainer.setStyle("-fx-padding: 10;");

		    try {
		        ArrayList<String> userNames = databaseHelper.getAllUsernames();
		        int j = 1;

		        for (String username : userNames) {
		            User u = databaseHelper.getUserByUserName(username);
		            if (!u.getRoles().contains("Instructor")) continue;

		            List<AdminTask> tasks = databaseHelper.getAdminTaskByUser(u.getId());
		            for (AdminTask task : tasks) {
		                if (!task.isResolved()) continue;

		                VBox taskBox = new VBox(5);
		                taskBox.setStyle("-fx-border-color: #ccc; -fx-background-color: #f9f9f9; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");

		                String uName = task.getUser().getUserName();
		                String taskString = "✅ Task " + j + ": " + task.getContent() + " (" + uName + ")";
		                Label taskLabel = new Label(taskString);
		                taskLabel.setStyle("-fx-font-size: 14;");

		                HBox buttonRow = new HBox(10);
		                buttonRow.setStyle("-fx-alignment: center-left;");

		                Button reopenButton = new Button("Reopen");
		                reopenButton.setOnAction(ev -> showReopenTaskDetails(primaryStage, task, admin));

		                buttonRow.getChildren().addAll(reopenButton);
		                taskBox.getChildren().addAll(taskLabel, buttonRow);

		                taskContainer.getChildren().add(taskBox);
		                j++;
		            }
		        }

		    } catch (Exception ex) {
		        System.out.println("ERROR processing tasks: " + ex.getMessage());
		        ex.printStackTrace();
		    }

		    scrollPane.setContent(taskContainer);

		    Button backButton = new Button("Back");
		    backButton.setOnAction(e -> show(primaryStage, admin));

		    layout.getChildren().addAll(titleLabel, scrollPane, backButton);
		    primaryStage.setScene(new Scene(layout, 800, 500));
		}
		private void showReopenTaskDetails(Stage primaryStage, AdminTask task, User admin) {
		    VBox layout = new VBox(10);
		    layout.setStyle("-fx-padding: 20;");

		    Label titleLabel = new Label("Reopen Admin Task");
		    Label originalLabel = new Label("Original Request:");
		    TextArea contentArea = new TextArea(task.getContent());
		    contentArea.setWrapText(true);
		    contentArea.setPrefRowCount(4);

		    VBox logBox = new VBox(5);
		    Label logHeader = new Label("Admin Action Log:");
		    logHeader.setStyle("-fx-font-weight: bold;");

		    try {
		        List<String> actions = databaseHelper.getActionsForTask(task.getId());
		        for (String action : actions) {
		            Label actionLabel = new Label("• " + action);
		            actionLabel.setStyle("-fx-font-size: 11;");
		            logBox.getChildren().add(actionLabel);
		        }
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    }

		    Button confirmButton = new Button("Confirm Reopen");
		    confirmButton.setOnAction(e -> {
		        try {
		            task.setResolved(false);
		            task.setContent(contentArea.getText().trim());
		            databaseHelper.updateAdminTaskContent(task.getId(), task.getContent());  // You’ll need this method
		            databaseHelper.markAdminTaskResolved(task.getId(), false);

		            // Optional: log action
		            databaseHelper.logAdminTaskAction(task.getId(), "Reopened by admin");

		            showClosedAdminTasks(primaryStage, admin);
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }
		    });

		    Button cancelButton = new Button("Cancel");
		    cancelButton.setOnAction(e -> showClosedAdminTasks(primaryStage, admin));

		    HBox buttonRow = new HBox(10, confirmButton, cancelButton);

		    layout.getChildren().addAll(titleLabel, originalLabel, contentArea, logHeader, logBox, buttonRow);
		    primaryStage.setScene(new Scene(layout, 700, 450));
		}

	}
