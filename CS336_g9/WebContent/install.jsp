<%@ page import="pkg.Auth"%>
<%@ page import= "pkg.Models.Customer" %>
<jsp:include page = "header.jsp"></jsp:include>
<jsp:include page = "navigation.jsp"></jsp:include>

<p>Click what you want to install into to the database.</p>
<p>Note: This will take a <b>long</b> time. Stops will take maybe 5-10 minutes, as it generates 3000+ entries through an algorithm.</p>
<p>If successful, the next page will only have a service message</p>
<p>If not, it will print SQL error.</p>

<p>Keep in mind if you want to reinstall the Trains, it will delete all stops</p>
<p>This is because we didn't set ON DELETE CASCADE so it will not allow us to delete the trains</p>
<p>Make sure to install stops after you install trains if that's the case.</p>

<form method = get action = "Install">
	<input type = "hidden" value = "trains" name = "type">
	Click the button to add trains in to the database.
	<br>
	Input the number of trains per line (Recommend 7-10)<input type = text name = "trains">
	<button>Install</button>
</form>

<form method = get action = "Install">
	Click the button to add stops in to the database.
	<br>
	<input type = "hidden" value = "stops" name = "type">
	<button>Install</button>
</form>

<jsp:include page="footer.jsp"></jsp:include>