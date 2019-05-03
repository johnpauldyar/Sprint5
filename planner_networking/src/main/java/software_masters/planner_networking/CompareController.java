package software_masters.planner_networking;

import java.rmi.RemoteException;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

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
    
	ViewTransitionalModel vtmodel;
	
	private Client client;
	
	private TreeItem<Node> currNode;


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
				.addListener((v, oldValue, newValue) -> tree_SelectionChanged(newValue));

	}
	@FXML
	void planChange2(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		tree2.setRoot(makeTree(drop1.getValue().getYear()).getRoot());

		tree2.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> tree_SelectionChanged(newValue));

	}
	
	void tree_SelectionChanged(TreeItem<Node> item)
	{

		if (item != null)
		{
			String str = item.getValue().getData();
			
			this.currNode = item;

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

}
