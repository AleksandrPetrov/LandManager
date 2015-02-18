#About
LandManager is a small site for landlord to ease accounting (2013).

Customer allowed to share source code (no NDA).

All the data about customer and environment (database accounts) was removed from project.


#Technologies:

maven, spring, spring-security, hibernate/JPA, GWT, html, jsp, css, bootstrap

Target java: JDK 7

Target Application server: Tomcat 7 in Jelastic Cloud

Target Database: MySQL in Jelastic Cloud


#Usage:
### Launch in preview mode

Execute command in project root directory: mvn gwt:run -P hsqldb

URL: http://127.0.0.1:8888/LandManager.html?gwt.codesvr=127.0.0.1:9997

Project will be launched using GWT Development Mode tool (servlet container: Jetty) and hsqldb.

With one preconfigured user admin/admin.

### Launch in GWT debug mode (for Eclipse)

1) Execute: mvn gwt:debug -P hsqldb

or Execute launcher: /launchers/LandManager - gwt debug hsqldb.launch

2) Create Remote Java Application in Debug Configurations with connection properties localhost:8000

or Execute launcher: /launchers/LandManager - debug.launch
  
### Build for server

1) Configure you database and add new profile in pom.xml.
Refer to examples provided for mysql (profile name: mysql)

2) Execute: mvn clean compile gwt:compile war:war -P <your _profile_name>

3) WAR file will be created in /target directory (file name: landmanager.war)  
