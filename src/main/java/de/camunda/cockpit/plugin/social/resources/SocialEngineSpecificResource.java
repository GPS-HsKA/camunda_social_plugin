package de.camunda.cockpit.plugin.social.resources;

import javax.ws.rs.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

import de.camunda.cockpit.plugin.social.dto.SocialContainerDto;
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.ProcessImpl;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SocialEngineSpecificResource extends AbstractCockpitPluginResource {

	public SocialEngineSpecificResource(String engineName) {
		super(engineName);
	}

	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:./camunda-h2-dbs/camundasocial;MV_STORE=FALSE;MVCC=FALSE;DB_CLOSE_ON_EXIT=TRUE;FILE_LOCK=NO";

	//  Database credentials
	static final String USER = "sa";
	static final String PASS = "sa";

	//Database Connection
	static java.sql.Connection conn = null;
	static Statement stmt = null;

	//***********************************************************************************************************************
	// Datenbankverbindung herstellen
	//***********************************************************************************************************************


	public void getDatabaseConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}

	//***********************************************************************************************************************
	// Tabellen erstellen
	//***********************************************************************************************************************

	@GET
	@Consumes("application/json")
	@Path("/createDatabase")
	public void createNewDatabase() {
		try {
			getDatabaseConnection();
			System.out.println("Creating database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE TAG (ID bigint auto_increment PRIMARY KEY, NAME VARCHAR(255), PROC_DEF VARCHAR (255), USER VARCHAR (255))";
			String sql1 = "CREATE TABLE BLOG (ID bigint auto_increment PRIMARY KEY, CAPTION VARCHAR(255), POST VARCHAR(255), PROC_DEF VARCHAR (255), USER VARCHAR (255), TIME TIMESTAMP, TAGDELETED VARCHAR (10))";
			stmt.execute(sql);
			stmt.execute(sql1);
			System.out.println("Database created successfully...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("Goodbye!");
	}

	//***********************************************************************************************************************
	// Extension Elements auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/{process-definition-id}/{key}")
	public ArrayList<String> xmlParse (@PathParam("process-definition-id") String processDefinitionId,
									   @PathParam("key") String key) throws Exception {

		BpmnModelInstance modelInstance = getProcessEngine().getRepositoryService().getBpmnModelInstance(processDefinitionId);
		ProcessImpl process = modelInstance.getModelElementById(key);

		ArrayList<String> tags = new ArrayList<String>();
		try {
			CamundaProperties properties = process.getExtensionElements().getElementsQuery().filterByType(CamundaProperties.class).singleResult();
			if (properties != null) {
				Collection<CamundaProperty> camundaProperties = properties.getCamundaProperties();

				for (CamundaProperty property : camundaProperties) {
					if ("tag".equals(property.getCamundaName())) {
						tags.add(property.getCamundaValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tags;
	}

	//***********************************************************************************************************************
	// Tag zu ProzessDefiniton auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/{process-definition-id}/tags")
	public ArrayList<SocialContainerDto> getTagsFromProcessId(@PathParam("process-definition-id") String processDefinitionId) throws SQLException, ClassNotFoundException {
		ArrayList<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT NAME FROM TAG WHERE PROC_DEF='"+processDefinitionId+"'";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setTagName(rs.getString("NAME"));

				dtos.add(dto);
			}

			rs.close();
			//stmt.close();
			//conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
	}

	//***********************************************************************************************************************
	// User zu ProzessDefiniton auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/{process-definition-id}/users")
	public ArrayList<SocialContainerDto> getUsersFromProcessId(@PathParam("process-definition-id") String processDefinitionId) throws SQLException, ClassNotFoundException {
		ArrayList<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT USER FROM TAG WHERE PROC_DEF='"+processDefinitionId+"'";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setUser(rs.getString("USER"));

				dtos.add(dto);
			}

			rs.close();
			//stmt.close();
			//conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
	}

	//***********************************************************************************************************************
	// BlogPost zu ProzessDefiniton auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/{process-definition-id}/blog")
	public ArrayList<SocialContainerDto> getPostsFromProcessId(@PathParam("process-definition-id") String processDefinitionId) throws SQLException, ClassNotFoundException {
		ArrayList<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM BLOG WHERE PROC_DEF='"+processDefinitionId+"' ORDER BY TIME DESC";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setId(rs.getInt("ID"));
				dto.setCaption(rs.getString("CAPTION"));
				dto.setTagName(rs.getString("POST"));
				dto.setTagStatus(rs.getString("TAGDELETED"));
				dto.setUser(rs.getString("USER"));
				dto.setTime(rs.getDate("TIME"));

				dtos.add(dto);
			}

			rs.close();
			//stmt.close();
			//conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
	}

	//***********************************************************************************************************************
	// Prozessdefinitionen zu Tag auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/{tag}/processdefinitions")
	public ArrayList<SocialContainerDto> getProcessDefsFromTag(@PathParam("tag") String tag) throws SQLException, ClassNotFoundException {
		ArrayList<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "SELECT PROC_DEF FROM TAG WHERE NAME='"+tag+"'";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setDefId(rs.getString("PROC_DEF"));

				dtos.add(dto);
			}

			rs.close();
			//stmt.close();
			//conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
	}

	//***********************************************************************************************************************
	// Alle Tags auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/tags")
	public ArrayList<SocialContainerDto> getAllTags() throws SQLException, ClassNotFoundException {
		ArrayList<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT NAME FROM TAG";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setTagName(rs.getString("NAME"));

				dtos.add(dto);
			}

			rs.close();
			//stmt.close();
			//conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
	}

	//***********************************************************************************************************************
	// Alle Tags eines Users auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/tags/{user}")
	public ArrayList<SocialContainerDto> getAllTagsFromUser(@PathParam("user") String user) throws SQLException, ClassNotFoundException {
		ArrayList<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT NAME, PROC_DEF FROM TAG WHERE USER='"+user+"'";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setTagName(rs.getString("NAME"));
				dto.setDefId(rs.getString("PROC_DEF"));

				dtos.add(dto);
			}

			rs.close();
			//stmt.close();
			//conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
	}

	//***********************************************************************************************************************
	// Alle User auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/users")
	public ArrayList<SocialContainerDto> getAllUsers() throws SQLException, ClassNotFoundException {
		ArrayList<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT USER FROM TAG";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setUser(rs.getString("USER"));

				dtos.add(dto);
			}

			rs.close();
			//stmt.close();
			//conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
	}

	//***********************************************************************************************************************
	// Alle Blogeinträge auslesen
	//***********************************************************************************************************************

	@GET
	@Produces("application/json")
	@Path("/blog")
	public ArrayList<SocialContainerDto> getAllPosts() throws SQLException, ClassNotFoundException {
		ArrayList<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM BLOG ORDER BY TIME DESC";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setId(rs.getInt("ID"));
				dto.setCaption(rs.getString("CAPTION"));
				dto.setPost(rs.getString("POST"));
				dto.setTagStatus(rs.getString("TAGDELETED"));
				dto.setDefId(rs.getString("PROC_DEF"));
				dto.setUser(rs.getString("USER"));
				dto.setTime(rs.getDate("TIME"));

				dtos.add(dto);
			}

			rs.close();
			//stmt.close();
			//conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
	}

	//***********************************************************************************************************************
	// Aktive UserId auslesen
	//***********************************************************************************************************************

	public String getUserId() throws SQLException, ClassNotFoundException {
		String user = null;
		try {
			user = getProcessEngine().getIdentityService().getCurrentAuthentication().getUserId();
		} catch (Exception e) {
			System.out.println(e);
		}
		return user;
	}


	//***********************************************************************************************************************
	// POST
	//***********************************************************************************************************************

	//***********************************************************************************************************************
	// Tag für Prozessdefiniton erstellen
	//***********************************************************************************************************************

	@POST
	@Consumes("application/json")
	@Path("{process-definition-id}/tags/{key}/{tagname}")
	public void setProcessDefinitionTag(@PathParam("process-definition-id") String processDefinitionId,
										@PathParam("key") String key,
										@PathParam("tagname") String tagName){
		try {
			getDatabaseConnection();
			String user = getUserId();
			stmt = conn.createStatement();
			String sql = "INSERT INTO TAG VALUES(default,'"+tagName+"', '"+processDefinitionId+"','"+user+"')";
			stmt.executeUpdate(sql);
			//stmt.close();
			conn.close();
			// XML - Extension
			createExtensionTag(processDefinitionId, key, tagName);

		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("########## Tag angelegt #########");
	}

	//***********************************************************************************************************************
	// Tag für Prozessdefiniton löschen
	//***********************************************************************************************************************

	@DELETE
	@Consumes("application/json")
	@Path("{process-definition-id}/tags/{tagname}")
	public void deleteProcessDefinitionTag(@PathParam("process-definition-id") String processDefinitionId,
										   @PathParam("tagname") String tagName){
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "DELETE FROM TAG WHERE NAME = '"+tagName+"' AND PROC_DEF = '"+processDefinitionId+"'";
			stmt.executeUpdate(sql);
			//stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("########## Tag gelöscht #########");
	}

	//***********************************************************************************************************************
	// BlogPost für Prozessdefiniton erstellen
	//***********************************************************************************************************************

	@POST
	@Consumes("application/json")
	@Path("{process-definition-id}/blog/{caption}/{post}/{TagDeleted}")
	public void setProcessDefinitionPost(@PathParam("process-definition-id") String processDefinitionId,
										 @PathParam("caption") String caption,
										 @PathParam("post") String post,
										 @PathParam("TagDeleted") String tagDeleted){
		try {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			getDatabaseConnection();
			String user = getUserId();
			stmt = conn.createStatement();
			String sql = "INSERT INTO BLOG VALUES(default, '"+caption+"', '"+post+"', '"+processDefinitionId+"','"+user+"','"+timestamp+"', '"+tagDeleted+"')";
			stmt.executeUpdate(sql);
			//stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("########## Blogpost angelegt #########");
	}

	//***********************************************************************************************************************
	// Deployment Prozess für neue Tag-Version
	//***********************************************************************************************************************

	public ProcessDefinition deploy(String deploymentName, File bpmnFile) throws FileNotFoundException {
		DeploymentBuilder deploymentBuilder = getProcessEngine().getRepositoryService().createDeployment();
		deploymentBuilder.addInputStream(bpmnFile.getName(), new FileInputStream(bpmnFile));
		deploymentBuilder.name(deploymentName);
		Deployment deployment = deploymentBuilder.deploy();

		ProcessDefinition processDefinition = getProcessEngine().getRepositoryService().createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		return processDefinition;
	}

	//***********************************************************************************************************************
	// Camunda Extension Element für Tag erstellen
	//***********************************************************************************************************************

	public void createExtensionTag(String processDefinitionId, String key, String tagName) throws IOException {

		BpmnModelInstance modelInstance = getProcessEngine().getRepositoryService().getBpmnModelInstance(processDefinitionId);
		ProcessImpl process = modelInstance.getModelElementById(key);

		ExtensionElements extensionElements = process.getExtensionElements();

		if (extensionElements == null) {
			extensionElements = modelInstance.newInstance(ExtensionElements.class);
			process.setExtensionElements(extensionElements);

			CamundaProperties camundaProperties = extensionElements.addExtensionElement(CamundaProperties.class);

			CamundaProperty camundaProperty = modelInstance.newInstance(CamundaProperty.class);
			camundaProperty.setCamundaName("tag");
			camundaProperty.setCamundaValue(tagName);

			camundaProperties.addChildElement(camundaProperty);
		} else {
			CamundaProperty camundaProperty = modelInstance.newInstance(CamundaProperty.class);
			camundaProperty.setCamundaName("tag");
			camundaProperty.setCamundaValue(tagName);

			CamundaProperties camundaProperties = extensionElements.getElementsQuery().filterByType(CamundaProperties.class).singleResult();
			camundaProperties.addChildElement(camundaProperty);
		}

		File file = File.createTempFile("bpmn-model-api-", ".bpmn");
		Bpmn.writeModelToFile(file, modelInstance);

		deploy(key, file);
	}

}


