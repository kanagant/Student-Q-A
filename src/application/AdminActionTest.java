package application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import databasePart1.DatabaseHelper;

class AdminActionTest {

	private DatabaseHelper databaseHelper;

	@BeforeEach
	public void setup() throws Exception {
	    
	    databaseHelper = new DatabaseHelper();
	    databaseHelper.getConnection();
	}
	
	@Test
	public void testLogAdminActions() throws SQLException {
	    
	    int taskId = 1; 
	    String action = "Reviewed user submission";

	    databaseHelper.logAdminTaskAction(taskId, action);

	    
	    List<String> actions = databaseHelper.getAdminTaskActions(taskId);
	    
	    boolean found = actions.stream().anyMatch(a -> a.contains(action));
	    
	    assertTrue(found, "Admin action should be logged and retrievable");
	}
	
	@Test
	public void testRetrieveClosedRequest() throws SQLException {
	   
		
		List<String> usernames = databaseHelper.getAllUsernames();

		boolean foundResolved = false;

		for (String username : usernames) {
		    User user = databaseHelper.getUserByUserName(username);
		    List<AdminTask> tasks = databaseHelper.getAdminTaskByUser(user.getId());
		    List<AdminTask> resolved = tasks.stream().filter(AdminTask::isResolved).toList();

		    if (!resolved.isEmpty()) {
		        System.out.println("✅ Found resolved tasks for user: " + username);
		        foundResolved = true;
		        break;
		    }
		}

		assertTrue(foundResolved, "At least one user should have a resolved admin task.");
	}
}
