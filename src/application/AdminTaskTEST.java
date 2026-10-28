package application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import application.*;
import databasePart1.DatabaseHelper;



/**
 * Unit tests for core features of the application, including:
 * - User registration
 * - Private messaging
 * - Posting questions, answers, and reviews
 * - Providing private feedback on reviews
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminTaskTEST {
	private DatabaseHelper databaseHelper;
	/**
     * Initializes the in-memory database and registers an admin user before any tests run.
     */
	@BeforeAll
	public void setupDatabase() throws SQLException {
        databaseHelper = new DatabaseHelper();
        databaseHelper.connectToDatabase(); // Creates tables
        databaseHelper.increment_num_users();
        User jlausche = new User(databaseHelper.get_num_users(),"jlausche", "Password!123", "jlausche@asu.edu", Arrays.asList("Admin"));
        databaseHelper.register(jlausche, jlausche.getRoles());
    }

	
	
	
	
	/**
     * Tests the entire Q&A workflow:
     * - Posting questions by a user
     * - Posting answers to those questions by another user
     * - Reviewing the answers
     * - Adding private feedback for each review
     */
	@Test
	void adminTasksTest() throws SQLException
	{
		databaseHelper.increment_num_users();
        User user1 = new User(databaseHelper.get_num_users(),"jeremy", "Password!123", "jeremy@example.com", Arrays.asList("Instructor"));
        databaseHelper.register(user1, user1.getRoles());
        
        databaseHelper.increment_num_users();
        User user2 = new User(databaseHelper.get_num_users(),"paul", "Password!123", "paul@example.com", Arrays.asList("Instructor"));
        databaseHelper.register(user2, user2.getRoles());
        
        databaseHelper.increment_num_users();
        User user3 = new User(databaseHelper.get_num_users(),"sarah", "Password!123", "sarah@example.com", Arrays.asList("Admin"));
        databaseHelper.register(user3, user3.getRoles());
		
        
         // The next several lines will be testing posting questions
         
        AdminTask task1 = new AdminTask(1, "Can you please add admin role to me?", user1);
		databaseHelper.addAdminTask(task1);
		
		AdminTask task2 = new AdminTask(2, "Will you please reset my password for me?", user2);
		databaseHelper.addAdminTask(task2);
		
		List<AdminTask> tasks1 = databaseHelper.getAdminTaskByUser(user1.getId());
		assertEquals(tasks1.size(),1);
		
		List<AdminTask> tasks2 = databaseHelper.getAdminTaskByUser(user2.getId());
		assertEquals(tasks2.size(),1);
		
		AdminTask testTASK1 = databaseHelper.getAdminTaskById(1);
		AdminTask testTASK2 = databaseHelper.getAdminTaskById(2);
		
		assertEquals(testTASK1.getContent(),task1.getContent());
		assertEquals(testTASK2.getContent(), task2.getContent());
		
		
	
	}
	
	
	

}
