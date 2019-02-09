window.onload = function(){
	let collapsibles = document.getElementsByClassName("collapsible");
	
	for(let i = 0; i < collapsibles.length; i++){
		collapsibles[i].addEventListener("click", function(){
			this.classList.toggle("active");
			console.log(this);
			var content = this.nextElementSibling;
			if(content.style.display === "block"){
				content.style.display = "none";
			}else{
				content.style.display = "block";
			}
		});
	}
	
	let year = new Date().getFullYear();
	let years = document.getElementsByClassName("year");
	for(let i = 0; i < years.length; i++){
		years[i].innerHTML = year;
	}
}