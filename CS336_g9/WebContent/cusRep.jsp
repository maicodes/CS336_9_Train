<jsp:include page="header.jsp"/>

<!-- This web page implements Customer Representative functionalities -->

<div id="cusRep">
	<div class="row">
	<div class="col-2 vertical-nav-con">
	<ul class="vertical-nav" data-active="#cusRepRes">
		<li class="vertical-nav-item"><a href="#cusRepRes" class="toggle-btn btn btn-outline-light">Reservations</a></li>
		<li class="vertical-nav-item"><a href="#cusRepMes" class="toggle-btn btn btn-outline-light">Messages</a></li>
		<li class="vertical-nav-item"><a href="#cusRepTra" class="toggle-btn btn btn-outline-light">Train Schedules</a></li>
	</ul>
	</div>

	<div class="col-8" class="contentBox">
		<!-- 
			Reservations : Add, Edit, Delete
		 -->
		<div id="cusRepRes" aria-hidden="false">
    		<button class="single-toggle-btn btn btn-outline-primary">Add New Reservation</button>
    		<br>
    		<h3>List of all reservations:</h3>
    		
		</div>
		
		<div id="cusRepMes" aria-hidden="true">
			Messages
		</div>
		
		<div id="cusRepTra" aria-hidden="true">
			Train Schedules
		</div>
	</div>
	</div>

</div>

<jsp:include page="footer.jsp"/>