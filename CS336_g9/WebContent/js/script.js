/**
 * 
 */
document.addEventListener("DOMContentLoaded",  function() {
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
     * Customer Representative Page
     * 
     * */
    function toggleBtn(btnIdentifier){
    	const btns = document.querySelectorAll(btnIdentifier)
    	
    	for (const btn of btns) {
    		if(btn == null)
    			console.log("Cant find element " + btnIdentifier)
    			
    		btn.addEventListener('click', function (e) {
    			const target = document.querySelector(btn.getAttribute('aria-controls'));
    			if(target.getAttribute('aria-hidden') === 'true')
    				target.setAttribute('aria-hidden', 'false');
    			else
    				target.setAttribute('aria-hidden', 'true');
    		});
    	}
    }


    function verticalNavItemToggle() {
    	const currBtns = document.querySelectorAll('.toggle-btn')
    	for(let btn of currBtns) {
        	btn.addEventListener('click', function (e) {
        		const parent = btn.closest("ul");
            	// deactivate previous active button
            	document.querySelector(parent.getAttribute('data-active')).setAttribute('aria-hidden', 'true');
            	// activate new active button
            	let active = btn.getAttribute('href');
            	parent.setAttribute('data-active', active);
                document.querySelector(active).setAttribute('aria-hidden', 'false');
            });
    	}
    }
    
    verticalNavItemToggle();
    toggleBtn(".single-toggle-btn")
    
    
    /* 
     *  Handler sign in button
     * 
     * */
    const newAccountBtn = document.querySelector('#newAccountBtn');
    const registerForm = document.querySelector('#register');
    const loginForm = document.querySelector("#login");
    const error = document.querySelector("#badLogin");
  
    newAccountBtn.addEventListener('click', function(){
    	loginForm.classList.add("hide");
    	registerForm.classList.remove("hide");
    	error.classList.add("hide");}, false);
   
    
  }, false);

});




