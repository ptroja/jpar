package matlab.jpar.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Iterator;

import matlab.jpar.server.common.JParServer;
import matlab.jpar.solver.common.JParSolver;

public class JParServerImpl extends UnicastRemoteObject implements
		JParServer {

	private static final long serialVersionUID = 7526472295622776147L;

	private HashSet freeSolvers;

	public JParServerImpl() throws RemoteException {
		freeSolvers = new HashSet();
	}
	
	public int getNumberOfAvailableSolvers() {
		synchronized (freeSolvers) {
			return freeSolvers.size();
		}
	}

	public JParSolver getFreeSolver() throws RemoteException {
		try {
			while (true) {
				synchronized (freeSolvers) {
					if (!freeSolvers.isEmpty()) {
						JParSolver solver = (JParSolver) freeSolvers.iterator().next();
						freeSolvers.remove(solver);
						System.out.println("Solvers available: " + freeSolvers.size());
						return solver;
					}
					freeSolvers.wait();
				}
			}
		} catch (InterruptedException e) {
			return null;
		}
	}

	public boolean registerSolver(JParSolver solver) throws RemoteException {
		synchronized (freeSolvers) {
			if (freeSolvers.add(solver)) {
				System.out.println("Solvers available: " + freeSolvers.size());				
				freeSolvers.notify();
				return true;
			} else {
				return false;
			}
		}
	}

	public void killSolvers () throws RemoteException {
		synchronized (freeSolvers) {			
			System.out.println("Killing solvers...");			
			while(!freeSolvers.isEmpty()) {
				JParSolver solver = (JParSolver) freeSolvers.iterator().next();
				solver.kill();
				freeSolvers.remove(solver);
				System.out.println("Solvers available: " + freeSolvers.size());
			}
			System.out.println("done.");
		}
	}

	public String [] getSolvers () throws RemoteException {
		synchronized (freeSolvers) {
			int i = 0;
			String [] ret = new String[freeSolvers.size()] ;
			for (Iterator it = freeSolvers.iterator(); it.hasNext(); i++) {
				JParSolver solver = (JParSolver) it.next();
				try {
					ret[i] = solver.getSolverName();
				} catch (Exception e) {
					System.err
					.println("Server: JParServer Get Solvers:"
							+ e.getMessage());
					e.printStackTrace();
				}
			}
			return ret;
		}
	}
	
	public static void main(String[] args) {
		String name = "JParServer";
		int port = 1099;
		try {
			JParServer server = new JParServerImpl();
			Registry r = LocateRegistry.createRegistry(port);
			// Registry r = LocateRegistry.getRegistry("localhost");
			r.bind(name, server);
		} catch (Exception e) {
			System.err
					.println("Server: JParServer Implementation Exception:"
							+ e.getMessage());
			e.printStackTrace();
		}
		System.out.println("JPar server running...");
	}

}
