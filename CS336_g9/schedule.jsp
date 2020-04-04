<%@ page import="pkg.Auth"%>
<%@ page import= "pkg.Models.Customer" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "pkg.Models.Train" %>
<%@ page import = "pkg.Models.Stop" %>
<%@ page import = "pkg.Common" %>
<jsp:include page = "header.jsp"></jsp:include>
<jsp:include page = "navigation.jsp"></jsp:include>
<%! Common lib = new Common(); %>
<%-- <%
	if (!Auth.isLoggedIn())
	response.sendRedirect("index.jsp");
%> --%>

<h1>Train Schedule</h1>
<%
	if (request.getAttribute("transit_line_names") == null)
	{
			/* out.print("<p>An error has occured fetching the available Transit Lines. Please " + 
			"try again later or contact the site admin if this problem persists.");
 */	}
	else
		{
		@SuppressWarnings("unchecked")
		ArrayList<String> line_names = (ArrayList<String>) request.getAttribute("transit_line_names");
		
		for (String s : line_names)
		{
			out.print("<p><a href = Schedule?line=" + s.replaceAll("\\s","") + ">" + s + "</a></p>");
		}
	}
%>

<%
if (request.getAttribute("line_name") != null) {
	@SuppressWarnings("unchecked")
	ArrayList<Train> trains = (ArrayList<Train>) request.getAttribute("trains");
	Stop titlePointer = trains.get(0).get_trips().get(0);
	if (titlePointer.get_nextStop().get_position() < titlePointer.get_position())
	{
		titlePointer = trains.get(1).get_trips().get(0);
	}
	Stop stopPointer;
	String base = "05:00";
%>

<h2>Forward Path</h2>
	<table border = 1 style = "white-space: nowrap;">
		<thead>
			<tr>
				<th>Train</th>
				<%
					while (titlePointer != null) {
				%>
				<th><%= titlePointer.get_station_name() %></th>
				<%
						titlePointer = titlePointer.get_nextStop();
					}
				%>
			</tr>
		</thead>
		<tbody>
			<%
				while (!base.equals("23:30")) {
			%>
			<tr>
				<%
					stopPointer = null;
					for (Train t : trains)
					{
						if (t.findTime(base) != null && (t.findTime(base).get_nextStop().get_position() > t.findTime(base).get_position()))
						{
							stopPointer = t.findTime(base);
				%>
						<td><%= t.getT_id() %>
				<%
						}
						
					}
					while (stopPointer != null) {
				%>
					<td><%= stopPointer.get_arrive_time() %></td>
				<%
				 		stopPointer = stopPointer.get_nextStop();
					}
				%>
			</tr>
			<%
					base = lib.addTime(base, 30);
				}
			%>
		</tbody>
	</table>
	
<%
	base = "05:00";
	titlePointer = trains.get(0).get_trips().get(0);
%>
<h2>Return Path</h2>
	<table border = 1 style = "white-space: nowrap;">
		<thead>
			<tr>
				<th>Train</th>
				<%
					while (titlePointer != null) {
				%>
				<th><%= titlePointer.get_station_name() %></th>
				<%
						titlePointer = titlePointer.get_nextStop();
					}
				%>
			</tr>
		</thead>
		<tbody>
			<%
				while (!base.equals("23:30")) {
			%>
			<tr>
				<%
					stopPointer = null;
					for (Train t : trains)
					{
						if (t.findTime(base) != null && (t.findTime(base).get_nextStop().get_position() < t.findTime(base).get_position()))
						{
							stopPointer = t.findTime(base);
				%>
						<td><%= t.getT_id() %>
				<%
						}
						
					}
					while (stopPointer != null) {
				%>
					<td><%= stopPointer.get_arrive_time() %></td>
				<%
				 		stopPointer = stopPointer.get_nextStop();
					}
				%>
			</tr>
			<%
					base = lib.addTime(base, 30);
				}
			%>
		</tbody>
	</table>
		
<% } %>

<jsp:include page="footer.jsp"></jsp:include>