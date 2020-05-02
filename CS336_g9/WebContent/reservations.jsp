<%@ page import="pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ page import= "pkg.Models.*" %>

<jsp:include page = "header.jsp"></jsp:include>
<jsp:include page = "navigation.jsp"></jsp:include>

<% // if Manager session is not set - user havent logined as admin, redirect to index.jsp
if((request.getSession(false).getAttribute("Customer")== null) )
{
%>
<jsp:forward page="/"></jsp:forward>
<%} %>

<%
if (session.getAttribute("resCancelMessage") != null)
{
	String mess = (String) session.getAttribute("resCancelMessage");
%>
<div class = "info box"> <%= mess %> </div>

<% } %>

<%
	Common lib = new Common();
  // Get a list of all reservations
  	ApplicationDB app = new ApplicationDB();
  	Connection con = app.getConnection();
  	if(con == null) {
		System.out.print("Cannot connect to database");
		return;
	}
  	
  	String userName = (String) session.getAttribute("Customer");
  	
  	String SQL_allReservations = "SELECT t3.rid \"Reservation Number\",  t3.originStop_id \"Origin Stop ID\", t3.station_id \"Origin Station ID\", t3.arrive_time \"Departure Time\", t3.train_id \"Departure Train Number\", " + 
  			"	t4.destinationStop_id \"Destination Stop ID\", t4.station_id \"Destination Station ID\", t4.arrive_time \"Arrival Time\", t4.train_id \"Arrival Train Number\" , " + 
  			"	travelTime \"Travel Time\", totalFare \"Total Price\", t3.date \"Booking Date\" " + 
  			"FROM (SELECT r.rid, originStop_id, station_id, arrive_time, train_id, travelTime, totalFare, t1.date " + 
  			"FROM (SELECT * " + 
  			"FROM Bookings " + 
  			"WHERE userName = ?) t1, " + 
  			"Stops s, " + 
  			"Reservations r " + 
  			"WHERE r.rid = t1.rid AND originStop_id = s.stop_id) t3 " + 
  			"JOIN " + 
  			"(SELECT r.rid, destinationStop_id, station_id, arrive_time, train_id " + 
  			"FROM (SELECT * " + 
  			"FROM Bookings " + 
  			"WHERE userName = ?) t1, " + 
  			"Stops s, " + 
  			"Reservations r " + 
  			"WHERE r.rid = t1.rid AND destinationStop_id = s.stop_id) t4 " + 
  			"ON t3.rid = t4.rid";
  	
  	try {
  		PreparedStatement ps = con.prepareStatement(SQL_allReservations);
		ps.setString(1, userName);
		ps.setString(2, userName);
		
		ResultSet rs = ps.executeQuery();
		
		// Get date from Bookings 
		String SQL_dates = "SELECT rid, date FROM Bookings WHERE userName = ?";
		PreparedStatement ps2 = con.prepareStatement(SQL_dates);
		ps2.setString(1, userName);
		
		ResultSet rs2 = ps2.executeQuery();
	%> 
	<div class="container mt-5">
		<h3>List of all reservations:</h3>
		<table class="table table-striped">
			  <thead class="thead-dark">
			    <tr>
			      <th scope="col">Reservation Number</th>
			      <th scope="col">Origin Stop ID</th>
			      <th scope="col">Origin Station ID</th>
			      <th scope="col">Departure Time</th>
			      <th scope="col">Departure Train Number</th>
			      <th scope="col">Destination Stop ID</th>
			      <th scope="col">Destination Station ID</th>
			      <th scope="col">Arrival Time</th>
			      <th scope="col">Arrival Train Number</th>
			      <th scope="col">Travel Time (minutes)</th>
			      <th scope="col">Total Price ($)</th>
			      <th scope="col">Booking Date</th>
			    </tr>
			  </thead>
			  <tbody>
		<%
			while (rs.next()){
				rs2.next();
				
				out.print("<tr>");
				
				String rid = rs.getString("Reservation Number");
				String departTime = rs.getString("Departure Time");
				String rid2 = rs2.getString("rid");
				String departTime2 = rs2.getString("date");
			
				if (rid.equals(rid2)){
					String time1 = lib.stripTime(departTime);
					String time2 = lib.stripTime(departTime2);
					if(!time1.equals(time2)){
						out.print("<div class='error box'> Your reservation " + rid + " has changed the departure time from " + time2 + " to " + time1 + "</div>");
						%>
						<!--
							<input type="hidden" id = "resRid001" value="<%= rid %>">
							<input type="hidden" id = "resTime1" value="<%= time1 %>">
							<input type="hidden" id = "resTime2" value="<%= time2 %>">
							<script>
								const rid = document.querySelector("#resRid001");
								const time1 = document.querySelector("#resTime1");
								const time2 = document.querySelector("#resTime2");
								alert("Your reservation " + rid.value + " has changed the departure time from " + time2.value + " to " + time1);
							</script>
						  -->
							
						<% 
					}
				}
				
				out.print("<td>");
				out.print(rid);
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Origin Stop ID"));
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Origin Station ID"));
				out.print("</td>");
				
				
				out.print("<td>");
				out.print(departTime);
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Departure Train Number"));
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Destination Stop ID"));
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Destination Station ID"));
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Arrival Time"));
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Arrival Train Number"));
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Travel Time"));
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Total Price"));
				out.print("</td>");
				
				out.print("<td>");
				out.print(rs.getString("Booking Date"));
				out.print("</td>");
				
				out.print("</tr>");

			}
		%>
		</tbody>
		</table>  
	</div>
	<%
		
  	} catch (Exception e) {
  		e.printStackTrace();
  	}
  	app.closeConnection(con);
%>

	<div class="container">
		<h3>Cancel a reservation</h3>
		<form action="Reservation" method="post">
			<input type="hidden" name="type" value="cancel">
			<div class="form-group">
				<label for="res-num">Reservation Number:</label>
				<input type="number" class="form-control" id="res-num" name="res-num">
			</div> 
			 <button type="submit" class="btn btn-danger">Cancel Reservation</button>
		</form>
	</div>

<jsp:include page="footer.jsp"></jsp:include>