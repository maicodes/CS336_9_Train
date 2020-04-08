<%@ page import="pkg.*"%>
<jsp:include page="header.jsp"/>

<!-- This web page implements Customer Representative functionalities -->
<div id="admin">
	<div class="row">
	<div class="col-2 vertical-nav-con">
	<ul class="vertical-nav" data-active="#adminEmp">
		<li class="vertical-nav-item"><a href="#adminEmp" class="toggle-btn btn btn-outline-light">Employees</a></li>
		<li class="vertical-nav-item"><a href="#adminCus" class="toggle-btn btn btn-outline-light">Customers</a> </li>
		<li class="vertical-nav-item"><a href="#adminMes" class="toggle-btn btn btn-outline-light">Messages</a></li>
		<li class="vertical-nav-item"><a href="#adminTra" class="toggle-btn btn btn-outline-light">Train Schedules</a></li>
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
				    		<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#adminEmp--delete-modal">Delete Employee</button>

			</div>
					<!-- New Employee Form -->
				    		<div id="adminEmp-add-modal" aria-hidden="true">
				    			<form action="NewEmployee" method="post">
									  <div class="form-group">
									    <label for="admin-emp-ssn">SSN</label>
									    <input type="text" class="form-control" id="admin-emp-ssn" name="emp-ssn">
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
		
		<div id="adminMes" aria-hidden="true">
			Messages
		</div>
		
		<div id="adminTra" aria-hidden="true">
			Train Schedules
		</div>
	</div>
	</div>

</div>

<jsp:include page="footer.jsp"/>