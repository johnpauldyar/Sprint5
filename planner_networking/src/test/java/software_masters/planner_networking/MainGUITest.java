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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class MainGUITest extends ApplicationTest {
	
	private Registry registry;
	private Client client;
	private LoginController controller;
	private MainController mainController;
	private Stage stage;
	private Scene scene;
	
	@BeforeClass
	public static void setUpClass() throws Exception
	{
		ServerImplementation.main(null);
		ApplicationTest.launch(Driver.class);
	}
	
	
	public void start(Stage stage) throws Exception
	{
		this.stage=stage;
		stage.show();
	}
	
	@Before
	public void login()
	{
		scene=stage.getScene();
		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Override
	

}
