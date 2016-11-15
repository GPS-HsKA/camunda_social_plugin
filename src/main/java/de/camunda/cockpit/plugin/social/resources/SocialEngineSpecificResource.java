package de.camunda.cockpit.plugin.social.resources;

import javax.ws.rs.*;
import java.sql.*;

import de.camunda.cockpit.plugin.social.dto.SocialContainerDto;
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource;
import org.joda.time.LocalDateTime;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class SocialEngineSpecificResource extends AbstractCockpitPluginResource {

	public static final String SOCIAL_TAG = "social.tag";

	public SocialEngineSpecificResource(String engineName) {
		super(engineName);
	}

	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:./camundasocial;MV_STORE=FALSE;MVCC=FALSE;DB_CLOSE_ON_EXIT=TRUE;FILE_LOCK=NO";

	//  Database credentials
	static final String USER = "sa";
	static final String PASS = "sa";

	//Database Connection
	static java.sql.Connection conn = null;
	static Statement stmt = null;

	public void getDatabaseConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}

	@GET
	@Consumes("application/json")
	@Path("/createDatabase")
	public void createNewDatabase() {
		try {
			getDatabaseConnection();
			System.out.println("Creating database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE TAG(ID bigint auto_increment PRIMARY KEY, NAME VARCHAR(255), PROC_DEF VARCHAR (255), USER VARCHAR (255))";
			String sql1 = "CREATE TABLE BLOG(ID bigint auto_increment PRIMARY KEY, CAPTION VARCHAR(255), POST VARCHAR(255), PROC_DEF VARCHAR (255), USER VARCHAR (255), TIME DATE)";
			stmt.execute(sql);
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
			String sql = "SELECT DISTINCT * FROM TAG WHERE PROC_DEF='"+processDefinitionId+"'";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setId(rs.getInt("ID"));
				dto.setTagName(rs.getString("NAME"));
				dto.setUser(rs.getString("USER"));

				dtos.add(dto);
			}

			rs.close();
			stmt.close();
			conn.close();
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
			String sql = "SELECT * FROM BLOG WHERE PROC_DEF='"+processDefinitionId+"'";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setId(rs.getInt("ID"));
				dto.setCaption(rs.getString("CAPTION"));
				dto.setTagName(rs.getString("POST"));
				dto.setUser(rs.getString("USER"));
				dto.setTime(rs.getDate("TIME"));

				dtos.add(dto);
			}

			rs.close();
			stmt.close();
			conn.close();
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
			String sql = "SELECT DISTINCT * FROM TAG";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setId(rs.getInt("ID"));
				dto.setTagName(rs.getString("NAME"));
				dto.setDefId(rs.getString("PROC_DEF"));
				dto.setUser(rs.getString("USER"));

				dtos.add(dto);
			}

			rs.close();
			stmt.close();
			conn.close();
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
			String sql = "SELECT * FROM BLOG ORDER BY TIME";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setId(rs.getInt("ID"));
				dto.setCaption(rs.getString("CAPTION"));
				dto.setPost(rs.getString("POST"));
				dto.setDefId(rs.getString("PROC_DEF"));
				dto.setUser(rs.getString("USER"));
				dto.setTime(rs.getDate("TIME"));

				dtos.add(dto);
			}

			rs.close();
			stmt.close();
			conn.close();
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
	@Path("{process-definition-id}/tags/{tagname}")
	public void setProcessDefinitionTag(@PathParam("process-definition-id") String processDefinitionId,
										@PathParam("tagname") String tagName){
		try {
			getDatabaseConnection();
			String user = getUserId();
			stmt = conn.createStatement();
			String sql = "INSERT INTO TAG VALUES(default,'"+tagName+"', '"+processDefinitionId+"','"+user+"')";
			stmt.executeUpdate(sql);
			int g = 0;
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("########## Tag angelegt #########");
	}

	//***********************************************************************************************************************
	// BlogPost für Prozessdefiniton erstellen
	//***********************************************************************************************************************

	@POST
	@Consumes("application/json")
	@Path("{process-definition-id}/blog/{caption}/{post}")
	public void setProcessDefinitionPost(@PathParam("process-definition-id") String processDefinitionId,
										 @PathParam("caption") String caption,
										 @PathParam("post") String post){
		try {
			getDatabaseConnection();
			String user = getUserId();
			stmt = conn.createStatement();
			String sql = "INSERT INTO BLOG VALUES(default,'"+post+"', '"+processDefinitionId+"','"+user+"',CURRENT_TIMESTAMP(),'"+caption+"')";
			stmt.executeUpdate(sql);
			int g = 0;
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("########## Tag angelegt #########");
	}
}


