The UCaPE repository contains the sources of all the examples and exercises
used in the book "Using Concurrency and Parallelism Effectively parts i & ii"
by Jon Kerridge published by Bookboon, free of charge.

http://bookboon.com/en/using-concurrency-and-parallelism-effectively-i-ebook  
http://bookboon.com/en/using-concurrency-and-parallelism-effectively-ii-ebook  

In order to use these source files you will need to put them into your IDE, which
will need Apache Groovy enabled( http://groovy-lang.org/download.html );
and associate them with  the following libraries, which are available as Github Packages

jcsp from https://github.com/CSPforJAVA/jcsp  
groovyJCSP from https://github.com/JonKerridge/groovyJCSP  

In order to use jcsp, groovyJCSP and Groovy the following will be required in the 
build.gradle file.
<pre>
repositories {
    mavenCentral()
    maven { // to download the CSPforJAVA.jcsp library
        name = "GitHubPackages"
        url = "https://maven.pkg.github.com/CSPforJAVA/jcsp"
        credentials {
            username = project.findProperty("gpr.user")
            password = project.findProperty("gpr.key")
        }
    }
    maven { // to download the jonkerridge.groovy_jcsp library
        name = "GitHubPackages"
        url = "https://maven.pkg.github.com/JonKerridge/groovyJCSP"
        credentials {
            username = project.findProperty("gpr.user")
            password = project.findProperty("gpr.key")
        }
    }
}

dependencies {
    implementation 'org.codehaus.groovy:groovy-all:3.0.7'
    implementation 'cspforjava:jcsp:1.1.9'
    implementation 'jonkerridge:groovy_jcsp:1.1.9'
    implementation group: 'junit', name: 'junit', version: '4.13.1'
    testImplementation group: 'org.codehaus.groovy', name: 'groovy-all', version: '3.0.7'
    testImplementation 'cspforjava:jcsp:1.1.9'
    testImplementation 'jonkerridge:groovy_jcsp:1.1.9'
    testImplementation group: 'junit', name: 'junit', version: '4.13.1'
}
</pre>
You will also need to include junit.jar in the build dependencies as shown.

**Please note**

In order to download Github Packages a user requires to have a Github Personal Access Token.  
See https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token

A gradle.properties file is required at the same directory level as the build.gradle file that contains

<pre>
gpr.user=userName
gpr.key=userPersonalAccessToken
</pre>

You will need to ensure that the java and groovy versions of the libraries match
with the versions you are using in your IDE are Java 8+ and Groovy 3.0.7+

It should be noted that the exercises packages import code from the examples package
and this will need to be maintained in your IDE.

The line numbers shown in the code samples in the books will line up with the line 
numbers in your IDE.

A simple test to ensure that the software has been installed correctly is:
navigate from examples> src> c02> RunHelloWorld.groovy, which is a runnable script

Use the IDE to run the script and 'Hello World!' should appear in the console.

In Chapters 15 onwards, contained in part ii of the book, examples of the use of 
networked systems is presented.  Some of these chapter contain two packages one 
labelled net and the other called net2.  The net2 packages are the ones described 
in the book and require no further intervention except to run the main Groovy scripts
as described in the text.

The net package examples require the running of the CNS-Server **_before_** any 
of the other components are invoked.  Each package has a pdf containing a description,
which came from an earlier version of the books, which was not actually published.  
This was because the net2 became available and has more functionality, especially 
where mobility of processes was concerned.

The CNS-Server is found in jcsp.net.tcpip.TCPIPCNSServer within the jcsp-?.?.?.jar
library and can be run directly from the library in your IDE.