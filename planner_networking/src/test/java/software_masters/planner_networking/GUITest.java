package software_masters.planner_networking;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
		primaryStage.show();
	}
	
	@Test
	public void startTest()
	{
		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");
		clickOn("#loginButton");
		assertTrue("#mainBorderPane")
	}
	
	

}
