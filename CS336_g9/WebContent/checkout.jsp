<%@page import = "java.text.DecimalFormat" %>
<%@page import = "pkg.Auth" %>
<jsp:include page = "header.jsp"></jsp:include>
<jsp:include page = "navigation.jsp"></jsp:include>

<%
	if (request.getParameter("line") == null)
	{
		response.sendRedirect("Schedule?line=all");
	}
	if (!Auth.isLoggedIn(request))
	{
		response.sendRedirect("auth.jsp");
	}
%>
	

<div class = "container">
	<div class = "row">
		<div class = "col">
			<h1>Reservation Confirmation and Pricing</h1>
			<hr />
		</div>
	</div>
	<div class = "row">
		<div class = "col">
			<h3>Ticket Details</h3>
			<div class = "container">
				<div class = "row">
					<div class = "col">
						<b>Train Line</b>
					</div>
					<div class = "col">
						<%= (String) request.getParameter("line") %>
					</div>
				</div>
				<div class = "row">
					<div class = "col">
						<b>Travel Date</b>
					</div>
					<div class = "col">
						<%= (String) request.getParameter("day") %>
					</div>
				</div>
				<div class = "row">
					<div class = "col">
						<b>Origin Station</b>
					</div>
					<div class = "col">
						<%= (String) request.getParameter("origin") %>
					</div>
				</div>
				<div class = "row">
					<div class = "col">
						<b>Destination Station</b>
					</div>
					<div class = "col">
						<%= (String) request.getParameter("destination") %>
					</div>
				</div>
				<%
					if (!((String) request.getParameter("departure")).equals("")) {
				%>
				<div class = "row">
					<div class = "col">
						<b>Origin Departure Time</b>
					</div>
					<div class = "col">
						<%= (String) request.getParameter("departure") %>
					</div>
				</div>
				<div class = "row">
					<div class = "col">
						<b>Destination Arrival Time</b>
					</div>
					<div class = "col">
						<%= (String) request.getParameter("arrival") %>
					</div>
				</div>
				<%
					}
				%>
				<div class = "row">
					<div class = "col">
						<b>Total Travel Time</b>
					</div>
					<div class = "col">
						<%= (Math.abs((Integer) request.getAttribute("total_time"))) + " minutes" %>
					</div>
				</div>
				<div class = "row">
					<div class = "col">
						<b>Train Number</b>
					</div>
					<div class = "col">
						<%= (String) request.getParameter("trainidvalue") %>
					</div>
				</div>
				<br />
				<div class = "row">
					<div class = "col">
						<b>Age Group</b>
					</div>
					<div class = "col">
						<% if (((String) request.getParameter("agegroup")).equals("regular")) { %>
							Adult
						<% } else if (((String) request.getParameter("agegroup")).equals("child")) { %>
							Child
						<% } else { %>
							Senior
						<% } %>
					</div>
				</div>
				<% if ((String) request.getParameter("disabled") != null) { %>
				<div class = "row">
					<div class = "col">
						<b>Special Discounts</b>
					</div>
					<div class = "col">
						Disabled
					</div>
				</div>
				<% } %>
				<div class = "row">
					<div class = "col">
						<b>Class</b>
					</div>
					<div class = "col">
						<% if (((String) request.getParameter("class")).equals("first")) { %>
							First Class
						<% } else if (((String) request.getParameter("class")).equals("business")) { %>
							Business
						<% } else { %>
							Economy
						<% } %>
					</div>
				</div>
				<div class = "row">
					<div class = "col">
						<b>Ticket Type</b>
					</div>
					<div class = "col">
						<% if (((String) request.getParameter("type")).equals("oneway")) { %>
							One Way
						<% } else if (((String) request.getParameter("type")).equals("roundtrip")) { %>
							Round Trip
						<% } else if (((String) request.getParameter("type")).equals("weekly")) { %>
							Weekly
						<% } else { %>
							Monthly
						<% } %>
					</div>
				</div>
				<br />
			</div>
			<div class = "container">
				<% if ( !((String) request.getParameter("type")).equals("oneway") ) { %>
					<h3>Return Trip Details</h3>
					<% if ( ((String) request.getParameter("type")).equals("roundtrip") ) { %>
					<div class = "row">
						<div class = "col">
							<b>Date of Return</b>
						</div>
						<div class = "col">
							<%= (String) request.getParameter("return_day") %>
						</div>
					</div>
					<% } %>
					<%
						String ret_data = (String) request.getParameter("return_time");
						String train = ret_data.substring(0, 4);
						String ret_dep = ret_data.substring(5, 10);
						String ret_arr = ret_data.substring(11, 16);
					%>
					<div class = "row">
						<div class = "col">
							<b>Return Origin Station</b>
						</div>
						<div class = "col">
							<%= (String) request.getParameter("destination") %>
						</div>
					</div>
					<div class = "row">
						<div class = "col">
							<b>Return Destination Station</b>
						</div>
						<div class = "col">
							<%= (String) request.getParameter("origin") %>
						</div>
					</div>
					<div class = "row">
						<div class = "col">
							<b>Return Departure Time</b>
						</div>
						<div class = "col">
							<%= ret_dep %>
						</div>
					</div>
					<div class = "row">
						<div class = "col">
							<b>Return Arrival Time</b>
						</div>
						<div class = "col">
							<%= ret_arr %>
						</div>
					</div>
					<div class = "row">
						<div class = "col">
							<b>Return Train</b>
						</div>
						<div class = "col">
							<%= train %>
						</div>
					</div>
				<% } %>
			</div>
		</div>
		<div class = "col-5">
			<h3>Price</h3>
			<table class = "table borderless">
				<thead>
				</thead>
				<tbody>
					<tr>
						<td><b>Initial Price</b></td>
						<td><%= (String) request.getAttribute("initial_price") %></td>
					</tr>
					<% 
						
						if ( (Boolean) request.getAttribute("age_discount") )
						{
					%>
					<tr>
						<td><b>Age Discount</b></td>
						<td style = "border-bottom: 1px solid gray">-<%= (String) request.getAttribute("discount_for_age") %></td>
					</tr>
					<tr>
						<td><b>Price After Age Discount</b></td>
						<td><%= (String) request.getAttribute("price_after_age") %></td>
					</tr>
					<% } %>
					
					<% if ( (Boolean) request.getAttribute("disabled_discount") ) { 
						
					%>
					<tr>
						<td><b>Disabled Discount</b></td>
						<td style = "border-bottom: 1px solid gray">-<%=(String) request.getAttribute("discount_for_disabled") %></td>
					</tr> 
					<tr>
						<td><b>Price After Special Discount</b></td>
						<td><%= (String) request.getAttribute("price_after_disabled") %></td>
					</tr>
					<% } %>
					
					<% if ( !((String) request.getAttribute("class_premium")).equals("$00.00") ) { %>
					<tr>
						<td><b>Class Premium</b></td>
						<td style = "border-bottom: 1px solid gray">+<%= (String) request.getAttribute("class_premium") %>
					</tr>
					<tr>
						<td><b>Price After Class Premium</b></td>
						<td><%= (String) request.getAttribute("price_after_class") %></td>
					</tr>
					<% } %>
					
					<% if ( !((String) request.getParameter("type")).equals("oneway") )  {
					%>
					<tr>
						<td><b>Trip Multiplier</b></td>
						<td style = "border-bottom: 1px solid gray">+<%=(String) request.getAttribute("extra_ticket_price") %></td>
						<td style = "float: right"><%= (String) request.getAttribute("multiplier") %></td>
					</tr>
					<tr>
						<td ><b>Total Costs</b></td>
						<td><%= (String) request.getAttribute("price_after_bulk") %></td>
					</tr>
					<% if (( (String) request.getParameter("type")).equals("weekly") || (((String) request.getParameter("type")).equals("monthly"))) { %>
					<tr>
						<td><b>Bulk Discounts</b></td>
						<td style = "border-bottom: 1px solid gray"><%= (String) request.getAttribute("bulk_discount") %></td>
					</tr>
					<tr>
						<td><b>Price After Bulk Discounts</b></td>
						<td><%= (String) request.getAttribute("price_before_tax") %></td>
					</tr>
					<%
						}
					}
					%>
					<tr>
						<td><b>Price Before Taxes and Fees</b></td>
						<td><%= (String) request.getAttribute("price_before_tax") %></td>
					</tr>
					
					
					
					<tr>
						<td>
							<b>Booking Fees and Charges</b>
						</td>
						<td style = "border-bottom: 1px solid gray">
							+<%=(String) request.getAttribute("booking_fee") %>
						</td>
					</tr>
					
					<tr>
						<td><b>Price Before Tax</b></td>
						<td>
							<%= (String) request.getAttribute("price_after_booking_fee") %>
						</td>
					</tr>
					
					<tr>
						<td style = "border-bottom: 1px solid gray"><b>Tax</b></td>
						<td style = "border-bottom: 1px solid gray">
							+<%= (String) request.getAttribute("applied_tax") %>
						</td>
					</tr>
					<tr>
						<td><b>Total</b></td>
						<td><b>
							<%= (String) request.getAttribute("total_cost")%>
							</b>
						</td>
					</tr>
					<tr></tr>
					<tr style = "color: grey">
						<td><i>Total Discounts</i></td>
						<td><i><%=(String) request.getAttribute("total_discount") %></i></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class = "row">
		<div class = "col">
			<form action = "Reservation?action=finish" method = "post">
			<%
				String ret_data = (String) request.getParameter("return_time");
				String train = ret_data.substring(0, 4);
				String ret_dep = ret_data.substring(5, 10);
				String ret_arr = ret_data.substring(11, 16);
			%>
				<input type = "hidden" name = "booking_fee" value = "<%= (String) request.getAttribute("booking_fee") %>" />
				<input type = "hidden" name = "ticket_class" value = "<%= (String) request.getAttribute("class") %>" />
				<input type = "hidden" name = "total_time" value = "<%= (Integer) request.getAttribute("total_time") %>" />
				<input type = "hidden" name = "total_cost" value = "<%= (String) request.getAttribute("total_cost") %>" />
				<input type = "hidden" name = "total_discount" value = "<%= (String) request.getAttribute("total_discount") %>" />
				<input type = "hidden" name = "fdate" value = "<%= (String) request.getParameter("day") %>" />
				
				<input type = "hidden" name = "origin" value = "<%= (String) request.getParameter("origin") %>" />
				<input type = "hidden" name = "destination" value = "<%= (String) request.getParameter("destination") %>" />
				
				<input type = "hidden" name = "ftid" value = "<%= (String) request.getParameter("trainidvalue") %>" />
				<input type = "hidden" name = "fdep" value = "<%= (String) request.getParameter("departure") %>" />
				<input type = "hidden" name = "farr" value = "<%= (String) request.getParameter("arrival") %>" />
				
				<input type = "hidden" name = "rtid" value = "<%= train %>" />
				<input type = "hidden" name = "rdep" value = "<%= ret_dep %>" />
				<input type = "hidden" name = "rarr" value = "<%= ret_arr %>" />
				<% if ( ((String) request.getParameter("type")).equals("roundtrip") ) { %>
					<input type = "hidden" name = "rdate" value = "<%= (String) request.getParameter("return_day") %>" />
				<% } %>
				
				<input type = "hidden" name = "type" value = "<%= (String) request.getParameter("type") %>" />
				
				<button class = "btn btn-primary">Confirm Purchase</button>
			</form>
		</div>
	</div>
</div>
<style>
  .borderless tr, .borderless td, .borderless th {
    border: none;
   }
   .borderless tr>td:nth-child(2) {
   float: right;
   }
</style>

<jsp:include page = "footer.jsp"></jsp:include>