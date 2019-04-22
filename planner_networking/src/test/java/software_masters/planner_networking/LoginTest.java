package software_masters.planner_networking;


import static org.junit.Assert.*;
import org.junit.After;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class LoginTest extends ApplicationTest{

	private Scene scene;
	
	private TextField t;
	private Label l;
	
	@BeforeClass
	public static void setUpClass() throws Exception
	{
		ServerImplementation.main(null);
		ApplicationTest.launch(Driver.class);
	}
	
	/**
	 * Connects to the server and houses the primary stage of the application
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.show();
		
		scene=primaryStage.getScene();
	}

	/**
	 * This method resets the text fields to their initial states
	 */
	@After
	public void reset()
	{
		clickOn("#hostText");
		clickOn("#hostText");
		type(KeyCode.BACK_SPACE);
		write("localhost");
		
		clickOn("#userText");
		clickOn("#userText");
		type(KeyCode.BACK_SPACE);
		
		clickOn("#passText");
		clickOn("#passText");
		type(KeyCode.BACK_SPACE);
	}
	
	@Test
	public void testStart()
	{		
		t=(TextField)scene.lookup("#hostText");
		assertTrue(t.getText().equals("localhost"));
		
		t = (TextField)scene.lookup("#userText");
		assertTrue(t.getText().isEmpty());
		
		t = (TextField)scene.lookup("#passText");
		assertTrue(t.getText().isEmpty());
	}
	
	@Test
	public void testFailHost()
	{
		clickOn("#hostText");
	 	clickOn("#hostText");
	 	type(KeyCode.BACK_SPACE);
	 	
	 	l = (Label)scene.lookup("#hostError");
	 	
	 	assertFalse(l.isVisible());
	 	
	 	clickOn("#loginButton");
	 	
	 	assertTrue(l.isVisible());
	}
}
