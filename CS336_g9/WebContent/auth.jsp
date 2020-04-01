<jsp:include page="header.jsp"/>


<!-- Login Form -->
<%
if (request.getParameter("try") != null)
{
%>
<div class = "error box" id = "badLogin">The attempted username/password combo is invalid. Please try again.</div>

<% } %>


<div class="auth" id="login">
	<div class="col-sm-8 col-md-4">
		<form action="Auth" method="get" class="needs-validation" novalidate>
    		<label for="login_userName" class="col-form-label">User Name</label>
      		<input type="text" name="userName" class="form-control" id="login_userName" placeholder="John Doe" required>
      			<div class="invalid-feedback">
        			User name cannot be empty
      			</div>
 
    		<label for="login_assword" class="form-label">Password</label>
      		<input type="password" name="password" class="form-control" id="login_password" placeholder="Password" required>

    			<div class="invalid-feedback">
        			Password cannot be empty
    			</div>
    
    		<button type="submit" class="auth-btn btn btn-primary">Log in</button>
		</form>

		<button class="btn btn-outline-secondary" id="newAccountBtn">Create a new account!</button>
		
	</div>
</div>



<!-- Register Form -->

<div class="auth hide" id="register">
	<div class="col-sm-8 col-md-4" >

<form action="Auth" method="post" class="needs-validation" novalidate>
  
      <label for="firstName">First Name</label>
      <input type="text" name="ufirstName" class="form-control" id="firstName" placeholder="First name" required>
      <div class="invalid-feedback">
          Please enter your first name!
      </div>
    
      <label for="lastName">Last name</label>
      <input type="text" name="ulastName" class="form-control" id="lastName" placeholder="Last name" required>
      <div class="invalid-feedback">
          Please enter your last name!
      </div>

      <label for="userName">Username</label>
        <input type="text" name="uname" class="form-control" id="userName" placeholder="Username" required>
        <div class="invalid-feedback">
          Please choose a username!
        </div>
   

      <label for="password">Password</label>
        <input type="password" name="upassword" class="form-control" id="password" placeholder="Password" required>
        <div class="invalid-feedback">
          Password is required!
        </div>
 
      <label for="email">Email</label>
        <input type="email" name="uemail" class="form-control" id="email" required>
        <div class="invalid-feedback">
          Please enter your email address!
        </div>
        
      <label for="phone">Telephone</label>
        <input type="text" name="uphone" class="form-control" id="phone" required>
        <div class="invalid-feedback">
          Please enter a phone number!
        </div>
 
      <label for="address">Address</label>
      <input type="text" name="uaddress" class="form-control" id="address" required>
      <div class="invalid-feedback">
        Please provide a valid address!
      </div>

      <label for="city">City</label>
      <input type="text" name="ucity" class="form-control" id="city" required>
      <div class="invalid-feedback">
        Please provide a valid city!
      </div>

      <label for="state">State</label>
      <input type="text" name="ustate" class="form-control" id="state" required>
      <div class="invalid-feedback">
        Please provide a valid state!
      </div>
    
      <label for="zip">Zip Code</label>
      <input type="text" name="uzip" class="form-control" id="zip" required>
      <div class="invalid-feedback">
        Please provide a valid zip code!
      </div>

      <button type="submit" class="auth-btn btn btn-primary" id="signinBtn">Sign in</button>
</form>

</div>

</div>


<jsp:include page="footer.jsp"/>