<%@ page import="pkg.*"%>
<%@ page import="java.util.*, java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet" %>
<jsp:include page="header.jsp"/>

<% // if CusRep session is not set - user havent logined as cusRep, redirect to index.jsp
if((request.getSession(false).getAttribute("CusRep")== null) )
{
%>
<jsp:forward page="/"></jsp:forward>
<%}%>

<script>
		const cusRepScript = document.createElement('script');
		cusRepScript.src = 'js/cusRep.js';
		cusRepScript.defer = true;
		document.head.append(cusRepScript);
</script>

<!-- This web page implements Customer Representative functionalities -->
<div class = "container-fluid" id = "cusRep">

	<div class = "row">
	
			<div class = "col-sm-12 col-md-2 vertical-nav-con" id = "cusRepNav">
				<ul class="vertical-nav" data-active="#cusRepRes">
					<li class="vertical-nav-item"><a href="index.jsp" class="toggle-btn btn btn-outline-light">Home</a></li>
					<li class="vertical-nav-item"><a href="#cusRepRes" class="toggle-btn btn btn-outline-light">Manage Reservations</a></li>
					<li class="vertical-nav-item"><a href="#cusRepSche" class="toggle-btn btn btn-outline-light">List of Schedules</a></li>
					<li class="vertical-nav-item"><a href="#cusRepCus" class="toggle-btn btn btn-outline-light">List of Customers</a></li>
				</ul>
			</div>
			
			<div class = "col-sm-12 col-md-10" id = "cusRepContent">
				
				<div class="container" id="cusRepRes" aria-hidden = "false">
				<br><br>
				
				<!-- Nav Links -->
					<ul class="nav nav-tabs">
						<li class="nav-item">
							<p class="nav-link" data-type="editRes">Edit A Reservation</p>
						</li>
						<li class="nav-item">
							<p class="nav-link" data-type="newRes">Make A New Reservation</p>
						</li>
						<li class="nav-item">
							<p class="nav-link" data-type="delRes">Delete A Reservation</p>
						</li>
					</ul>
					
				<!-- Content -->
					<div class="cusRep-contentBox">
					
						<!--
						
						 Edit Reservation 
						 
						 -->
						<div id="editRes" aria-hidden="false">
							<p>To edit a reservation, input the reservation number (rid) of the reservation first. </p>
								<!-- Edit Reservation Form -->
								<form action="cusRep.jsp" id="getRidForm">
									<div class="form-group" style="margin-top: 1em;">
									<label for="cusRep-res-edit-rid">Reservation ID (rid)</label>
									<input type="number" class="form-control" id="cusRep-res-edit-rid" name="rid">
									</div>
									<button id="cusRep-edit-getRes" class="btn btn-info">Get Reservation Data</button>
								</form>
								
								<%
									if( request.getParameter("rid") != null ) {
										ApplicationDB app = new ApplicationDB();
										Connection con = app.getConnection();
										int rid = Integer.parseInt(request.getParameter("rid"));
										String SQL_Select_Res = "SELECT * FROM Reservations WHERE rid = ?";
										PreparedStatement ps = con.prepareStatement(SQL_Select_Res);
										ps.setInt(1, rid);
										ResultSet rs = ps.executeQuery();
								%>
								
								<% if (rs.next()) { %>
								<div id="resEditForm">
								Please fill in one or more fields that you wish to edit
								<form action="cusRep_reservations" method="post" >
											 <input type="hidden" name="rid" value="<%= rid %>">
											 <input type="hidden" name="type" value="edit">
											  <div class="form-group">
											    <label for="cusRep-res-edit-originStop">Origin Stop</label>
											    <input type="text" class="form-control" id="cusRep-res-edit-originStop" name="origin-stop" value="<%= rs.getString("originStop_id") %>">
											  </div>
											  <div class="form-group">
											    <label for="cusRep-res-edit-desStop">Destination Stop</label>
											    <input type="text" class="form-control" id="cusRep-res-edit-desStop" name="des-stop" value="<%= rs.getString("destinationStop_id") %>">
											  </div>
											  <div class="form-group">
											   <label for="cusRep-res-class">Class</label>
											   <select id = "cusRep-res-class" name = "class">
											   		<% String[] classes = new String[] {"economy", "first", "business"};
											   			String currClass = rs.getString("class");
											   		%>
											   		<option value=" <%= currClass %> "> <%= currClass %> </option>
											   		<% for (String c : classes) {
											   				
											   				if (!c.equals(currClass)){
											   					%>
											   				<option value=" <%= c %> "> <%= c %> </option>	
											   	
											   		<% } } %>
											   </select>
											  </div>
											  <div class="form-group">
											   <label for="cusRep-res-totalDiscount">Total Discount ($)</label>
											   <input type="text" class="form-control" id="cusRep-res-totalDiscount" name="total-discount" value="<%= rs.getString("total_discount") %>" pattern="[0-9]+(\.[0-9]{1,2})?">
											  </div>
											  <div class="form-group">
											   <label for="cusRep-res-fee">Booking Fee ($)</label>
											   <input type="text" class="form-control" id="cusRep-res-fee" name="fee" value="<%=rs.getString("bookingFee") %>" pattern="[0-9]+(\.[0-9]{1,2})?">
											  </div>
											  <div class="form-group">
											   <label for="cusRep-res-totalFare">Total Fare ($)</label>
											   <input type="text" class="form-control" id="cusRep-res-totalFare" name="total-fare" value="<%=rs.getString("totalFare")%>" pattern="[0-9]+(\.[0-9]{1,2})?">
											  </div>
											  <div class="form-group">
											   <label for="cusRep-res-travelTime">Travel Time (minutes)</label>
											   <input type="number" class="form-control" id="cusRep-res-travelTime" name="travel-time" value="<%=rs.getInt("travelTime") %>" pattern="[0-9]+(\.[0-9]{1,2})?">
											  </div>
											  <div class="form-group">
											   <label for="cusRep-res-trip">Trip Type</label>
											   <select id = "cusRep-res-trip" name = "trip">
											  		<% String[] tripTypes = new String[] {"One-Way", "Round-Trip", "Weekly", "Monthly"};
											   			String currTripType = "One-Way";
											   			
											   			if(rs.getInt("isWeekly") == 1){
											   				currTripType = "Weekly";
											   			} else if (rs.getInt("isMonthly") == 1)
											   				currTripType = "Monthly";
											   			else if (rs.getInt("is_round_trip") == 1)
											   				currTripType = "Round-Trip";
											   		%>
											   		<option value=" <%= currTripType %> "> <%= currTripType %> </option>
											   		<% for (String t : tripTypes) {
											   				
											   				if (!t.equals(currTripType)){
											   					%>
											   				<option value=" <%= t %> "> <%= t %> </option>	
											   	
											   		<% } } %>
											   </select>
											  </div>
											  
											  <button type="submit" class="btn btn-primary">Edit</button>
							</form> 
							</div>
							<% } 
								else 
								{%>
								<p class="error">Cannot find the reservation with number <%= rid %>.</p>
								
								
							<%	}
										
							}%>
								
							<!-- End Edit Reservation Form -->
						</div>
						
						<!--
						
						 New Reservation 
						 
						 -->
						<div id="newRes" aria-hidden="true">
							
							<div class="container" style="margin-top: 1em;">
								<p style="color: red;">If the customer has an account, make a reservation with the customer's user name.</p>
								<form action="cusRep_reservations" method="post">
									<input type="hidden" name="type" value="addWithUserName">
									<div class="form-group">
										<label for="cusRep-res-add-uname">Customer's User Name</label>
										<input id="cusRep-res-add-uname" type="text" name="uname">
									</div>
									<button type="submit" class="btn btn-primary">Select A Schedule</button>
								</form>
							</div>
							
							<div class="container" style="margin-top: 20px;">
								<p style="color: red;">If the customer does not have an account, make a reservation with the customer's first name and last name, under the customer representative's account.</p>
								<form action="cusRep_reservations" method="post">
									<input type="hidden" name="type" value="addWithFirstName">
									<div class="form-group">
										<label for="cusRep-res-add-fname">Customer's First Name</label>
										<input id="cusRep-res-add-fname" type="text" name="fname">
									</div>
									<div class="form-group">
										<label for="cusRep-res-add-lname">Customer's Last Name</label>
										<input id="cusRep-res-add-lname" type="text" name="lname">
									</div>
									<button type="submit" class="btn btn-primary">Select A Schedule</button>
								</form>
							</div>
							
						</div>
						
						
						
						<!--
						
						 Delete Reservation 
						 
						 -->
						<div id="delRes" aria-hidden="true">
							<!-- Delete Reservation Form -->
					    			<form action="cusRep_reservations" method="post">
					    				  <input type="hidden" name="type" value="delete">
										  <div class="form-group">
										    <label for="cusRep-res-del-rid">Reservation ID (rid) </label>
										    <input type="number" class="form-control" id="cusRep-res-del-rid" name="rid">
										  </div>
										  <button type="submit" class="btn btn-danger">Delete</button>
									</form>
						<!-- End Delete Reservation Form -->
						</div>
					
					</div>
					
				</div>
				
				<div class="container" id="cusRepSche" aria-hidden = "true">
				<br><br>
				
					<div class="container">
						<div class="container">
							<form action="/CS336_g9/Schedule?line=all" method="get">
								<input type="hidden" name="line" value="all">
								<h5>Produces a list of train schedules for a specific origin and destination:</h5>
								<button type="submit" class="btn btn-info">Select origin and destination</button>
							</form>
						</div>
						<div class="container">
							<h5>Produces a list of train schedules for a given station:</h5>
							<form action="cusRep_reservations" method="post">
								<input type="hidden" name="type" name="schedule">
								<div class="form-group">
								<label for="cusRep-sche-station">Station Name</label>
								<select id="cusRep-sche-station" name="station">
								</select>
								</div>
								<button type="submit" class="btn btn-info">Get Schedule</button>
							</form>
						</div>
					</div>
					
					
				</div>
				
				
				<!--
						
						 Produce a list of all customers who have seats reserves on a given transit line and train number
						 
				-->
				<div class="container" id="cusRepCus" aria-hidden = "true">
				<br><br>
					<div class="container">
						<h5> Produce a list of all customers who have seats reserved on a given transit line and train number: </h5>
						<% 
							Common lib = new Common();
							ArrayList<String> trans = lib.get_transit_line_names();
						%>
						
						<form action="cusRep_reservations" method="get">
							<input type="hidden" name="type" value="customers">
							<div class="form-group">
							<label for="cusRep-cus-trans">Transit Line Name</label>
							<select id="cusRep-cus-trans" name="tran">
						
						<% 	for(String tran : trans){%>	
							<option value="<%= tran %>"><%= tran %></option>		
						<%	
						}
						%>
							</select>
							</div>
							<div class="form-group">
							<label for="cusRep-cus-train">Train Number</label>
							<select id="cusRep-cus-train" name="train">
								<%
									ArrayList<Integer> trains = lib.getTrainNumbers();
									for (Integer train : trains) {
								%>
									<option value="<%= train %>"> <%=train%> </option>		
							<% }%>
							</select>
							</div>
							<button type="submit" class="btn btn-info">Submit</button>
						</form>
						
					</div>
			</div>
			
	</div>

</div>
</div>



<jsp:include page="footer.jsp"/>