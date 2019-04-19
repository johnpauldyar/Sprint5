package software_masters.planner_networking;


import static org.junit.Assert.assertTrue;
//import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class GUITest extends ApplicationTest{

	private Registry registry;
	private Client client;
	private LoginController controller;
	private MainController mainController;
	private Stage stage;
	private Scene scene;

	
	
	@Before
	public void setUpClass() throws Exception
	{
		ServerImplementation.main(null);
		ApplicationTest.launch(Driver.class);
	}
	/*
	 * Connects to the server and houses the primary stage of the application
	 * 
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		stage=primaryStage;
		scene=stage.getScene();
		primaryStage.show();
	}
	
	@Test
	public void startTest()
	{
		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");
		TextField t=(TextField)scene.lookup("#hostText");
		assertTrue(t.getText().equals("localhost"));
	}
	
	@Test
	public void loginTest()
	{
		clickOn("#loginButton");
		//TextField t=(TextField)scene.lookup("#hostText");
		//assertTrue(t.getText().equals("localhost"));
	}
	
	

}
