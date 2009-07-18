package matlab.jpar.solver;

import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import matlab.jpar.client.common.JParClient;
import matlab.jpar.server.common.JParServer;
import matlab.jpar.solver.common.JParSolver;


public class JParSolverImpl extends UnicastRemoteObject implements
		JParSolver {

	private JParServer server;

	private Object taskSynchObj;
	private String taskID;
	private Remote taskClient;
	private Object newArgs[];	
	private int task_no;
	
	private static final int JOB_WAITING_FOR = 0;
	private static final int JOB_TODO = 1;
	private static final int JOB_KILL = 2;
	private int pendingJob;
	
	// this flag should be checked by M-scripts
	private boolean initialized = false;
	
	public boolean isInitialized() {
		return initialized;
	}	
	
	private static final long serialVersionUID = 7526472295622776147L;

	public int getTask_no() {
		return task_no;	}

	
	public Object[] getNewArgs() {
		return newArgs;
	}

	public boolean waitForJob() {
		while (true) {
			synchronized (taskSynchObj) {
				switch(pendingJob) {
				  case JOB_WAITING_FOR:
					try {
						taskSynchObj.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				  case JOB_TODO:
					pendingJob = JOB_WAITING_FOR;
					return true;
				  case JOB_KILL:
					return false;
				}
			}
		}
	}

	public JParSolverImpl(String host) throws RemoteException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		
		String serverName = "JParServer";		
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			this.server = (JParServer) registry.lookup(serverName);
		} catch (RemoteException e) {
			System.err.println("Solver: Java RMI Exception:" + e.getMessage());
			return;			
		} catch (NotBoundException e) {
			System.err.println("Solver: " + serverName + " lookup failed:" + e.getMessage());
			return;
		} catch (Exception e) {
			System.err.println("Solver: JParSolver Constructor:" + e.getMessage());					
			e.printStackTrace();
			return;
		}
		
		this.taskSynchObj = new Object();
		this.pendingJob = JOB_WAITING_FOR;
		
		if (this.register()) {
			initialized = true;
		}
	}

	public boolean executeTask(Remote cli, String taskID, int nargout, String argout, String func, Object[] args)
			throws RemoteException {

		System.out.println("Solver: executeTask(): New task: " + taskID);

		synchronized (this.taskSynchObj) {
			if (this.taskID == null) {
				this.taskID = taskID;
				this.taskClient = cli;
			} else {
				System.out.println("Solver: I have already task: " + this.taskID);
				return false;
			}

			try {
				int numArgs = (args == null) ? 0 : args.length;
				newArgs = new Object[numArgs + 4];
				newArgs[0] = this;
				newArgs[1] = (Object) new Integer(nargout);
				newArgs[2] = argout;
				newArgs[3] = func;
				for (int i = 0; i < numArgs; i++) {
					newArgs[i + 4] = args[i];
				}
				task_no++;
				pendingJob = JOB_TODO;
				taskSynchObj.notify();
			} catch (Exception e) {
				System.err.println("Solver: JParSolver Executing Task:"
						+ e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean taskIsDone(Object[] retVal) throws RemoteException {
		System.out.println("Solver: taskIsDone(): Old task: " + this.taskID);

		synchronized (this.taskSynchObj) {
			try {
				boolean ret = ((JParClient)this.taskClient).taskIsDone(this.taskID, retVal);
				if (!ret) {
					System.out.println("Solver: Client does not want results");
					System.out.println("Solver: I am dead...");
					return ret;
				}
				ret = this.register();
				if (!ret) {
					System.out.println("Solver: Server can not register me");
					System.out.println("Solver: I am dead...");
					return ret;
				}
			} catch (Exception e) {
				System.err.println("Solver: JParSolver Task Is Done:"
						+ e.getMessage());
				e.printStackTrace();
				return false;
			}

			this.taskID = null;
			this.taskClient = null;
		}
		
		return true;
	}

	private boolean register() {
		try {
			return server.registerSolver(this);
		} catch (Exception e) {
			System.err.println("Solver: JParSolver Registering Exception:"
					+ e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public void kill() throws RemoteException {
		synchronized (taskSynchObj) {
			pendingJob = JOB_KILL;
			taskSynchObj.notify();
		}
	}
	
	public String getSolverName() throws RemoteException {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException e) {
            return "unknown";
        }
	}
}