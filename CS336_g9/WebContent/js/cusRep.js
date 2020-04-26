/**
 * 
 */

class App {

	constructor () {
		this.navLinks = document.querySelectorAll(".nav-link"); 
		this.navLinks[0].classList.add("active");
		this.activeLink = this.navLinks[0];
		this.setupNavLinks();
	}
	
	setupNavLinks() {
		for (const link of this.navLinks){
			link.addEventListener("click", () => {
				this.activeLink.classList.remove("active");
				const oldContentBox = document.querySelector(`#${this.activeLink.dataset.type}`);
				oldContentBox.setAttribute("aria-hidden", "true");
				
				this.activeLink = link;
				link.classList.add("active");
				const newContentBox = document.querySelector(`#${this.activeLink.dataset.type}`);
				newContentBox.setAttribute("aria-hidden", "false");		
			});
		}
	}
	
}

const app = new App();