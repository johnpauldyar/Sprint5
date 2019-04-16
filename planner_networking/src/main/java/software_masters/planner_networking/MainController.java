package software_masters.planner_networking;

import java.rmi.RemoteException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class MainController {

    @FXML
    ChoiceBox<PlanFile> yearDropdown;

    @FXML
    private Button yearSelectButton;

    @FXML
    private Button addChildButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button copyButton;

    @FXML
    private Button logout;

    @FXML
    private TextField contentField;

    @FXML
    private Button setContentButton;

    @FXML
    private TextField newYearTxtField;

    @FXML
    private Button enterNewYearButton;

    @FXML
    private TreeView<Node> tree;
    
    private Client client;
    
    private TreeItem<Node> currNode;
    
    public void setClient(Client client)
    {
    	this.client=client;
    }

    @FXML
    void addBranch(MouseEvent event) throws Exception 
    {
    	addBranch(currNode, yearDropdown.getValue().getYear());
    }

    @FXML
    void copy(MouseEvent event) throws IllegalArgumentException, RemoteException 
    {
		newYearTxtField.setText("");
		newYearTxtField.setEditable(true);
		enterNewYearButton.setDisable(false);
		String year = yearDropdown.getValue().getYear();
		yearSelectButton.setDisable(true);
		yearDropdown.setDisable(true);
		tree.setRoot(makeDeepCopy(year).getRoot());
		tree.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> tree_SelectionChanged(newValue));
		editButton.setText("View");
		editButton.setDisable(true);
		saveButton.setDisable(true);
    }

    @FXML
    void edit(MouseEvent event) throws IllegalArgumentException, RemoteException 
    {
		client.getPlan(yearDropdown.getValue().getYear());
		Boolean bool = client.getCurrPlanFile().isCanEdit();

		if ((editButton.getText() == "Edit") && bool)
		{
			editButton.setText("View");
			contentField.setEditable(true);
			addChildButton.setDisable(false);
			removeButton.setDisable(false);
			saveButton.setDisable(false);
			setContentButton.setDisable(false);
			copyButton.setDisable(false);
		} else if (editButton.getText() == "View")
		{
			editButton.setText("Edit");
			contentField.setEditable(false);
			addChildButton.setDisable(true);
			removeButton.setDisable(true);
			saveButton.setDisable(true);
			setContentButton.setDisable(true);
			copyButton.setDisable(true);
		}

    }

    @FXML
    void logout(MouseEvent event) {

    }

    @FXML
    void newPlan(MouseEvent event) throws IllegalArgumentException, RemoteException 
    {
		String year = newYearTxtField.getText();
		if (year.length() >= 1)
		{
			currNode.getValue().setData(contentField.getText());
			saveC(tree.getRoot().getValue(), year);
			yearSelectButton.setDisable(false);
			yearDropdown.setDisable(false);
			editButton.setDisable(false);
			client.getPlan(newYearTxtField.getText());
			yearDropdown.getItems().add(client.getCurrPlanFile());
			newYearTxtField.setText("");
			newYearTxtField.setEditable(false);
			enterNewYearButton.setDisable(true);
			saveButton.setDisable(false);
		} else
		{
			System.out.println("Please enter a valid year");
		}

    }

    @FXML
    void planChange(MouseEvent event) throws IllegalArgumentException, RemoteException 
    {
		tree.setRoot(makeTree(yearDropdown.getValue().getYear()).getRoot());

		tree.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> tree_SelectionChanged(newValue));
		editButton.setText("View");
		edit(event);

    }

    @FXML
    void remove(MouseEvent event)
    {
		if (currNode.getParent().getChildren().size() > 1)
		{
			client.setCurrNode(currNode.getValue());
			client.getCurrNode().getParent().removeChild(client.getCurrNode());
			currNode.getParent().getChildren().remove(currNode);
		}
    }

    @FXML
    void save(MouseEvent event) throws RemoteException 
    {
		currNode.getValue().setData(contentField.getText());
		saveC(tree.getRoot().getValue(), yearDropdown.getValue().getYear());
    }

    @FXML
    void setText(MouseEvent event) 
    {
		currNode.getValue().setData(contentField.getText());
    }

    void tree_SelectionChanged(TreeItem<Node> item)
    {
		if (item != null)
		{
			String str = item.getValue().getData();
			contentField.setText(str);
			this.currNode = item;
		}
    }
    
    
    
    
    
    
    
    /**
	 * Gets the plans of the department for display in a choiceBox
	 * 
	 * @param ChoiceBox<PlanFile> plans
	 * @throws Exception
	 */
	public void getPlans(ChoiceBox<PlanFile> plans) throws Exception
	{
		plans.getItems().addAll(client.getPlans());
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
				TreeItem<Node> tree1 = new TreeItem<Node>(parent.getValue().getChildren().get(i));
				tree1.setExpanded(true);
				parent.getChildren().add(tree1);
				recursiveSearch(tree1);
			}
		}

	}

	/**
	 * Creates a new branch for the Node tree, then adds it to the treeView
	 * 
	 * @param currNode
	 * @param year
	 * @throws Exception
	 */
	public void addBranch(TreeItem<Node> currNode, String year) throws Exception
	{
		client.getPlan(year);
		client.setCurrNode(currNode.getValue().getChildren().get(0));
		client.addBranch();
		TreeItem<Node> tree2 = new TreeItem<Node>(
				currNode.getValue().getChildren().get(currNode.getValue().getChildren().size() - 1));
		recursiveSearch(tree2);
		currNode.getChildren().add(tree2);
	}

	/**
	 * Saves a planFile to the server
	 * 
	 * @param node
	 * @param s
	 * @throws RemoteException
	 */
	public void saveC(Node node, String s) throws RemoteException
	{
		Centre plan = new Centre(node);
		PlanFile planF = new PlanFile(s, true, plan);
		client.pushPlan(planF);
	}

	/**
	 * Makes a deep copy of a tree for use as templates
	 * 
	 * @param year
	 * @return TreeView<Node>(treeRoot)
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public TreeView<Node> makeDeepCopy(String year) throws IllegalArgumentException, RemoteException
	{
		client.getPlan(year);
		Node master = client.getCurrNode();
		Node copy = new Node(null, master.getName(), master.getData(), null);
		deepCopier(master, copy);
		TreeItem<Node> treeRoot = new TreeItem<Node>(copy);
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot);
		return new TreeView<Node>(treeRoot);

	}

	/**
	 * recursive searcher for the deep copier
	 * 
	 * @param master
	 * @param copy
	 * @throws RemoteException
	 */
	private static void deepCopier(Node master, Node copy) throws RemoteException
	{
		if (master.getChildren().size() != 0)
		{

			for (int i = 0; i < master.getChildren().size(); i++)
			{
				Node tree1 = new Node(copy, master.getChildren().get(i).getName(),
						master.getChildren().get(i).getData(), null);
				copy.getChildren().add(tree1);
				master = master.getChildren().get(i);
				deepCopier(master, tree1);
			}
		}

	}

}
