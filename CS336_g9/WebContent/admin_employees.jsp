<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

		<%
				List<String> list = new ArrayList<String>();
		
				try {
		
					//Get the database connection
					ApplicationDB db = new ApplicationDB();	
					Connection con = db.getConnection();	
					
					//Create a SQL statement
					Statement stmt = con.createStatement();
					//Make a SELECT query from the sells table with the price range specified by the 'price' parameter at the index.jsp
					String str = "SELECT * FROM Employees";
					//Run the query against the database.
					ResultSet result = stmt.executeQuery(str);
		%>
	<!-- Make an HTML table to show the results: -->
		<table class="table table-striped">
			  <thead class="thead-dark">
			    <tr>
			      <th scope="col">SSN</th>
			      <th scope="col">First Name</th>
			      <th scope="col">Last Name</th>
			      <th scope="col">User Name</th>
			      <th scope="col">Password</th>
			    </tr>
			  </thead>
			  <tbody>

			<%
					//parse out the results
					while (result.next()) {
			
						out.print("<tr>");
						
						out.print("<td>");
						out.print(result.getString("ssn"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("firstName"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("lastName"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("userName"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("password"));
						out.print("</td>");
						
						out.print("</tr>");
		
					}
					//close the connection.
					con.close();
		
				} catch (Exception e) {
				}
			%>
		</tbody>
		</table>