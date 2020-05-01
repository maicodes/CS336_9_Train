<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<jsp:include page="header.jsp"/>

<% 
Object isManager = request.getSession(false).getAttribute("Manager");
Object isCR = request.getSession(false).getAttribute("CusRep");
if (isManager == null && isCR == null)
{
%>
<jsp:forward page="/"></jsp:forward>
<%} %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>
	<form action="index.jsp" method="post">
	<button type="submit" class="auth-btn btn btn-primary">back</button>
	</form>
	<form action="browseCusRep.jsp" method="get">
	  <input type="text" name="search" size="50">
	  <button type="submit" class="auth-btn btn btn-primary">search</button>
	</form>
</body>
</html>

		<%
				String s = request.getParameter("search");
				List<String> list = new ArrayList<String>();
		
				try {
		
					//Get the database connection
					ApplicationDB db = new ApplicationDB();	
					Connection con = db.getConnection();	
					ResultSet result;
					if (s != null){
						PreparedStatement sResult = con.prepareStatement("SELECT * FROM Messages WHERE content LIKE ? OR username LIKE ? OR title LIKE ?;");
						String temp = "%" + s + "%";
						sResult.setString(1, temp);
						sResult.setString(2, temp);
						sResult.setString(3, temp);
						System.out.println(sResult);
						result = sResult.executeQuery();	
					}
					
					else{
						//Create a SQL statement
						Statement stmt = con.createStatement();
						//Make a SELECT query from the sells table with the price range specified by the 'price' parameter at the index.jsp
						String str = "SELECT * FROM Messages";
						//Run the query against the database.
						result = stmt.executeQuery(str);	
					}	
					
		%>
	<!-- Make an HTML table to show the results: -->
		<table class="table table-striped">
			  <thead class="thead-dark">
			    <tr>
			      <th scope="col">Question ID</th>
			      <th scope="col">Username</th>
			      <th scope="col">Title</th>
			      <th scope="col">Body</th>
			      <th scope="col">Response</th>
			      <th scope="col">Submit response</th>
			    </tr>
			  </thead>
			  <tbody>

			<%
					//parse out the results
					while (result.next()) {
			
						out.print("<tr>");
						out.print("<form action=\"respondMessage\" method=\"post\">");
						out.print(String.format("<input type=\"hidden\" name=\"qid\" value=\"%s\">", result.getString("qid")));
						out.print("<td>");
						out.print(result.getString("qid"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("username"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("title"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("content"));
						out.print("</td>");
						
						out.print("<td>");
						out.print(result.getString("response"));
						out.print("</td>");
						
						out.print("<td>");
						out.print("<input type=\"text\" name=\"response\" size=\"50\">");
						out.print("<button type=\"submit\" class=\"auth-btn btn btn-primary\">Submit</button>");
						out.print("</form>");
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