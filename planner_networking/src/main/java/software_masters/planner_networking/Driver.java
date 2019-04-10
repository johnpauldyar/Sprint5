package software_masters.planner_networking;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class Driver extends Application
{
	Registry registry;
	Client client; 
	Controller controller;
	public static void main(String [] args) throws Exception
	{
		launch(args);
	}
	
	
	@Override public void start(Stage primaryStage) throws Exception
	{
		try
		{
			registry = LocateRegistry.createRegistry(1075);
			ServerImplementation server = new ServerImplementation();
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
				ServerImplementation server = new ServerImplementation();
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
		primaryStage.setScene(scene);
		primaryStage.setTitle("The Click me app");
		primaryStage.show();

	}
	
	

}
