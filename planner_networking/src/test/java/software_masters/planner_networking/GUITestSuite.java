package software_masters.planner_networking;

import java.rmi.RemoteException;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CompareTest.class, LoginTest.class, MainGUITest.class })
public class GUITestSuite 
{
	@BeforeClass
	public static void setup() throws RemoteException
	{
		ServerImplementation.main(null);
	}
}
