package matlab.jpar.solver.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JParSolver extends Remote {

	public boolean executeTask(Remote client, String taskID,
			int nargout, String argout, String func,
			Object[] args) throws RemoteException;
	
	public void kill() throws RemoteException;
	
	public String getSolverName() throws RemoteException;
}
