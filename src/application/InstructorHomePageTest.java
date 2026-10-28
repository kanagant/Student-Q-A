package application; 

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import databasePart1.DatabaseHelper;
import application.AdminTask;
import application.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for DatabaseHelper and InstructorHomePage functionalities.
 */
public class InstructorHomePageTest {

    private DatabaseHelper databaseHelper;
    private User instructorUser;

    @BeforeEach
    void setUp() throws SQLException {
        databaseHelper = new DatabaseHelper();
        databaseHelper.connectToDatabase();

        // Create the roles list
        List<String> roles = new ArrayList<>(Arrays.asList("Instructor"));
        
        // Correct constructor: User(int id, String username, String password, String email, List<String> roles)
        instructorUser = new User(0, "testInstructor", "password123", "testInstructor@example.com", roles);

        // Register user only if not already registered
        if (!databaseHelper.doesUserExist(instructorUser.getUserName())) {
            databaseHelper.register(instructorUser, roles);
        }
        instructorUser = databaseHelper.getUserByUserName("testInstructor"); // Refresh with ID
    }

    @Test
    void testEstablishingParameters() {
        assertNotNull(databaseHelper.getConnection(), "Database connection should be established.");
        assertNotNull(instructorUser, "Instructor user should exist.");
        assertEquals("testInstructor", instructorUser.getUserName(), "Instructor username should match.");
        assertTrue(instructorUser.getRoles().contains("Instructor"), "User should have 'Instructor' role.");
    }

    @Test
    void testReopeningClosedRequest() throws SQLException {
        // Arrange
        AdminTask task = new AdminTask(0, "Test closing and reopening task", instructorUser);
        databaseHelper.addAdminTask(task);

        // Fetch inserted task
        AdminTask insertedTask = databaseHelper.getAdminTasksSortedByDate().get(0);

        // Mark closed
        databaseHelper.markAdminTaskResolved(insertedTask.getId(), true);
        AdminTask closedTask = databaseHelper.getAdminTaskById(insertedTask.getId());
        assertTrue(closedTask.isResolved(), "Task should initially be closed.");

        // Act: Reopen
        databaseHelper.markAdminTaskResolved(insertedTask.getId(), false);

        // Assert
        AdminTask reopenedTask = databaseHelper.getAdminTaskById(insertedTask.getId());
        assertFalse(reopenedTask.isResolved(), "Task should be reopened (resolved = false).");
    }
}

