<jsp:include page="header.jsp"/>

<% // if CusRep session is not set - user havent logined as cusRep, redirect to index.jsp
if((request.getSession(false).getAttribute("CusRep")== null) )
{
%>
<jsp:forward page="/"></jsp:forward>
<%} %>

<!-- This web page implements Customer Representative functionalities -->

<div id="cusRep">
	<div class="row">
	<div class="col-2 vertical-nav-con">
	<ul class="vertical-nav" data-active="#cusRepRes">
		<li class="vertical-nav-item"><a href="#cusRepRes" class="toggle-btn btn btn-outline-light">Manage Reservations</a></li>
		<li class="vertical-nav-item"><a href="#cusRepTra" class="toggle-btn btn btn-outline-light">Manage Train Schedules</a></li>
	</ul>
	</div>

	<div class="col-8" class="contentBox">
		<br><br>
		<!-- 
			Reservations : Add, Edit, Delete
		 -->
		<div id="cusRepRes" aria-hidden="false">
			<div class="group-btns">
							<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#new-res-model">Add A New Reservation</button>
							<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#edit-res-modal">Edit A Reservation</button>
				    		<button class="single-toggle-btn btn btn-outline-primary" aria-controls="#delete-res-modal">Delete A Reservation</button>

			</div>
			<!-- New Reservation Form -->
				    		<div id="new-res-model" aria-hidden="true">
				    			<h5>Add A New Reservation For A Customer</h5>
				    			<form action="cusRep_reservations" method="post">
				    				  <input type="hidden" name="type" value="add">
				    				  <div class="form-group">
									    <label for="cusRep-res-uname">Customer User Name</label>
									    <input type="text" class="form-control" id="cusRep-res-uname" name="uname">
									  </div>
									  <div class="form-group">
									    <label for="cusRep-res-originStop">Origin Stop</label>
									    <input type="text" class="form-control" id="cusRep-res-originStop" name="origin-stop">
									  </div>
									  <div class="form-group">
									    <label for="cusRep-res-desStop">Destination Stop</label>
									    <input type="text" class="form-control" id="cusRep-res-desCity" name="des-stop">
									  </div>
									  <div class="form-group">
									   <label for="cusRep-res-fare">Fare</label>
									   <select id="cusRep-res-fare" name="fare">
									   		<option value="single">Single</option>
									   		<option value="weekly">Weekly</option>
									   		<option value="monthly">Monthly</option>
									   </select>
									  </div>
									  <div class="form-group">
									   <label for="cusRep-res-trip">Trip</label>
									   <select id="cusRep-res-trip" name="trip">
									   		<option value="single">One-Way</option>
									   		<option value="round">Round Trip</option>
									   </select>
									  </div>
									  
									  <button type="submit" class="btn btn-primary">Submit</button>
								</form>
				    		</div>
			<!-- End New Reservation Form -->
				    
			<!-- Delete Reservation Form -->
			<div>
							<div id="delete-res-modal" aria-hidden="true">
								<h5>Delete A Reservation</h5>
				    			<form action="cusRep_reservations" method="post">
				    				  <input type="hidden" name="type" value="delete">
									  <div class="form-group">
									    <label for="cusRep-res-rid">Reservation ID (rid) </label>
									    <input type="text" class="form-control" id="cusRep-res-rid" name="rid">
									  </div>
									  <button type="submit" class="btn btn-primary">Submit</button>
								</form>
				    		</div>	
			
			</div>
			<!-- End Delete Reservation Form -->
			
			
			<!-- Edit Reservation Form -->
				    		<div id="edit-res-modal" aria-hidden="true">
				    			<h5>Edit A Reservation</h5>
				    			<form action="cusRep_reservations" method="post"  class="needs-validation" novalidate>
				    				  <input type="hidden" name="type" value="edit">
				    				  <div class="form-group">
									    <label for="cusRep-res-edit-rid">Reservation ID (rid)</label>
									    <input type="text" class="form-control" id="cusRep-res-edit-rid" name="rid" required>
									    <div class="invalid-feedback">
        									Please enter the rid of the reservation that you wish to edit!
      									</div>
									  </div>
									  
									  Please fill in one or more fields that you wish to edit!
									  <div class="form-group">
									    <label for="cusRep-res-edit-originCity">Origin City</label>
									    <input type="text" class="form-control" id="cusRep-res-edit-originCity" name="origin-city">
									  </div>
									  <div class="form-group">
									    <label for="cusRep-res-edit-desCity">Destination City</label>
									    <input type="text" class="form-control" id="cusRep-res-edit-desCity" name="des-city">
									  </div>
									  <div class="form-group">
									    <label for="cusRep-res-date">Travel Date</label>
									    <input type="text" class="form-control" id="cusRep-res-date" name="date">
									  </div>
									  <div class="form-group">
									   <label for="cusRep-res-fare">Fare</label>
									   <select id="cusRep-res-fare" name="fare">
									   		<option value="single">Single</option>
									   		<option value="weekly">Weekly</option>
									   		<option value="monthly">Monthly</option>
									   </select>
									  </div>
									  <div class="form-group">
									   <label for="cusRep-res-trip">Trip</label>
									   <select id="cusRep-res-trip" name="trip">
									   		<option value="single">One-Way</option>
									   		<option value="round">Round Trip</option>
									   </select>
									  </div>
									  <div class="form-group">
									   <label for="cusRep-res-class">Class</label>
									   <input type="text" class="form-control" id="cusRep-res-class" name="class">
									  </div>
									  
									  <div class="form-group">
									   <label for="cusRep-res-totalDiscount">Total Discount ($)</label>
									   <input type="number" class="form-control" id="cusRep-res-totalDiscount" name="discount">
									  </div>
									  
									  <div class="form-group">
									   <label for="cusRep-res-fee">Booking Fee ($)</label>
									   <input type="number" class="form-control" id="cusRep-res-fee" name="fee">
									  </div>
									  
									  <div class="form-group">
									   <label for="cusRep-res-totalFare">Total Fare ($)</label>
									   <input type="number" class="form-control" id="cusRep-res-totalFare" name="total-fare">
									  </div>
									  
									  <div class="form-group">
									   <label for="cusRep-res-travelTime">Travel Time (minutes)</label>
									   <input type="number" class="form-control" id="cusRep-res-travelTime" name="travel-time">
									  </div>
									  
									  <button type="submit" class="btn btn-primary">Submit</button>
								</form>
				    		</div>
			<!-- End Edit Reservation Form -->
		
    		<br>
    	
    		
		</div>
		
		<!-- 
			Train Schedules : Add, Edit, Delete
		 -->
		<div id="cusRepTra" aria-hidden="true">
			Train Schedules
		</div>
	</div>
	</div>

</div>

<jsp:include page="footer.jsp"/>