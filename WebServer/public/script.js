window.addEventListener("load", function(){

	// Make Collapsibles functional
	let collapsibles = document.getElementsByClassName("collapsible");
	for(let i = 0; i < collapsibles.length; i++){
		collapsibles[i].addEventListener("click", function(){
			this.classList.toggle("active");
			var content = this.nextElementSibling;
			if(content.style.display === "block"){
				content.style.display = "none";
			}else{
				content.style.display = "block";
			}
		});
	}

	// Replace Date
	let year = new Date().getFullYear();
	let years = document.getElementsByClassName("year");
	for(let i = 0; i < years.length; i++){
		years[i].innerHTML = year;
	}

});

// Loader
function loadHTML(href, element){
	var request = new XMLHttpRequest();
	request.open("GET", href, true);
	request.send(null);
	request.onload = function(e){
		if(request.readyState === 4){
			if(request.status === 200){
				element.innerHTML = request.responseText;
			}
		}
	}
}

// Copy to Clipboard
function copy(element, text) {
	let temporary = element.getAttribute("switch");
	element.setAttribute("switch", element.innerHTML);
	element.innerHTML = temporary;
	if(element.classList.contains("active")){
		element.classList.remove("active");
	}else{
		let pseudo = document.createElement("input");
		pseudo.value = text;
		document.body.appendChild(pseudo);
		pseudo.select();
		document.execCommand("copy");
		pseudo.parentElement.removeChild(pseudo);
		element.classList.add("active");
	}
}
