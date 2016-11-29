Camunda Social Cockpit Plugin
##############################

This is a plugin extension for the Camunda BPM platform. The Plugin is tested with Camunda BPM version 7.6

Installation:
#############

- Add a H2 JDBC Driver to the '/WEB-INF/lib' directory of the file 'camunda-webapp-jboss-7.6.0-alpha4.war'
- deploy the 'camunda-webapp-jboss-7.6.0-alpha4.war' on Wildfly 10 application server

Release Notes:
##############

V1.3
- Delete Tags from process-definitions
- Process name and version is visible in microblog entrie on dashboard

V1.2
- User modal added to show all interactions regarding a certain user

V1.1
- Create tags for a process-definitions
- Create microblog entries for a process-definition

V1.0
- initial commit
