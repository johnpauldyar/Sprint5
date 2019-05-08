package software_masters.planner_networking;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainController
{

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
	private TextField newYearTxtField;

	@FXML
	private Button enterNewYearButton;

	@FXML
	private TreeView<Node> tree;
    
    @FXML
    Label userId;
    
    @FXML
    private TextField comment;
    
    @FXML
    private Button commentSubmit;
    
    @FXML
    private VBox commentVBox;

	private Client client;

	private TreeItem<Node> currNode;
	
	@FXML
	private Button compare;

	ViewTransitionalModel vtmodel;

	ChangeListener<String> listener;
	
	String user;

	public void setViewTransitionalModel(ViewTransitionalModel model)
	{
		this.vtmodel = model;
	}

	public void setClient(Client client)
	{

		this.client = client;
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
		removeButton.setDisable(true);
		addChildButton.setDisable(true);
		enterNewYearButton.setDisable(false);
		copyButton.setDisable(true);
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
			saveButton.setDisable(false);
			copyButton.setDisable(false);
			if (currNode != null )
			{
				addChildButton.setDisable(false);
				removeButton.setDisable(false);
			}
		} else if (editButton.getText() == "View")
		{
			editButton.setText("Edit");
			contentField.setEditable(false);
			addChildButton.setDisable(true);
			removeButton.setDisable(true);
			saveButton.setDisable(true);
			copyButton.setDisable(true);
		}

	}

	@FXML
	void logout(MouseEvent event) throws IOException
	{
		boolean confirm = false;
		TreeItem<Node> check = tree.getRoot();

		confirm = ConfirmationBox.show("Do you want to save before you log out?", "Save", "Yes", "No");
		if (confirm)
		{
			if (!(check == null))
			{
				saveC(tree.getRoot().getValue(), yearDropdown.getValue().getYear());
			}
		}
		vtmodel.showLogin();
	}

	@FXML
	void newPlan(MouseEvent event) throws Exception
	{
		String year = newYearTxtField.getText();
		if (year.length() >= 1)
		{
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
			yearDropdown.getItems().clear();
			getPlans(yearDropdown);
			tree.setRoot(null);
			addChildButton.setDisable(true);
			removeButton.setDisable(true);
			editButton.setDisable(true);
			saveButton.setDisable(true);
		} else
		{
			System.out.println("Please enter a valid year please");
		}

	}

	@FXML
	void planChange(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		editButton.setDisable(false);
		currNode = null;
		logout.setDisable(false);
		comment.setDisable(true);
		commentSubmit.setDisable(true);
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
	void compare(MouseEvent event) throws IOException, Exception
	{
		vtmodel.showCompare(user);
	}

	@FXML
	void save(MouseEvent event) throws RemoteException
	{
		saveC(tree.getRoot().getValue(), yearDropdown.getValue().getYear());
	}
	
	@FXML
	public void addComment(MouseEvent event) throws RemoteException
	{
		ArrayList<String> help = currNode.getValue().getComments();
		String string = comment.getText();
		string= user + ": " + string;
		help.add(string);
		currNode.getValue().setComments(help);
		save(event);
		comment.setText("");
		makeComments();
	}

	void setText(String newvalue)
	{
		currNode.getValue().setData(newvalue);
	}

	void tree_SelectionChanged(TreeItem<Node> item)
	{
		if (listener != null)
		{
			contentField.textProperty().removeListener(listener);
		}
		if (item != null)
		{
			String str = item.getValue().getData();
			contentField.setText(str);
			comment.setDisable(false);
			commentSubmit.setDisable(false);
			comment.setText("");
			this.currNode = item;
			if (editButton.getText() == "View")
			{
				addChildButton.setDisable(false);
				removeButton.setDisable(false);
			}
			makeComments();

		}
		listener = (observable, oldvalue, newvalue) -> setText(newvalue);
		contentField.textProperty().addListener(listener);
		

	}
	
	public void makeComments()
	{
		if (commentVBox.getChildren() != null)
		{
			commentVBox.getChildren().clear();
		}
		ArrayList<String> comments = currNode.getValue().getComments();
		for (int i = 0; i < comments.size(); i++)
		{
			Button rmv = new Button("Remove");
			Label lbl = new Label(comments.get(i));
			HBox hbox = new HBox(lbl,rmv);
			commentVBox.getChildren().add(hbox);
			rmv.setOnAction(e -> {
				try {
					removeComment(hbox);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
		}
		
	}
	
	public void removeComment(HBox hbox) throws RemoteException
	{
		String str = ((Label) hbox.getChildren().get(0)).getText();
		ArrayList<String> help = currNode.getValue().getComments();
		help.remove(str);
		currNode.getValue().setComments(help);
		makeComments();
		saveC(tree.getRoot().getValue(), yearDropdown.getValue().getYear());
		
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
		Node copy = new Node(null, master.getName(), master.getData(), null,null);
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
						master.getChildren().get(i).getData(), null,null);
				copy.getChildren().add(tree1);
				deepCopier(master.getChildren().get(i), tree1);
			}
		}

	}
	public void setUser(String user)
	{
		this.user=user;
	}
	


}
