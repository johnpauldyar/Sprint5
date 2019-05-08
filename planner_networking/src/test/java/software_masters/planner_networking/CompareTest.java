package software_masters.planner_networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CompareTest extends ApplicationTest 
{
	
	private Stage stage;
	private Scene scene;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		//ServerImplementation.main(null);
		ApplicationTest.launch(Driver.class);
	}

	@Before
	public void setUp() throws Exception 
	{
		scene = stage.getScene();
		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");
		clickOn("#loginButton");
		clickOn("#compare");
	}

	@After
	public void tearDown() throws Exception 
	{
		clickOn("#logout");
		scene = stage.getScene();
	}
	
	@Override
	public void start(Stage stage) throws Exception 
	{
		this.stage = stage;
		stage.show();
	}
	
	@Test
	public void testInitialScene()
	{
		scene = stage.getScene();
		Label diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
		Label lbl1 = (Label) scene.lookup("#label1");
		assertTrue(lbl1.getText().equals(""));
		
		Label lbl2 = (Label) scene.lookup("#label2");
		assertTrue(lbl2.getText().equals(""));
		
		ChoiceBox<PlanFile> drop1 = (ChoiceBox<PlanFile>) scene.lookup("#drop1");
		assertTrue(drop1.getValue() == null);
		
		ChoiceBox<PlanFile> drop2 = (ChoiceBox<PlanFile>) scene.lookup("#drop2");
		assertTrue(drop2.getValue() == null);
	}

	@Test
	public void testViews() 
	{
		scene=stage.getScene();
		selectYear1(1);
		ChoiceBox<PlanFile> drop1 = (ChoiceBox<PlanFile>) scene.lookup("#drop1");
		assertTrue(drop1.getValue().getYear().equals("2009"));
		
		clickOn("#tree1");
		typeDown(-10);
		Label lbl1 = (Label) scene.lookup("#label1");
		assertTrue(lbl1.getText().equals("Our mission is to pass this class excellently"));
		
		selectYear2(1);
		ChoiceBox<PlanFile> drop2 = (ChoiceBox<PlanFile>) scene.lookup("#drop2");
		assertTrue(drop2.getValue().getYear().equals("2009"));
		
		clickOn("#tree2");
		typeDown(-10);
		Label lbl2 = (Label) scene.lookup("#label2");
		assertTrue(lbl2.getText().equals("Our mission is to pass this class excellently"));
		
	}
	
	@Test
	public void testViewButton()
	{
		scene=stage.getScene();
		BorderPane compare = (BorderPane) scene.lookup("#compareBorderPane");
		assertTrue(compare.equals(stage.getScene().getRoot()));
		clickOn("#view");
		
		scene=stage.getScene();
		assertFalse(compare.equals(stage.getScene().getRoot()));
		clickOn("#compare");
	}
	
	@Test
	public void testCompareIfSameYear()
	{
		scene = stage.getScene();
		
		Label diffLabel;
		
		selectYear1(1);
		selectYear2(1);
		diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
		selectYear1(1);
		selectYear2(1);
		diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
		selectYear1(1);
		selectYear2(1);
		diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
		selectYear1(1);
		selectYear2(1);
		diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
		selectYear1(1);
		selectYear2(1);
		diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
		selectYear1(1);
		selectYear2(1);
		diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
		selectYear1(1);
		selectYear2(1);
		diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
		selectYear1(1);
		selectYear2(1);
		diffLabel = (Label) scene.lookup("#diffLabel");
		assertTrue(diffLabel.getText().equals(" "));
		
	}
	
	@Test
	public void testCompare()
	{
		Label diffLabel;
		scene = stage.getScene();
		
		
		selectYear1(1);
		selectYear2(2);
		diffLabel = (Label) scene.lookup("#diffLabel");
		String str = " \nResults 1 differs\nResults 2 differs\nLearning Objective 2 differs\n" 
				+ "Assessment Process 3 differs\nResults 3 differs\n"
				+ "Assessment Process 4 differs\nResults 4 differs";
		//System.out.println(diffLabel.getText());
		//System.out.println(str);
		assertTrue(str.equals(diffLabel.getText()));
		
		
	}


	
	public void selectYear1(int index)
	{
		clickOn("#drop1");
		typeDown(index);
		type(KeyCode.ENTER);
		clickOn("#select1");
	}
	
	public void selectYear2(int index)
	{
		clickOn("#drop2");
		typeDown(index);
		type(KeyCode.ENTER);
		clickOn("#select2");
	}
	
	
	public void typeDown(int x)
	{
		if (x > 0)
		{
			for (int i = 0; i < x; i++)
			{
				type(KeyCode.DOWN);
			}
		} else
		{
			for (int i = 0; i > x; i--)
			{
				type(KeyCode.UP);
			}
		}
	}

}
