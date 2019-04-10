package software_masters.planner_networking;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;

/**
 * @author john.dyar
 *
 */
public class Driver extends Application
{
	
	
	Registry registry;
	Client client; 
	Controller controller;
	Stage stage;
	
	
	/**
	 * launches the javafx application
	 * @param args
	 * @throws Exception
	 */
	public static void main(String [] args) throws Exception
	{
		launch(args);
	}
	
	
	/* 
	 * Connects to the server and houses the primary stage of the application
	 * 
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override public void start(Stage primaryStage) throws Exception
	{
		try
		{
			registry = LocateRegistry.getRegistry(1060);
			Server stub = (Server) registry.lookup("PlannerServer");
			client = new Client(stub);
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		controller = new Controller(client);

		ClientView b = new ClientView(controller, client);
		Scene scene = b.scene;
		stage=primaryStage;
		primaryStage.setScene(scene);
		primaryStage.setTitle("Centre Business Plans");
		primaryStage.show();
		primaryStage.setOnCloseRequest(
				e ->{
					e.consume();
					try {
						closing();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					});

	}
	
	/**
	 * Creates a confirmation box and closes the client's application
	 * 
	 * @throws RemoteException
	 */
	public void closing() throws RemoteException
	{
		boolean confirm = false;
		confirm = ConfirmationBox.show("Are you sure you want to quit?","Confirmation","Yes","No");
		if (confirm)
		{
			//client.getServer().save();
			stage.close();
		}
	}
	
	

}
