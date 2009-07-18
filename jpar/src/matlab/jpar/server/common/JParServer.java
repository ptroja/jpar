package matlab.jpar.server.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import matlab.jpar.solver.common.JParSolver;

public interface JParServer extends Remote {

	public boolean registerSolver(JParSolver reqID) throws RemoteException;
	
	public JParSolver getFreeSolver() throws RemoteException;
	
	public void killSolvers() throws RemoteException;
	
	public String [] getSolvers() throws RemoteException;
}
