package de.camunda.cockpit.plugin.social.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.TypeConstraintException;
import java.sql.*;

import de.camunda.cockpit.plugin.social.dto.SocialContainerDto;
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SocialEngineSpecificResource extends AbstractCockpitPluginResource {

	public static final String SOCIAL_TAG = "social.tag";

	public SocialEngineSpecificResource(String engineName) {
		super(engineName);
	}

	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:./camundasocial;MV_STORE=FALSE;MVCC=FALSE";

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
	@Path("{process-definition-key}/tags")
	public void createNewDatabase(@PathParam("process-definition-key") String processDefinitonId, String[] args) {
		try {
			getDatabaseConnection();
			System.out.println("Creating database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE TEST6(ID bigint auto_increment PRIMARY KEY, NAME VARCHAR(255))";
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

	@GET
	@Consumes("application/json")
	@Path("process-definition/{id}/tags")
	public String getTagsFromProcessDefiniton(@PathParam("id") String processDefinitonId) {
		ResultSet tags = null;
		try {
			getDatabaseConnection();
			System.out.println("Creating query...");
			stmt = conn.createStatement();
			String sql = "SELECT NAME FROM TEST5 WHERE ID=1;";
			tags = stmt.executeQuery(sql);
			System.out.println("Query successfully...");
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
		String result = tags.toString();
		return result;
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
			String sql = "SELECT * FROM TEST5";
			ResultSet rs = stmt.executeQuery(sql);
			int g = 0;

			while (rs.next()) {

				SocialContainerDto dto = new SocialContainerDto();
				dto.setId(rs.getInt("id"));
				dto.setTag(rs.getString("name"));

				dtos.add(dto);
			}

			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtos;
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
	public void setProcessDefinitionTag(@PathParam("process-definition-id") String processDefinitionId, @PathParam("tagname") String tagName) {
		try {
			getDatabaseConnection();
			stmt = conn.createStatement();
			String sql = "INSERT INTO TEST6 VALUES(default,'"+tagName+"')";
			stmt.executeUpdate(sql);
			int g = 0;
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("########## Tag angelegt #########");
	}
}


