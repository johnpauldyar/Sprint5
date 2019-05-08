package software_masters.planner_networking;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lee Kendall
 * @author Wesley Murray
 * 
 *         This class is the server for our business planner application
 *
 *         initialized with two accounts - an Admin(Username: admin, password:
 *         admin, cookie: 0) and a normal user (Username: user, password: user,
 *         cookie: 1) initialized with one department - (name: default) The
 *         default department has a default plan file - (year: "2019", candEdit:
 *         true, Plan Centre_Plan_1) planTemplateMap is initialized with VMOSA
 *         and Centre
 */

public class ServerImplementation implements Server
{

	private ConcurrentHashMap<String, Account> loginMap = new ConcurrentHashMap<String, Account>();
	private ConcurrentHashMap<String, Account> cookieMap = new ConcurrentHashMap<String, Account>();
	private ConcurrentHashMap<String, Department> departmentMap = new ConcurrentHashMap<String, Department>();
	private ConcurrentHashMap<String, PlanFile> planTemplateMap = new ConcurrentHashMap<String, PlanFile>();

	/**
	 * Initializes server with default objects listed above for testing
	 * 
	 */
	public ServerImplementation() throws RemoteException
	{
		Department dpt = new Department();
		departmentMap.put("default", dpt);

		Account admin = new Account("admin", "0", dpt, true);
		Account user = new Account("user", "1", dpt, false);
		loginMap.put("admin", admin);
		loginMap.put("user", user);
		cookieMap.put("0", admin);
		cookieMap.put("1", user);

		Plan plan = new Centre();
		plan.setName("Centre_Plan_1");
		Node root = plan.getRoot();
		root.setData("Mission 1");
		root.getChildren().get(0).setData("Goal 1");
		root.getChildren().get(0).getChildren().get(0).setData("Learning Objective 1");
		root.getChildren().get(0).getChildren().get(0).getChildren().get(0).setData("Assessment Process 1");
		root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).setData("Results 1");

		PlanFile planfile = new PlanFile("2019", true, plan);
		dpt.addPlan("2019", planfile);

		Plan plan2 = new Centre();
		plan2.setName("Centre_Plan_2");
		Node root2 = plan2.getRoot();
		root2.setData("Mission to Mars");
		root2.getChildren().get(0).setData("Goal to get to mars");
		root2.getChildren().get(0).getChildren().get(0).setData("Learning Objective: learn about mars");
		root2.getChildren().get(0).getChildren().get(0).getChildren().get(0)
				.setData("Assessment Process: are we on mars");
		root2.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0)
				.setData("Result: I am on mars");

		PlanFile planfile2 = new PlanFile("2017", false, plan2);
		dpt.addPlan("2017", planfile2);

		Plan defaultCentre = new Centre();
		Plan defaultVMOSA = new VMOSA();
		planTemplateMap.put("Centre", new PlanFile(null, true, defaultCentre));
		planTemplateMap.put("VMOSA", new PlanFile(null, true, defaultVMOSA));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#logIn(java.lang.String,
	 * java.lang.String)
	 */

	public String login(String username, String password)
	{
		if (!loginMap.containsKey(username))// checks username is valid
		{
			throw new IllegalArgumentException("Invalid username and/or password");

		}
		Account userAccount = loginMap.get(username);

		String cookie = userAccount.testCredentials(password);
		return cookie;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#getPlan(java.lang.String,
	 * java.lang.String)
	 */

	public PlanFile getPlan(String year, String cookie)
	{
		cookieChecker(cookie);// checks that cookie is valid

		Account userAccount = cookieMap.get(cookie);
		Department department = userAccount.getDepartment();
		if (!department.containsPlan(year))
		{
			throw new IllegalArgumentException("Plan doesn't exist within your department");

		}
		return department.getPlan(year);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#getPlans(java.lang.String)
	 */
	public Collection<PlanFile> getPlans(String cookie)
	{
		cookieChecker(cookie);// checks that cookie is valid

		Account userAccount = cookieMap.get(cookie);
		Department department = userAccount.getDepartment();
		return department.getPlans();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * software_masters.planner_networking.Server#getPlanOutline(java.lang.String,
	 * java.lang.String)
	 */

	public PlanFile getPlanOutline(String name, String cookie)
	{
		cookieChecker(cookie);// checks that cookie is valid

		if (!planTemplateMap.containsKey(name))// checks plan template exists
		{
			throw new IllegalArgumentException("Plan outline doesn't exist");

		}
		return planTemplateMap.get(name);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#savePlan(software_masters.
	 * planner_networking.PlanFile, java.lang.String)
	 */

	public void savePlan(PlanFile plan, String cookie)
	{
		cookieChecker(cookie);// checks that cookie is valid

		if (plan.getYear() == null)// checks planFile is given a year
		{
			throw new IllegalArgumentException("This planFile needs a year!");
		}

		Account userAccount = cookieMap.get(cookie);
		Department dept = userAccount.getDepartment();

		if (dept.containsPlan(plan.getYear()))
		{
			PlanFile oldPlan = dept.getPlan(plan.getYear());
			if (!oldPlan.isCanEdit())// checks planFile is editable
			{
				throw new IllegalArgumentException("Not allowed to edit this plan");
			}

		}
		dept.addPlan(plan.getYear(), plan);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#addUser(java.lang.String,
	 * java.lang.String, java.lang.String, boolean, java.lang.String)
	 */

	public void addUser(String username, String password, String departmentName, boolean isAdmin, String cookie)
	{
		cookieChecker(cookie);// checks that cookie is valid and that user is admin
		adminChecker(cookie);

		departmentChecker(departmentName);

		String newCookie = cookieMaker();
		Department newDept = departmentMap.get(departmentName);
		Account newAccount = new Account(password, newCookie, newDept, isAdmin);
		loginMap.put(username, newAccount);
		cookieMap.put(newAccount.getCookie(), newAccount);

	}

	/**
	 * Helper method to randomly generate a 25-character cookie. This method
	 * regenerates a cookie if it already exists in the cookieMap.
	 * 
	 * @return String cookie
	 */
	private String cookieMaker()
	{
		int leftLimit = 33; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 25;
		Random random = new Random();
		String generatedString;
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++)
		{
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * ((rightLimit - leftLimit) + 1));
			buffer.append((char) randomLimitedInt);
		}
		generatedString = buffer.toString();

		while (cookieMap.containsKey(generatedString))
		{
			buffer = new StringBuilder(targetStringLength);
			for (int i = 0; i < targetStringLength; i++)
			{
				int randomLimitedInt = leftLimit + (int) (random.nextFloat() * ((rightLimit - leftLimit) + 1));
				buffer.append((char) randomLimitedInt);
			}
			generatedString = buffer.toString();
		}
		return generatedString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#flagPlan(java.lang.String,
	 * java.lang.String, boolean, java.lang.String)
	 */

	public void flagPlan(String departmentName, String year, boolean canEdit, String cookie)
	{
		cookieChecker(cookie);// checks that cookie is valid and that user is admin
		adminChecker(cookie);
		departmentChecker(departmentName);

		Department dept = departmentMap.get(departmentName);
		if (!dept.containsPlan(year))
		{
			throw new IllegalArgumentException("Plan doesn't exist");

		}

		dept.getPlan(year).setCanEdit(canEdit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * software_masters.planner_networking.Server#addDepartment(java.lang.String,
	 * java.lang.String)
	 */

	public void addDepartment(String departmentName, String cookie)
	{
		cookieChecker(cookie);// checks that cookie is valid and that user is admin
		adminChecker(cookie);

		departmentMap.put(departmentName, new Department());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * software_masters.planner_networking.Server#addPlanTemplate(java.lang.String,
	 * software_masters.planner_networking.PlanFile)
	 */

	public void addPlanTemplate(String name, PlanFile plan)
	{
		planTemplateMap.put(name, plan);
	}

	/**
	 * Loads server from xml, called in main
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ServerImplementation load() throws FileNotFoundException
	{
		String filepath = "PlannerServer.serv";
		XMLDecoder decoder = null;
		decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filepath)));
		ServerImplementation server = (ServerImplementation) decoder.readObject();
		decoder.close();
		return server;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#save()
	 */

	public void save()
	{
		String filename = "PlannerServer.serv";
		XMLEncoder encoder = null;
		try
		{
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
		} catch (FileNotFoundException fileNotFound)
		{
			System.out.println("ERROR: While Creating or Opening the File " + filename);
		}
		encoder.writeObject(this);
		encoder.close();

	}

	/**
	 * Checks that the client is logged in with a valid cookie
	 * 
	 * @param cookie
	 * @throws IllegalArgumentException
	 */
	private void cookieChecker(String cookie)
	{
		if (!cookieMap.containsKey(cookie))
		{
			throw new IllegalArgumentException("Need to log in");

		}

	}

	/**
	 * Checks that the user is an admin
	 * 
	 * @param cookie
	 * @throws IllegalArgumentException
	 */
	private void adminChecker(String cookie)
	{
		if (!cookieMap.get(cookie).isAdmin())// Checks that user is admin
		{
			throw new IllegalArgumentException("You're not an admin");
		}
	}

	/**
	 * Checks department is valid
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 */
	private void departmentChecker(String name)
	{
		if (!departmentMap.containsKey(name))
		{
			throw new IllegalArgumentException("Deparment doesn't exist");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#getLoginMap()
	 */

	public ConcurrentHashMap<String, Account> getLoginMap()
	{
		return loginMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * software_masters.planner_networking.Server#setLoginMap(java.util.concurrent.
	 * ConcurrentHashMap)
	 */

	public void setLoginMap(ConcurrentHashMap<String, Account> loginMap)
	{
		this.loginMap = loginMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#getCookieMap()
	 */

	public ConcurrentHashMap<String, Account> getCookieMap()
	{
		return cookieMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * software_masters.planner_networking.Server#setCookieMap(java.util.concurrent.
	 * ConcurrentHashMap)
	 */

	public void setCookieMap(ConcurrentHashMap<String, Account> cookieMap)
	{
		this.cookieMap = cookieMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#getDepartmentMap()
	 */

	public ConcurrentHashMap<String, Department> getDepartmentMap()
	{
		return departmentMap;
	}

	public String getDepartmentMapString()
	{
		return departmentMap.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#setDepartmentMap(java.util.
	 * concurrent.ConcurrentHashMap)
	 */

	public void setDepartmentMap(ConcurrentHashMap<String, Department> departmentMap)
	{
		this.departmentMap = departmentMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#getPlanTemplateMap()
	 */

	public ConcurrentHashMap<String, PlanFile> getPlanTemplateMap()
	{
		return planTemplateMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#setPlanTemplateMap(java.util.
	 * concurrent.ConcurrentHashMap)
	 */

	public void setPlanTemplateMap(ConcurrentHashMap<String, PlanFile> planTemplateMap)
	{
		this.planTemplateMap = planTemplateMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Server#equals(java.lang.Object)
	 */

	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		ServerImplementation other = (ServerImplementation) obj;
		if (cookieMap == null)
		{
			if (other.cookieMap != null)
			{
				return false;
			}
		} else if (!ServerImplementation.<String, Account>hashesEqual(cookieMap, other.cookieMap))
		{
			return false;
		}
		if (departmentMap == null)
		{
			if (other.departmentMap != null)
			{
				return false;
			}
		} else if (!ServerImplementation.<String, Department>hashesEqual(departmentMap, other.departmentMap))
		{
			return false;
		}
		if (loginMap == null)
		{
			if (other.loginMap != null)
			{
				return false;
			}
		} else if (!ServerImplementation.<String, Account>hashesEqual(loginMap, other.loginMap))
		{
			return false;
		}
		if (planTemplateMap == null)
		{
			if (other.planTemplateMap != null)
			{
				return false;
			}
		} else if (!ServerImplementation.<String, PlanFile>hashesEqual(planTemplateMap, other.planTemplateMap))
		{
			return false;
		}
		return true;
	}

	/**
	 * Compares two hashes for testing
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
	private static <K, V> boolean hashesEqual(ConcurrentHashMap<K, V> map1, ConcurrentHashMap<K, V> map2)
	{
		for (Enumeration<K> keyList = map1.keys(); keyList.hasMoreElements();)
		{
			K key = keyList.nextElement();
			if (!map1.containsKey(key))
			{
				return false;
			}
			if (!map2.containsKey(key))
			{
				return false;
			}
			if (!map1.get(key).equals(map2.get(key)))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Starts the server, allows clients to access it
	 * 
	 * @param args
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException
	{
		System.out.println("Start Server");
		ServerImplementation server;
		Registry registry;
		try
		{
			registry = LocateRegistry.createRegistry(1060);
			server = ServerImplementation.load();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}

		Server stub = (Server) UnicastRemoteObject.exportObject(server, 0);
		try
		{
			registry.bind("PlannerServer", stub);
		} catch (java.rmi.AlreadyBoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
