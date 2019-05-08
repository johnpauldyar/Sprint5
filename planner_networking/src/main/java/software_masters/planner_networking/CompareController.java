package software_masters.planner_networking;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class CompareController {

    @FXML
    ChoiceBox<PlanFile> drop1;

    @FXML
    private TreeView<Node> tree1;

    @FXML
    private Label label1;

    @FXML
    ChoiceBox<PlanFile> drop2;

    @FXML
    private TreeView<Node> tree2;

    @FXML
    private Label label2;

    @FXML
    private Label diffLabel;

    @FXML
    private Button view;

    @FXML
    private Button logout;
    
    @FXML
    private Button select1;
    
    @FXML
    private Button select2;
    
    @FXML
    private BorderPane compareBorderPane;
    
	ViewTransitionalModel vtmodel;
	
	private Client client;
	
	private TreeItem<Node> currNode;
	
	ArrayList<String> helperList;

	ArrayList<Integer> intList;
	
	String user;

	public void setViewTransitionalModel(ViewTransitionalModel model)
	{
		this.vtmodel = model;
	}

	public void setClient(Client client)
	{

		this.client = client;
	}
	
	public void getPlans(ChoiceBox<PlanFile> plans) throws Exception
	{
		plans.getItems().addAll(client.getPlans());
	}
	
	@FXML
	void planChange1(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		tree1.setRoot(makeTree(drop1.getValue().getYear()).getRoot());

		tree1.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> tree_SelectionChanged1(newValue));
		if (tree2.getRoot() != null)
		{	
			compare();
		}

	}
	@FXML
	void planChange2(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		tree2.setRoot(makeTree(drop2.getValue().getYear()).getRoot());

		tree2.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> tree_SelectionChanged2(newValue));
		if (tree1.getRoot() != null)
		{	
			compare();
		}
	}
	
	@FXML
	void logout(MouseEvent event) throws IOException
	{
		vtmodel.showLogin();
	}
	
	@FXML
	void view(MouseEvent event) throws IOException, Exception
	{
		vtmodel.showMainView(user);
	}
	
	void tree_SelectionChanged1(TreeItem<Node> item)
	{

		if (item != null)
		{
			String str = item.getValue().getData();
			label1.setText(str);
			this.currNode = item;

		}

	}
	
	void tree_SelectionChanged2(TreeItem<Node> item)
	{

		if (item != null)
		{
			String str = item.getValue().getData();
			label2.setText(str);
			this.currNode = item;

		}

	}
	
	private void compare()
	{
		helperList = new ArrayList<String>();
		
		diffLabel.setText(" ");
		Node node1 = tree1.getRoot().getValue();
		Node node2 = tree2.getRoot().getValue();
		int level = 0;
		intList = new ArrayList<Integer>(drop1.getValue().getPlan().getDefaultNodes().size());
		for (int i = 0; i<drop1.getValue().getPlan().getDefaultNodes().size();i++ )
		{
			intList.add(0);
		}
		compareHelper(node1, node2, level);
		intList = new ArrayList<Integer>(drop2.getValue().getPlan().getDefaultNodes().size());
		for (int i = 0; i<drop2.getValue().getPlan().getDefaultNodes().size();i++ )
		{
			intList.add(0);
		}
		compareHelper(node2, node1, level);
		ArrayList<String> finalList = new ArrayList<String>();
		for (int i = 0; i < helperList.size(); i++)
		{
			if (!(finalList.contains(helperList.get(i))))
			{
				finalList.add(helperList.get(i));
			}
		}
		for (int i = 0; i <finalList.size(); i++)
		{
			diffLabel.setText(diffLabel.getText() + "\n" + finalList.get(i) + " differs");
		}
		
	}
	
	private void compareHelper(Node node1,Node node2, int level)
	{
		int local = level;
		
		intList.set(local, intList.get(local)+1);
		if (node2 != null)
		{
			if (!(node1.getData().equals(node2.getData()))) 
			{
				String str = node1.getName() + " " + Integer.toString(intList.get(local));
				helperList.add(str);
			}
		}
		else
		{
			helperList.add(node1.getName() + " " + Integer.toString(intList.get(local)));
		}
		if (node1.getChildren().size() != 0)
		{
			for (int i = 0; i < node1.getChildren().size(); i++)
			{
				if (node2 != null)
				{

					if (node2.getChildren().size() > i)
					{
						compareHelper(node1.getChildren().get(i),node2.getChildren().get(i), local+1);
					}
					else
					{
						compareHelper(node1.getChildren().get(i),null,local+ 1);
					}
				}
				else
				{
					compareHelper(node1.getChildren().get(i),null, 1+local);
				}
			}
		}
	}
	
	/**
	 * Creates a tree for display in the application
	 * 
	 * @param year
	 * @return TreeView<Node>(treeRoot)
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public TreeView<Node> makeTree(String year) throws IllegalArgumentException, RemoteException
	{

		client.getPlan(year);
		Node nodeRoot = client.getCurrNode();
		TreeItem<Node> treeRoot = new TreeItem<Node>(nodeRoot);
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot);
		return new TreeView<Node>(treeRoot);
	}
	/**
	 * used to recursive search through a graph to create a TreeView
	 * 
	 * @param parent
	 */
	private static void recursiveSearch(TreeItem<Node> parent)
	{
		if (parent.getValue().getChildren().size() != 0)
		{

			for (int i = 0; i < parent.getValue().getChildren().size(); i++)
			{
				TreeItem<Node> treeHelp = new TreeItem<Node>(parent.getValue().getChildren().get(i));
				treeHelp.setExpanded(true);
				parent.getChildren().add(treeHelp);
				recursiveSearch(treeHelp);
			}
		}

	}
	
	public void setUser(String user)
	{
		this.user = user;
	}

}
