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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.After;
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
		clickOn("#loginButton");
	}
	
	@After
	public void logout()
	{
		clickOn("#logout");
		scene=stage.getScene();
		clickOn("#no");
		scene=stage.getScene();
	}
	
	public void typeDown(int x)
	{
		if(x>0)
		{
			for(int i=0; i<x;i++)
			{
				type(KeyCode.DOWN);
			}
		}
		else
		{
			for(int i=0; i>x;i--)
			{
				type(KeyCode.UP);
			}
		}
	}
	
	public void selectYear(int index)
	{
		clickOn("#yearDropdown");
		typeDown(index);
		type(KeyCode.ENTER);
		clickOn("#yearSelectButton");
	}
	
	@Test
	public void testSelectYear()
	{
		scene=stage.getScene();
		
		TextField field=(TextField)scene.lookup("#newYearTxtField");
		assertTrue((field.isDisable()));
		
		//check first year is correct
		selectYear(1);
		clickOn("#tree");
		typeDown(10);
		field=(TextField)scene.lookup("#contentField");
		assertTrue(field.getText().equals("Git abilities"));
		
		//and the second year
		selectYear(1);
		clickOn("#tree");
		typeDown(10);
		field=(TextField)scene.lookup("#contentField");
		assertTrue(field.getText().equals("Passing sprint 2"));
		
		//and first again
		selectYear(-1);
		clickOn("#tree");
		typeDown(10);
		field=(TextField)scene.lookup("#contentField");
		assertTrue(field.getText().equals("Git abilities"));
		
	}
}
