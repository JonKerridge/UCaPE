package examples.c24.Distributed.launcher;

import groovy.lang.GroovyShell;
 





public class GroovyLauncher {
    public static void main(String[] args) {
    	/*
    	 * to create a launcher
    	 * 
    	 * run GroovyLauncher as a Java Application to get a launch configuration
    	 * 
    	 * on the whole project click export and select Runnable Jar
    	 * in the dialog choose the above Launch Configuration
    	 * 
    	 * save the jar in a file say Launcher.jar
    	 * 
    	 * now copy the Launcher.jar file AND the project work space to any other folder
    	 * 
    	 * you can run any groovy script in the project as follows (typically)
    	 * 
    	 * java -jar Launcher.jar .\project\src\package\script.groovy
    	 * 
    	 * probably easier to put it in a batch file
    	 */
    	
    	System.out.println("running script: " + args[0]);
        GroovyShell.main(args);
    }
}
