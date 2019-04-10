package software_masters.planner_networking;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;

public class Driver extends Application
{
	Registry registry;
	Client client; 
	Controller controller;
	Stage stage;
	public static void main(String [] args) throws Exception
	{
		launch(args);
	}
	
	
	@Override public void start(Stage primaryStage) throws Exception
	{
		try
		{
	
			
			registry= LocateRegistry.createRegistry(1075);
			ServerImplementation server = ServerImplementation.load();
			Server actualServer = server;
			Server stub = (Server) UnicastRemoteObject.exportObject(server, 0);
			registry.rebind("PlannerServer", stub);
			Server testServer = (Server) registry.lookup("PlannerServer");
			client = new Client(testServer);
		} 
		catch (Exception e)
		{
			try
			{
				registry= LocateRegistry.getRegistry(1075);
				ServerImplementation server = ServerImplementation.load();
				Server actualServer = server;
				Server stub = (Server) UnicastRemoteObject.exportObject(server, 0);
				registry.rebind("PlannerServer", stub);
				Server testServer = (Server) registry.lookup("PlannerServer");
				client = new Client(testServer);
				
			}
			catch(Exception i)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
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
	
	public void closing() throws RemoteException
	{
		boolean confirm = false;
		confirm = ConfirmationBox.show("Are you sure you want to quit?","Confirmation","Yes","No");
		if (confirm)
		{
			System.out.print("Closing");
			client.getServer().save();
			stage.close();
		}
	}
	
	

}
