package matlab.jpar.client.common;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JParClient extends Remote {

	public boolean taskIsDone(String taskID, Object [] ratVal) throws RemoteException;
	
}
