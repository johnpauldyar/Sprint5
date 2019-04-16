package software_masters.planner_networking;

import java.rmi.RemoteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

	private Client client;
	
    @FXML
    private TextField hostText;

    @FXML
    private Label hostError;

    @FXML
    private TextField userText;

    @FXML
    private Label userError;

    @FXML
    private PasswordField passText;

    @FXML
    private Label passError;

    @FXML
    private Button loginButton;

    /**
     * This function handles when the login button is pressed
     * @param event
     * @throws IllegalArgumentException
     * @throws RemoteException
     */
    @FXML
    public void loginPress(ActionEvent event) throws IllegalArgumentException, RemoteException{
    	
    	String user = userText.getText();
    	String pass = passText.getText();
    	String host = hostText.getText();
    	
    	if (user.equals(null))
    	{
    		userError.setVisible(true);
    	}
    	else if (pass.equals(null))
    	{
    		passError.setVisible(true);
    	}
    	else if (host.equals(null))
    	{
    		hostError.setVisible(true);
    	}
    	else
    	{
    		// TODO set server somehow //
    		client.login(user, pass);
    		// TODO reset scene and switch to ClientView
    	}
    }

	/**
	 * @return the client
	 */
	public Client getClient()
	{
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Client client)
	{
		this.client = client;
	}

}
