package software_masters.planner_networking;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import javafx.scene.control.TreeItem;

public class Controller {
	
	ClientView view;
	Client client;
	Registry registry;
	Controller controller;
	
	public Controller(Client client) throws Exception
	{
		this.client=client;
		client.login("user", "user");
		client.getPlan("2019");
		
	}
	
	public void getPlans(ChoiceBox<PlanFile> plans) throws Exception
	{
		plans.getItems().addAll(client.getPlans());
	}
	

	public TreeView<Node> makeTree(String year) throws IllegalArgumentException, RemoteException
	{
		
		client.getPlan(year);
		Node nodeRoot = client.getCurrNode();
		TreeItem<Node>treeRoot = new TreeItem<Node>(nodeRoot);
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot);
		return new TreeView<Node>(treeRoot);
	}
	
	private static void recursiveSearch(TreeItem<Node> parent)
	{
	    if (parent.getValue().getChildren().size()!=0)
	    {
	      
	   
	    	for (int i = 0; i < parent.getValue().getChildren().size(); i++) 
	    	{   
	    		TreeItem<Node> tree1 = new TreeItem<Node>(parent.getValue().getChildren().get(i));
	    		tree1.setExpanded(true);
	    		parent.getChildren().add(tree1);
	    		recursiveSearch(tree1);
	    	}
	    }

	 }
	
	public void addBranch(TreeItem<Node> currNode,String year) throws Exception
	{
		client.getPlan(year);
		client.setCurrNode(currNode.getValue().getChildren().get(0));
		client.addBranch();
		TreeItem<Node> tree2 = new TreeItem<Node>(currNode.getValue().getChildren().get(currNode.getValue().getChildren().size() -1));
		recursiveSearch(tree2);
		currNode.getChildren().add(tree2);
	}
	
	public void save(Node node, String s) throws RemoteException
	{
		Centre plan = new Centre(node);
		PlanFile planF =new PlanFile(s ,true,plan);
		client.pushPlan(planF);
	}
	
	public TreeView<Node> makeDeepCopy(String year) throws IllegalArgumentException, RemoteException
	{
		client.getPlan(year);
		Node master = client.getCurrNode();
		Node copy = new Node(null, master.getName(), master.getData(), null);
		deepCopier(master,copy);
		TreeItem<Node>treeRoot = new TreeItem<Node>(copy);
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot);
		return new TreeView<Node>(treeRoot);
		
		
	}

	private static void deepCopier(Node master, Node copy) throws RemoteException
	{
	    if (master.getChildren().size()!=0)
	    {
	      
	    	for (int i = 0; i < master.getChildren().size(); i++) 
	    	{   
	    		Node tree1 = new Node(copy, master.getChildren().get(i).getName(),master.getChildren().get(i).getData(),null);
	    		copy.getChildren().add(tree1);
	    		master=master.getChildren().get(i);
	    		deepCopier(master, tree1);
	    	}
	    }
		
	}
}
