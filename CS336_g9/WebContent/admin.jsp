<%@ page import="pkg.*"%>
<jsp:include page="header.jsp"/>

<!-- This web page implements Customer Representative functionalities -->
<div id="admin">
	<div class="row">
	<div class="col-2 vertical-nav-con">
	<ul class="vertical-nav" data-active="#adminEmp">
		<li class="vertical-nav-item"><a href="#adminEmp" class="toggle-btn btn btn-outline-light">Manage Employees</a></li>
		<li class="vertical-nav-item"><a href="#adminCus" class="toggle-btn btn btn-outline-light">Manage Customers</a> </li>
		<li class="vertical-nav-item"><a href="#adminSaleReports" class="toggle-btn btn btn-outline-light">Manage Sales Reports</a></li>
		<li class="vertical-nav-item"><a href="#adminReservations" class="toggle-btn btn btn-outline-light">Manage Reservations</a></li>
		<li class="vertical-nav-item"><a href="#adminRevenue" class="toggle-btn btn btn-outline-light">Manage Revenue</a></li>
	</ul>
	</div>

	<div class="col-8 contentBox">
		<!-- 
			Employees : Add, Edit, Delete
		 -->
		<div id="adminEmp" aria-hidden="false">
			<div class="group-btns">
							<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#adminEmp-add-modal">Add New Employee</button>
							<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#adminEmp-edit-modal">Edit Employee Info</button>
				    		<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#adminEmp-delete-modal">Delete Employee</button>

			</div>
			<!-- New Employee Form -->
				    		<div id="adminEmp-add-modal" aria-hidden="true">
				    			<h5>Add New Employee</h5>
				    			<form action="admin_Employees" method="post">
									  <div class="form-group">
									    <label for="admin-emp-ssn">SSN</label>
									    <input type="text" class="form-control" id="admin-emp-ssn" name="add-emp-ssn">
									  </div>
									  <div class="form-group">
									    <label for="admin-emp-fname">First Name</label>
									    <input type="text" class="form-control" id="admin-emp-fname" name="emp-fname">
									  </div>
									  <div class="form-group">
									    <label for="admin-emp-lname">Last Name</label>
									    <input type="text" class="form-control" id="admin-emp-lname" name="emp-lname">
									  </div>
									  <div class="form-group">
									    <label for="admin-emp-uname">User Name</label>
									    <input type="text" class="form-control" id="admin-emp-uname" name="emp-uname">
									  </div>
									  <div class="form-group">
									    <label for="admin-emp-pass">Password</label>
									    <input type="password" class="form-control" id="admin-emp-pass" name="emp-pass">
									  </div>
									  <button type="submit" class="btn btn-primary">Submit</button>
								</form>
				    		</div>
			<!-- End New Employee Form -->
				    
			<!-- Delete Employee Form -->
			<div>
							<div id="adminEmp-delete-modal" aria-hidden="true">
								<h5>Delete Employee</h5>
				    			<form action="admin_Employees" method="post">
									  <div class="form-group">
									    <label for="admin-emp-del-ssn">Employee SSN: </label>
									    <input type="text" class="form-control" id="admin-emp-del-ssn" name="del-emp-ssn">
									  </div>
									  <button type="submit" class="btn btn-primary">Submit</button>
								</form>
				    		</div>	
			
			</div>
			<!-- End Delete Employee Form -->
			
			
			<!-- Edit Employee Form -->
				    		<div id="adminEmp-edit-modal" aria-hidden="true">
				    			<h5>Edit Employee Information</h5>
				    			<form action="admin_Employees" method="post">
									  <div class="form-group">
									    <label for="admin-emp-edit-ssn">Select an employee's ssn to edit: </label>
									    <input type="text" class="form-control" id="admin-emp-edit-ssn" name="edit-emp-ssn">
									  </div>
									  <h6>Select one or more fields to edit:</h6>
									  <div class="form-group">
									    <label for="admin-emp-fname">First Name</label>
									    <input type="text" class="form-control" id="admin-emp-edit-fname" name="emp-fname">
									  </div>
									  <div class="form-group">
									    <label for="admin-emp-lname">Last Name</label>
									    <input type="text" class="form-control" id="admin-emp-edit-lname" name="emp-lname">
									  </div>
									  <div class="form-group">
									    <label for="admin-emp-uname">User Name</label>
									    <input type="text" class="form-control" id="admin-emp-edit-uname" name="emp-uname">
									  </div>
									  <div class="form-group">
									    <label for="admin-emp-pass">Password</label>
									    <input type="password" class="form-control" id="admin-emp-edit-pass" name="emp-pass">
									  </div>
									  <button type="submit" class="btn btn-primary">Submit</button>
								</form>
				    		</div>
			<!-- End New Employee Form -->
		
    		<br>
    		<h3>List of all employees:</h3>
    		<jsp:include page="admin_employees.jsp"/>
		</div>
		
		<!-- 
			Customers : Add, Edit, Delete Info
		 -->
		<div id="adminCus" aria-hidden="true">
			<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#adminCus-modal">Add New Customer</button>
			<!-- Modal -->
				<div id="adminCus-modal" aria-hidden="true">
					Ask TA do we need to create new customer
				</div>
    		<br>
    		<h3>List of all customers:</h3>
    		<jsp:include page="admin_customers.jsp"/>
		</div>
		
		
		<!--  
			Obtain sales reports for a particular month
		-->
		<div id="adminSaleReports" aria-hidden="true">
			<h3>Obtain sales reports by months:</h3>
    		<form action="admin_salesReports" method="post">
    			<div class="form-group">
					<label for="admin-sales">Select a month: </label>
					<input type="number" class="form-control" id="admin-sales" name="admin-sales-month">
				</div>
				  <button type="submit" class="btn btn-primary">Submit</button>
    		</form>
		</div>
		
		<!--  
			Reservations
		-->
		<div id="adminReservations" aria-hidden="true">
			<h5>Get a list of reservations by transit line:</h5>
    		<form action="admin_reservations" method="post">
    			<div class="form-group">
					<label for="admin-res-transitLine">Transit Line Name: </label>
					<input type="text" class="form-control" id="admin-res-transitLine" name="admin-res-tran">
				</div>
				  <button type="submit" class="btn btn-outline-primary">Submit</button>
    		</form>
    		<br>
    		<h5>Get a list of reservations by train number:</h5>
    		<form action="admin_reservations" method="post">
    			<div class="form-group">
					<label for="admin-res-train">Train Number: </label>
					<input type="number" class="form-control" id="admin-res-train" name="admin-res-train">
				</div>
				  <button type="submit" class="btn btn-outline-primary">Submit</button>
    		</form>
    		<br>
    		<h5>Get a list of reservations by customer name:</h5>
    		<form action="admin_reservations" method="post">
    			<div class="form-group">
					<label for="admin-res-cus">Customer Name: </label>
					<input type="text" class="form-control" id="admin-res-cus" name="admin-res-cus">
				</div>
				  <button type="submit" class="btn btn-outline-primary">Submit</button>
    		</form>
		</div>
		
		<!--  
			Revenue
		-->
		<div id="adminRevenue" aria-hidden="true">
				[] Produce a listing of revenue per:
				 [] transit line
				 [] destination city
				 [] customer name
				 [] best customer
				 [] best 5 most active transit lines 
		</div>
	</div>
	</div>

</div>

<jsp:include page="footer.jsp"/>