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
class Homework4TEST {
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
     * Tests sending and receiving private messages between two users.
     * Verifies that all messages sent are retrievable in the correct order.
     */

	@Test
	void sendAndRecievePrivateMessage() throws SQLException {
		databaseHelper.increment_num_users();
        User user1 = new User(databaseHelper.get_num_users(),"bob", "Password!123", "bob@example.com", Arrays.asList("Instructor"));
        databaseHelper.register(user1, user1.getRoles());
        databaseHelper.increment_num_users();
        User user2 = new User(databaseHelper.get_num_users(),"charlie", "Password!123", "charlie@example.com", Arrays.asList("Staff"));
        databaseHelper.register(user2, user2.getRoles());
        
        databaseHelper.sendPrivateMessage(user1, user2, "This is a test!");
        databaseHelper.sendPrivateMessage(user2, user1, "Well so is this!");
        databaseHelper.sendPrivateMessage(user1, user2, "Lets hope we pass!");
        databaseHelper.sendPrivateMessage(user2, user1, "That is the goal!");
        databaseHelper.sendPrivateMessage(user1, user2, "Goodluck!");
        databaseHelper.sendPrivateMessage(user2, user1, "You as well!");
        
        List<PrivateMessage> privateMessage = databaseHelper.getMessages(user1.getId(), user2.getId());
        List<String> pms = new ArrayList<>();
        for(PrivateMessage pm : privateMessage)
        {
        	pms.add(pm.getContent());
        	System.out.println(pm.getContent());
        }
        assertTrue(pms.size()==6);
	}
	
	
	/**
     * Tests the entire Q&A workflow:
     * - Posting questions by a user
     * - Posting answers to those questions by another user
     * - Reviewing the answers
     * - Adding private feedback for each review
     */
	@Test
	void postQuestionsAnswersReviewsPrivateFeedback() throws SQLException
	{
		databaseHelper.increment_num_users();
        User user1 = new User(databaseHelper.get_num_users(),"jeremy", "Password!123", "jeremy@example.com", Arrays.asList("Student", "Reviewer"));
        databaseHelper.register(user1, user1.getRoles());
        databaseHelper.increment_num_users();
        User user2 = new User(databaseHelper.get_num_users(),"paul", "Password!123", "paul@example.com", Arrays.asList("Student"));
        databaseHelper.register(user2, user2.getRoles());
		
        
         // The next several lines will be testing posting questions
         
        Question question1 = new Question(1, "When is the assignment due?", user1, "Assignments");
		databaseHelper.addQuestion(question1);
		
		Question question2 = new Question(2, "When should our mid term grades be available?", user1, "Exams");
		databaseHelper.addQuestion(question2);
		
		List<Question> questions = databaseHelper.getQuestionsByUser(user1.getId());
		assertEquals(questions.size(),2);
		
		Question testQ1 = databaseHelper.getQuestionById(1);
		Question testQ2 = databaseHelper.getQuestionById(2);
		
		assertEquals(testQ1.getContent(),question1.getContent());
		assertEquals(testQ2.getContent(), question2.getContent());
		
		
		 // The next several lines will be testing posting answers for questions
		 
		
		Answer answer1 = new Answer(1, "The assignment is due on the 10th of april", user2, question1);
		Answer answer2 = new Answer(2, "The grades should be realeased by the end of the week!", user2, question2);
		databaseHelper.addAnswer(answer1);
		databaseHelper.addAnswer(answer2);
		List<Answer> answers = databaseHelper.getAnswersByUser(user2.getId());
		
		assertEquals(2, answers.size());
		
		Answer testA1 = databaseHelper.getAnswerById(1);
		Answer testA2 = databaseHelper.getAnswerById(2);
		
		assertEquals(testA1.getContent(),answer1.getContent());
		assertEquals(testA2.getContent(),answer2.getContent());
		
		
		
		
		 // The next several lines will be testing posting reviews for answers
		 
		
		
		Review review1 = new Review(1, "Thanks this was super helpful!", user1, answer1, null);
		Review review2 = new Review(2, "I don't see where that's posted, could you please elaborate where you got this information?", user1, answer2, null);
		databaseHelper.addReview(review1);
		databaseHelper.addReview(review2);
		
		List<Review> reviews = databaseHelper.getReviewsByReviewerId(user1.getId());
		
		assertEquals(2,reviews.size());
		
		Review testR1 = databaseHelper.getReviewById(1);
		Review testR2 = databaseHelper.getReviewById(2);
		
		assertEquals(testR1.getContent(),review1.getContent());
		assertEquals(testR2.getContent(),review2.getContent());
		
		
		
		
		//  The next several lines will be testing posting private feedback for reviews
		 
		
	    databaseHelper.addFeedback(user2.getId(), user1.getId(), 1, 1, "Thank you!!! Best of luck!");
	    databaseHelper.addFeedback(user2.getId(), user1.getId(), 2, 2, "This was posted in the sylabus I suggest always checking there before asking questions here!");
	    
	    List<Feedback> feedback = databaseHelper.getFeedbackForReviewer(user1.getId());
	    assertEquals(2,feedback.size());
	    
	    Feedback testF1 = databaseHelper.getFeedbackForReview(1).get(0);
	    Feedback testF2 = databaseHelper.getFeedbackForReview(2).get(0);
	    
	    assertEquals("Thank you!!! Best of luck!",testF1.getContent());
	    assertEquals("This was posted in the sylabus I suggest always checking there before asking questions here!",testF2.getContent());
	    
	
	}
	
	
	

}
