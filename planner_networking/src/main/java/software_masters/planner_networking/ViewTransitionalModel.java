package software_masters.planner_networking;

import java.io.IOException;

public interface ViewTransitionalModel
{

	public void showMainView(String user) throws IOException, Exception;

	public void showLogin() throws IOException;

	public void showCompare(String user) throws IOException, Exception;

	void setClient(Client client) throws IOException;

}
