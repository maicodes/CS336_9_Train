/**
 * 
 */

(function() {
  window.addEventListener('load', function() {
	  
    /* 
     * Handle log in/sign in validation on client side
     * 
     * */
    var forms = document.getElementsByClassName('needs-validation');
    	// Loop over them and prevent submission
    var validation = Array.prototype.filter.call(forms, function(form) {
      form.addEventListener('submit', function(event) {
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
      }, false);
    });
    
    
    /* 
     *  Handler sign in button
     * 
     * */
    const newAccountBtn = document.querySelector('#newAccountBtn');
    const registerForm = document.querySelector('#register');
    const loginForm = document.querySelector("#login");
    
    const newAccountBtnHandler = () => {
    	loginForm.classList.add("hide");
    	registerForm.classList.remove("hide");
    };
    newAccountBtn.onclick = newAccountBtnHandler;
    
  }, false);
 
})();

