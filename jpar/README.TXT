BUILD:

1. To build jPar you need to have JDK (Java Development Kit) with "javac" and   
   "jar" tools installed. Start command line (shell) interface and add JDK
   tools directory to your PATH enviroment variableif if necessary:

   c:\jpar>set PATH="c:Program files\java\jdk1.6.0_13\bin\";%PATH% (Windows)

   Build the jpar.jar Java archive with command "compile.bat" (Windows) or
   "sh compile.sh" (Unix).

   NOTE: if you have Ant build tool (http://ant.apache.org/) installed just
   issue the command: "ant". This will build jpar.jar file in main directory.

INSTALL:

1. Copy file .java.policy to home directory (in Windows use 
   "Documents and Settings\Username" directory) or use "install.bat" (Windows)
   or "sh ./install.sh" (Unix) on every node, where you want to run 
   jPar client or solver

2. Check whether the folder with Java binaries is in the system path 
   writing "java" in the command window. If not, change the path variable,
   adding this directory. 
   Run one instance of jpar server using "jpar_server.bat" (Windows) 
   or "sh ./jpar_server.sh" (Unix) on node, where you want to run jpar
   clients

3. Every instance of Matlab has to be started from or have as a current 
   the directory which contains jpar files

4. Each solver has to be started from Matlab session in jpar directory using:

   >> jpar_solver(['hostname']);

   where 'hostname' is the name of host where jpar server is running
   (defaults to localhost)

5. You can test parallelization with the example command:

   >> a = rand(100,100,10) + i*rand(100,100,10);
   >> [V,D] = jpar_client('eig', a)
   
6. To kill solvers use the command:

   >> jpar_client('kill');

7. To see free solvers use the command:

   >> jpar_client('hosts');

AUTHORS:

Marek Majchrowski <M.Majchrowski@elka.pw.edu.pl>,
Piotr Trojanek <P.Trojanek@elka.pw.edu.pl>,
Andrzej Karbowski <A.Karbowski@ia.pw.edu.pl>.

NOT IMPLEMENTED:

* support for sparse matrices
