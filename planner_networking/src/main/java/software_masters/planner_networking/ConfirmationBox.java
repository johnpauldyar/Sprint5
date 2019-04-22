package software_masters.planner_networking;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author john.dyar
 *
 */
public class ConfirmationBox
{
	static Stage stage;
	static boolean btnYesClicked;

	/**
	 * Creates a confirmation box to be implemented in the application
	 * 
	 * @param message
	 * @param title
	 * @param textYes
	 * @param textNo
	 * @return
	 */
	public static boolean show(String message, String title, String textYes, String textNo)
	{
		btnYesClicked = false;

		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setMinWidth(250);

		Label lbl = new Label(message);

		Button btnYes = new Button();
		btnYes.setText(textYes);
		btnYes.setOnAction(e -> btnYes_Clicked());
		btnYes.setId("yes");

		Button btnNo = new Button();
		btnNo.setText(textNo);
		btnNo.setOnAction(e -> btnNo_Clicked());
		btnNo.setId("no");

		HBox paneBtn = new HBox(20);
		paneBtn.getChildren().addAll(btnYes, btnNo);

		VBox pane = new VBox(20);
		pane.getChildren().addAll(lbl, paneBtn);
		pane.setAlignment(Pos.CENTER);

		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.showAndWait();
		return btnYesClicked;

	}

	private static void btnYes_Clicked()
	{
		stage.close();
		btnYesClicked = true;
	}

	private static void btnNo_Clicked()
	{
		stage.close();
		btnYesClicked = false;
	}
}
