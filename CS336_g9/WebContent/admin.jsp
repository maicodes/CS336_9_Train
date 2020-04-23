<%@ page import="pkg.*"%>
<jsp:include page="header.jsp"/>


<% // if Manager session is not set - user havent logined as admin, redirect to index.jsp
if((request.getSession(false).getAttribute("Manager")== null) )
{
%>
<jsp:forward page="/"></jsp:forward>
<%} %>

<!-- This web page implements Admin/Manager functionalities -->
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
									  <div class="form-group">
									   <label for="admin-emp-role">Role</label>
									   <select id="admin-emp-role" name="emp-role">
									   		<option value="Manager">Manager</option>
									   		<option value="CusRep">Customer Representative</option>
									   </select>
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
				    			<form action="admin_Employees" method="post" class="needs-validation" novalidate>
									  <div class="form-group">
									    <label for="admin-emp-edit-ssn">Select an employee's ssn to edit: </label>
									    <input type="text" class="form-control" id="admin-emp-edit-ssn" name="edit-emp-ssn" required>
									    <div class="invalid-feedback">
        									SSN cannot be empty
      									</div>
									  </div>
									  <h6>Select one or more fields to edit:</h6>
									  <div class="form-group">
									    <label for="admin-emp-edit-new-ssn">SSN</label>
									    <input type="text" class="form-control" id="admin-emp-edit-new-ssn" name="emp-new-ssn">
									  </div>
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
			<!-- End Edit Employee Form -->
		
    		<br>
    		<h3>List of all employees:</h3>	
    		<jsp:include page="admin_employees.jsp"/> 
		</div>
		
		<!-- 
			Customers : Add, Edit, Delete Info
		 -->
		<div id="adminCus" aria-hidden="true" >
			<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#adminCus-add-modal">Add New Customer</button>
			<!-- Modal -->
				<div id="adminCus-add-modal" aria-hidden="true">
				<!-- New Customer Form-->
 						<form action="admin_Customers" method="post"  class="needs-validation" novalidate>
 							  <input type = "hidden" value = "add" name = "type">
						      <label for="admin-cus-add-firstName">First Name</label>
						      <input type="text" name="fname" class="form-control" id="admin-cus-add-firstName" placeholder="First name" required>
						      <div class="invalid-feedback">
						          Please enter customer's first name!
						      </div>
						    
						      <label for="admin-cus-add-lastName">Last name</label>
						      <input type="text" name="lname" class="form-control" id="admin-cus-add-lastName" placeholder="Last name" required>
						      <div class="invalid-feedback">
						          Please enter customer's last name!
						      </div>
						
						      <label for="admin-cus-add-uName">Username</label>
						        <input type="text" name="uname" class="form-control" id="admin-cus-add-uName" placeholder="Username" required>
						        <div class="invalid-feedback">
						          Please enter customer's user name!
						        </div>
						   
						
						      <label for="admin-cus-add-pw">Password</label>
						        <input type="password" name="pw" class="form-control" id="admin-cus-add-pw" placeholder="Password" required>
						        <div class="invalid-feedback">
						          Password is required!
						        </div>
						        
						       <label for="admin-cus-add-dob">DOB</label>
							      <input type="date" name="dob" class="form-control" id="admin-cus-add-dob" required>
							      <div class="invalid-feedback">
							        DOB is required!
							      </div>
						 
						      <label for="admin-cus-add-email">Email</label>
						        <input type="email" name="email" class="form-control" id="admin-cus-add-email">
			
						        
						      <label for="admin-cus-add-phone">Telephone</label>
						        <input type="text" name="phone" class="form-control" id="phone">
						 
						      <label for="admin-cus-add-address">Address</label>
						      <input type="text" name="address" class="form-control" id="admin-cus-add-address">
	
						
						      <label for="admin-cus-add-city">City</label>
						      <input type="text" name="city" class="form-control" id="admin-cus-add-city">
						
						      <label for="admin-cus-add-state">State</label>
						      <input type="text" name="state" class="form-control" id="admin-cus-add-state">
						    
						      <label for="admin-cus-add-zip">Zip Code</label>
						      <input type="text" name="zip" class="form-control" id="admin-cus-add-zip" required>
							  <div class="invalid-feedback">
							       Zipcode is required!
							  </div>
							      
						      <button type="submit" class="auth-btn btn btn-primary">Submit</button>
						</form>
					<!-- End New Customer Form -->
					</div>
					
				<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#adminCus-edit-modal">Edit Customer Information</button>
				<!-- Modal -->
				<div id="adminCus-edit-modal" aria-hidden="true">
				<!-- Edit Customer Form-->
 						<form action="admin_Customers" method="post"  class="needs-validation" novalidate>
 							  <input type = "hidden" value = "edit" name = "type">
 							  <p>Please fill in the user name and other fields that you wish to edit!</p>
 							  <label for="admin-cus-edit-uName">User Name</label>
						        <input type="text" name="uname" class="form-control" id="admin-cus-edit-uName" placeholder="Username" required>
						        <div class="invalid-feedback">
						          Please enter customer's user name!
						        </div>
						        
						      <label for="admin-cus-edit-new-uname">New User Name</label>
						      <input type="text" name="new-uname" class="form-control" id="admin-cus-edit-new-uname" placeholder="User name">
						      
						      <label for="admin-cus-edit-firstName">First Name</label>
						      <input type="text" name="fname" class="form-control" id="admin-cus-edit-firstName" placeholder="First name">
						    
						      <label for="admin-cus-edit-lastName">Last Name</label>
						      <input type="text" name="lname" class="form-control" id="admin-cus-edit-lastName" placeholder="Last name">
						   
						      <label for="admin-cus-edit-pw">Password</label>
						        <input type="password" name="upassword" class="form-control" id="admin-cus-edit-pw" placeholder="Password">
						        
						      <label for="admin-cus-add-dob">DOB</label>
							      <input type="date" name="dob" class="form-control" id="admin-cus-add-dob">
						 
						      <label for="admin-cus-edit-email">Email</label>
						        <input type="email" name="email" class="form-control" id="admin-cus-edit-email">
						        
						      <label for="admin-cus-edit-phone">Telephone</label>
						        <input type="text" name="phone" class="form-control" id="admin-cus-edit-phone">

						      <label for="admin-cus-edit-address">Address</label>
						      <input type="text" name="address" class="form-control" id="admin-cus-edit-address">
						
						      <label for="admin-cus-edit-city">City</label>
						      <input type="text" name="city" class="form-control" id="admin-cus-edit-city">
						
						      <label for="admin-cus-edit-state">State</label>
						      <input type="text" name="state" class="form-control" id="admin-cus-edit-state">
						    
						      <label for="admin-cus-edit-zip">Zip Code</label>
						      <input type="text" name="zip" class="form-control" id="admin-cus-edit-zip">
						
						      <button type="submit" class="auth-btn btn btn-primary">Submit</button>
						</form> 
					<!-- End Edit Customer Form -->
				</div>
				
				<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#adminCus-del-modal">Delete Customer Information</button>
				<!-- Modal -->
				<div id="adminCus-del-modal" aria-hidden="true">
				<!-- Delete Customer Form-->
 						<form action="admin_Customers" method="post"  class="needs-validation" novalidate>
 							  <input type = "hidden" value = "del" name = "type">
						      <label for="admin-cus-del-uName">Username</label>
						        <input type="text" name="uname" class="form-control" id="admin-cus-del-uName" placeholder="Username" required>
						        <div class="invalid-feedback">
						          Please enter customer's user name!
						        </div>
						      <button type="submit" class="auth-btn btn btn-primary">Submit</button>
						</form> 
					<!-- End Delete Customer Form -->
				</div>
    		<br>
    		<h3>List of all customers:</h3>
    		<jsp:include page="admin_customers.jsp"/>
		</div>
		
		
		<!--  
			Obtain sales reports for a particular month
		-->
		<div id="adminSaleReports" aria-hidden="true">
			<h3>Obtain sales report for a particular month:</h3>
    		<form action="admin_salesReports" method="post">
    			<div class="form-group">
    				<label for="admin-sales-y">Select a year: </label>
					<input type="number" class="form-control" id="admin-sales-y" name="admin-sales-year">
					<label for="admin-sales-m">Select a month: </label>
					<input type="number" class="form-control" id="admin-sales-m" name="admin-sales-month">
				</div>
				  <button type="submit" class="btn btn-primary">Submit</button>
    		</form>
		</div>
		
		<!--  
			Reservations
		-->
		<div id="adminReservations" aria-hidden="true">
			<h5>Get a list of reservations by transit line and train number:</h5>
    		<form action="admin_reservations" method="post">
    			<div class="form-group">
					<label for="admin-res-transitLine">Transit Line Name: </label>
					<input type="text" class="form-control" id="admin-res-transitLine" name="admin-res-tran">
					<div class="form-group">
					<label for="admin-res-train">Train Number: </label>
					<input type="number" class="form-control" id="admin-res-train" name="admin-res-train">
				</div>
				</div>
				  <button type="submit" class="btn btn-outline-primary">Submit</button>
    		</form>
    		<br>
    		<h5>Get a list of reservations by customer name:</h5>
    		<form action="admin_reservations" method="post">
    			<div class="form-group">
					<label for="admin-res-fname">First Name: </label>
					<input type="text" class="form-control" id="admin-res-fname" name="admin-res-fname">
					<label for="admin-res-lname">Last Name: </label>
					<input type="text" class="form-control" id="admin-res-lname" name="admin-res-lname">
				</div>
				  <button type="submit" class="btn btn-outline-primary">Submit</button>
    		</form>
		</div>
		
		<!--  
			Revenue
		-->
		<div id="adminRevenue" aria-hidden="true">
				<div> 
				<h3>Get The Best Customer</h3>
			    		<form action="admin_revenue" method="post">
			    			  <input type="hidden" name="type" value="bestCustomer"></input>
							  <button type="submit" class="btn btn-outline-primary">Click Here</button>
			    		</form> 
			    </div>
			    
			    <br>
			    
			    <div> 
			    <h3>Get Best 5 Most Active Transit Lines </h3>
			    		<form action="admin_revenue" method="post">
			    			  <input type="hidden" name="type" value="5mostActiveTransitLine"></input>
							  <button type="submit" class="btn btn-outline-primary">Click Here</button>
			    		</form> 
			    </div>
			    
			    <br>
				 
				<h1> Produce a listing of revenue per: </h1>
				<div> 
					 <h3>Transit Line:</h3>
			    		<form action="admin_revenue" method="post">
			    			<input type="hidden" name="type" value="revByTransitLine"></input>
							  <button type="submit" class="btn btn-outline-primary">Click Here</button>
			    		</form> 
			    </div>
			    
			    <br>
			    
			    <div> 
			    		<h3>Destination City:</h3>
			    		<form action="admin_revenue" method="post">
			    			<input type="hidden" name="type" value="revByCity"></input>
							  <button type="submit" class="btn btn-outline-primary">Click Here</button>
			    		</form> 
			    </div>
			    
			    <br>
			    
			   <div> 
			   			<h3>Customer Name:</h3>
			    		<form action="admin_revenue" method="post">
			    			<input type="hidden" name="type" value="revByCus"></input>
							  <button type="submit" class="btn btn-outline-primary">Click Here</button>
			    		</form> 
			    </div>
		</div>
</div>
</div>
</div>

<jsp:include page="footer.jsp"/>