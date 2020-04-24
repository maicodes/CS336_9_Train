<%@ page import = "pkg.Models.Fare" %>
<%@ page import = "pkg.Models.StationTrain" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "pkg.Common" %>
<jsp:include page = "header.jsp"></jsp:include>
<jsp:include page = "navigation.jsp"></jsp:include>
	<%
		String origin = (String) request.getAttribute("origin");
		String destination = (String) request.getAttribute("destination");
		Integer distance = (Integer) request.getAttribute("distance");
		Fare pricing = (Fare) request.getAttribute("pricing");
		
		Common lib = new Common();
	%>
	<div class = "container">
		<div class = "row">
			<h1>Ticket from <%= origin %> to <%= destination %></h1>
		</div>
		<div class = "row">
			<a href = "Schedule?origin=<%= origin.replaceAll("\\s","") %>">Change Destination</a>
		</div>
	</div>
	<hr />
	<div class = "container">
		<div class = "row">
			<div class = "col">
				<div class = "container">
					<h2>Options</h2>
					<hr />
					<form action = "Reservation" method = "post" name = "createTicket" id = "createTicket">
						<input type = "hidden" name = "line" value = "<%= pricing.getLine() %>">
						<input type = "hidden" name = "origin" value = "<%= origin %>">
						<input type = "hidden" name = "destination" value = "<%= destination %>">
						<input type = "hidden" name = "distance" value = "<%= distance %>">
						<input type = "hidden" name = "action" value = "checkout">
						<div class = "row">
							<div class = "col">
								<h4>Select Date</h4>
								<table class = "table">
									<thead>
									</thead>
									<tbody>
										<tr>
											<td>
												<label for = "day">Travel Date</label>
											</td>
											<td>
											<select name = "day">
												<%
													ArrayList<String> dates = lib.getWeek();
													for (String d : dates) {
												%>
													<option value = "<%=d %>"><%=d %></option>
												<%
													}
												%>
											</select>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class = "col">
								<h4>Select Travel Time</h4>
								<table class = "table">
									<thead></thead>
									<tbody>
										<tr>
											<td><label for = "time">Time:</label></td>
											<td>
												<select id = "time" name = "time" onchange = "getTrain()">
													<% ArrayList<StationTrain> forward = StationTrain.list(origin, destination); %>
													<%
														for (StationTrain st : forward) {
													%>
														<option value = "<% out.print(st.getId() + " " + st.getDep() + " " + st.getArr()); %>">
														<% out.print(st.getDep() + " - " + st.getArr()); %>
														</option>
													<%
														}
													%>
												</select>
											</td>
										</tr>
										<tr>
											<td>Train</td>
											<td><span id = "trainID"></span>
											<input type = "hidden" name = "trainidvalue" id = "trainidvalue" value = "Any">
											<input type = "hidden" name = "departure" id = "departure" value = "">
											<input type = "hidden" name = "arrival" id = "arrival" value = "">
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class = "row">
							<div class = "col">
								<h4>Select Class</h4>
								<table class = "table">
									<thead></thead>
									<tbody>
										<tr>
											<td><label for = "class">First Class</label></td>
											<td><input type = "radio" name = "class" value = "first"></td>
										</tr>
										<tr>
											<td><label for = "class">Business</label></td>
											<td><input type = "radio" name = "class" value = "business"></td>
										</tr>
										<tr>
											<td><label for = "class">Economy</label></td>
											<td><input type = "radio" name = "class" value = "economy" checked></td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<div class = "col">
								<h4>Select Trip Type</h4>
								<table class = "table">
									<thead></thead>
									<tbody>
										<tr>
											<td><label for = "type">One Way</label></td>
											<td><input type = "radio" name = "type" value = "oneway" checked></td>
										</tr>
										<tr>
											<td><label for = "type">Round Trip</label></td>
											<td><input type = "radio" name = "type" value = "roundtrip"></td>
										</tr>
										<tr>
											<td><label for = "type">Weekly</label></td>
											<td><input type = "radio" name = "type" value = "weekly"></td>
										</tr>
										<tr>
											<td><label for = "type">Monthly</label></td>
											<td><input type = "radio" name = "type" value = "monthly"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class = "row">
							<div class = "col">
								<h4>Age Group</h4>
								<table class = "table">
									<thead>
									</thead>
									<tbody>
										<tr>
											<td><label for = "agegroup">Child</label></td>
											<td><input type = "radio" name = "agegroup" value = "child"></td>
										</tr>
										<tr>
											<td><label for = "agegroup">Senior</label></td>
											<td><input type = "radio" name = "agegroup" value = "senior"></td>
										</tr>
										<tr>
											<td><label for = "agegroup">Regular</label></td>
											<td><input type = "radio" name = "agegroup" value = "regular" checked></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class = "col">
								<h4>Disability</h4>
								<table class = "table">
									<thead></thead>
									<tbody>
										<tr>
											<td><label for = "disabled">Disabled</label></td>
											<td><input type = "checkbox" name = "disabled" value = "disabled"></td>
										</tr>
									</tbody>
								</table>
								<h4></h4>
							</div>
						</div>
						<hr />
						<div class = "row">
							<div class = "col">
								<button class = "btn btn-primary" style = "float: right" id = "checkout">Checkout</button>
							</div>
						</div>
						<div id="myModal" class="modal fade">
						    <div class="modal-dialog">
						        <div class="modal-content">
						            <div class="modal-header">
						                <h5 class="modal-title">Select Return Time</h5>
						                <button type="button" class="close" data-dismiss="modal">&times;</button>
						            </div>
						            <div class="modal-body">
						                <p>Please select return trip time.</p>
						                
						                <div id = "rt_only" style = "display:none">
						                Day:
						                <select name = "return_day">
											<%
												for (String d : dates) {
											%>
												<option value = "<%=d %>"><%=d %></option>
											<%
												}
											%>
										</select>
										</div>
										Time: 
										<select id = "return_time" name = "return_time">
											<%
												ArrayList<StationTrain> reverse = StationTrain.list(destination, origin);
												for (StationTrain st : reverse) {
											%>
												<option value = "<% out.print(st.getId() + " " + st.getDep() + " " + st.getArr()); %>">
													<% out.print(st.getDep() + " - " + st.getArr()); %>
												</option>
											<%
												}
											%>
										</select>
						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-primary" id = "forceCheckout">Checkout</button>
						            </div>
						        </div>
						    </div>
						</div>
					</form>
				</div>
			</div>
			<div class = "col">
				<p><i>Tickets are valid <b>only</b> in one direction.</i></p>
				<h3>Reservations</h3>
				<p>Each ticket purchased will assign you a random seat based on the class chosen, date/time, and train chosen.</p>
				
				<h3>Round Trip and One Way Tickets</h3>
				<p>Tickets are only valid on the date of purchase and for the time stated. Contact us for reservation cancellation if you cannot make the train.
				</p>
				<p>Round trip purchases will provide you with the <b>same selections</b> in both directions, but you will need to specify a return time
				 so a reservation may be created.
				</p>
				<p>Note that weekly or monthly passes are considered <b>round-trip</b>. Return trips for these types of reservations will be set at the 
				same time each day. If there are any scheduling conflicts, please contact customer support. </p>
				<h3>Discounts</h3>
				<p>Child tickets are for ages 5-11</p>
				<p>Senior discounts are for ages 65+</p>
				<p>Please make sure you select the appropriate ticket for the passenger. No refunds will be allowed for
				 any incorrectly purchased tickets, and you risk being escorted off a train for incorrect purchases</p>
			</div>
		</div>
	</div>
	<script
  src="https://code.jquery.com/jquery-3.5.0.min.js"
  integrity="sha256-xNzN2a4ltkB44Mc/Jz3pT4iU1cmeR0FkXs4pru/JxaQ="
  crossorigin="anonymous"></script>
	<script>
	
	const trainidtext = document.getElementById("trainID");
	let time = document.getElementById("time").value;
	trainidtext.innerHTML = time.substring(0, 4);
	
	const arrival = document.getElementById("arrival");
	const trainidinput = document.getElementById("trainidvalue");
	const departure = document.getElementById("departure");
	
	trainidinput.value = time.substring(0,4);
	departure.value = time.substring(5,10);
	arrival.value = time.substring(11, 16);
	
	
	const getTrain = () => {
		const trainidtext = document.getElementById("trainID");
		const arrival = document.getElementById("arrival");
		const trainidinput = document.getElementById("trainidvalue");
		const departure = document.getElementById("departure");
		
		let time = document.getElementById("time").value;
		trainidtext.innerHTML = time.substring(0,4);
		
		trainidinput.value = time.substring(0,4);
		departure.value = time.substring(5,10);
		arrival.value = time.substring(11, 16);
	}
	
	$(document).ready(function(){
	    
	    $("form").submit(function (e) {
			e.preventDefault();
			
			const type = $("input[name=type]:checked").val();
			console.log(type)
			if (type != "oneway")
			{
				$("#myModal").modal("show");
				if (type == "roundtrip")
				{
					$('#rt_only').css({'display': 'block'});
				}
				else {
					$('#rt_only').css({'display': 'none'});
				}
				
			}
			else
			{
				document.createTicket.submit();
			}
		});
	    
	    $("#forceCheckout").on("click", function() {
	    	document.createTicket.submit();
	    })
	});
	
	</script>
<jsp:include page="footer.jsp"></jsp:include>
