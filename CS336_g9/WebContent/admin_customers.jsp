<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
 <%
				List<String> list = new ArrayList<String>();
		
				try {
					
					ApplicationDB db = new ApplicationDB();	
					Connection con = db.getConnection();	
					
					//Create a SQL statement
					Statement stmt = con.createStatement();
						String str = "SELECT * FROM Customers";
						//Run the query against the database.
						ResultSet result = stmt.executeQuery(str);
		%>
	<!-- Make an HTML table to show the results: -->
		<table class="table table-striped">
			  <thead class="thead-dark">
			    <tr>
			      <th scope="col">User Name</th>
			      <th scope="col">First Name</th>
			      <th scope="col">Last Name</th>
			      <th scope="col">Address</th>
			      <th scope="col">City</th>
			      <th scope="col">State</th>
			      <th scope="col">Zipcode</th>
			      <th scope="col">Email</th>
			      <th scope="col">Telephone</th>
			      <th scope="col">DOB</th>
			    </tr>
			  </thead>
			  <tbody>

			<%
					//parse out the results
					while (result.next()) {
			
						out.print("<tr>");
						
						out.print("<td>");
						out.print(result.getString("userName"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("firstName"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("lastName"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("address"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("city"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("state"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("zipcode"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("email"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("telephone"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("DOB"));
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
 