package software_masters.planner_networking;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;


public class ClientView extends BorderPane{
	
	Client client;
	Controller controller;
	TreeItem<Node> treeRoot;
	TreeItem<Node> currNode;
	TreeView<Node> tree;
	Registry registry;
	Node nodeRoot;
	
	TextField dataTxt;
	Button edit;
	ChoiceBox<PlanFile> plans;
	Button btn;
	Scene scene;
	Button chooseYear;
	Button remove;
	Button save;
	Button temp;
	Button copy;
	TextField yearData; 
	Button subY;

	
	public ClientView(Controller controller, Client client) throws Exception
	{
		System.out.println("hello");
		this.client=client;
		this.controller=controller;


		btn = new Button("Add New Child");
		btn.setOnAction(e -> {
			try {
				addBranch();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		btn.setDisable(true);
		
		
		edit = new Button("Edit");
		edit.setOnAction(e -> {
			try {
				editView();
			} catch (IllegalArgumentException | RemoteException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		});
		
		
		temp = new Button("Set Text");
		temp.setOnAction(e-> setText());
		temp.setDisable(true);
		
		
		save = new Button("Save");
		save.setDisable(true);
		save.setOnAction(e -> {
			try {
				save();
			} catch (RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		
		
		plans = new ChoiceBox<PlanFile>();
		controller.getPlans(plans);
		
		
		tree=new TreeView<Node>(treeRoot);
		
		
		chooseYear = new Button("Select");
		chooseYear.setOnAction(e -> {
			try {
				planChange();
			} catch (IllegalArgumentException | RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		
		remove = new Button();
		remove.setDisable(true);
		remove.setText("Remove Current Branch");
		remove.setOnAction(e ->remove());
		
		
		dataTxt = new TextField();
		dataTxt.setMinHeight(90);
		dataTxt.setEditable(false);
		
		Label yearlbl = new Label("Year: ");
		yearlbl.setMinWidth(75);
		
		
		yearData=new TextField();
		yearData.setMinWidth(200);
		yearData.setEditable(false);
		
		
		subY=new Button("Submit new year");
		subY.setDisable(true);
		subY.setOnAction(e -> {
			try {
				newPlan();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		
		copy = new Button("Use as template");
		copy.setOnAction(e -> {
			try {
				copy();
			} catch (IllegalArgumentException | RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		copy.setDisable(true);
		
		Label txLb=new Label("Fill in the content below");
		
		Region spacer1=new Region();
		Region spacer2=new Region();
		VBox.setVgrow(spacer1, Priority.ALWAYS);
		VBox.setVgrow(spacer2, Priority.ALWAYS);
		
		
		HBox h = new HBox(plans,chooseYear);
		BorderPane pane1 = new BorderPane();
		BorderPane pane = new BorderPane();
		VBox v =new VBox(btn,remove,edit,save,copy);
		HBox yearPane = new HBox(10,yearlbl, yearData,subY);
		VBox v2 = new VBox(spacer1, txLb,dataTxt,temp,spacer2);
		
		pane.setLeft(tree);
		pane.setTop(h);
		pane.setRight(v);
		pane1.setCenter(v2);
		pane1.setTop(yearPane);
		pane.setCenter(pane1);
		scene = new Scene(pane);
	}
	
	public void tree_SelectionChanged(TreeItem<Node> item)
	{
		if (item != null)
		{
			String str = item.getValue().getData();
			dataTxt.setText(str);
			this.currNode = item;
		}
	}
	

	public void planChange() throws IllegalArgumentException, RemoteException
	{
		
		tree.setRoot(controller.makeTree(plans.getValue().getYear()).getRoot());
		
		tree.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) ->
				tree_SelectionChanged(newValue));
		edit.setText("View");
		this.editView();
	}
	
	
	
	public void addBranch() throws Exception
	{
		controller.addBranch(currNode,plans.getValue().getYear());
		
	}
	
	public void remove()
	{

		if (currNode.getParent().getChildren().size() >1)
		{
			client.setCurrNode(currNode.getValue());
			client.getCurrNode().getParent().removeChild(client.getCurrNode());
			currNode.getParent().getChildren().remove(currNode);
		}
	}
	
	public void editView() throws IllegalArgumentException, RemoteException
	{
		client.getPlan(plans.getValue().getYear());
		Boolean bool= client.getCurrPlanFile().isCanEdit();
		

		if (edit.getText() == "Edit" && bool)
		{
			edit.setText("View");
			dataTxt.setEditable(true);
			btn.setDisable(false);
			remove.setDisable(false);
			save.setDisable(false);
			temp.setDisable(false);
			copy.setDisable(false);
		}
		else if (edit.getText() == "View")
		{
			edit.setText("Edit");
			dataTxt.setEditable(false);
			btn.setDisable(true);
			remove.setDisable(true);
			save.setDisable(true);
			temp.setDisable(true);
			copy.setDisable(true);
		}
		
	
	}
	
	public void save() throws RemoteException
	{
		currNode.getValue().setData(dataTxt.getText());
		controller.save(tree.getRoot().getValue(),plans.getValue().getYear());
	}
	
	public void setText()
	{
		currNode.getValue().setData(dataTxt.getText());
	}
	
	public void copy() throws IllegalArgumentException, RemoteException
	{
		yearData.setText("");
		yearData.setEditable(true);
		subY.setDisable(false);
		String year=plans.getValue().getYear();
		chooseYear.setDisable(true);
		plans.setDisable(true);
		tree.setRoot(controller.makeDeepCopy(year).getRoot());
		tree.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) ->
			tree_SelectionChanged(newValue));
		edit.setText("View");
		edit.setDisable(true);
		save.setDisable(true);
	}
	
	public void newPlan() throws Exception
	{
		String year= yearData.getText();
		if (year.length() >=1)
		{
		currNode.getValue().setData(dataTxt.getText());
		controller.save(tree.getRoot().getValue(),year);
		chooseYear.setDisable(false);
		plans.setDisable(false);
		edit.setDisable(false);
		client.getPlan(yearData.getText());
		plans.getItems().add(client.getCurrPlanFile());
		yearData.setText("");
		yearData.setEditable(false);
		subY.setDisable(true);
		save.setDisable(false);
		}
		else
		{
			System.out.println("Please enter a valid year");
		}
		
	}
	
}
