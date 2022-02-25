const URL = "http://localhost:1947/forum/User"
const tableEl = document.querySelector("table");
const formEditEl = document.querySelector("form#formEdit");
tableEl.addEventListener("click", async(e)=> {
	if(e.target.tagName === "IMG") {
		let userIdData = e.target.dataset.userid;

		if(e.target.alt ==="edit"){
			let nomData = e.target.dataset.nom;
			let prenomData = e.target.dataset.prenom;
			let telData = e.target.dataset.tel;

			formEditEl.style = "display:block;";
			formEditEl.nom.value=nomData;
			formEditEl.prenom.value=prenomData;
			formEditEl.tel.value=telData;

			formEditEl.addEventListener("submit", async(evt)=> {
				evt.preventDefault();
				formEditEl.style = "display:none;";
				let nomEdit = evt.target.nom.value
				let prenomEdit = evt.target.prenom.value
				let telEdit = evt.target.tel.value
				try{
					await fetch(URL, {
					method: 'PUT',
					headers:{
					'Content-Type':'application/json'
					},
					body: JSON.stringify({"userId":userIdData, "nom":nomEdit,"prenom": prenomEdit,"tel": telEdit})
					})
					window.location.replace(URL);

				}catch(e){
					console.log(e)
				}
				
			})
		} else if(e.target.alt ==="trash"){
			try {
				 await fetch(`${URL}?userId=${userIdData}`, {
				method:'DELETE'})
				window.location.replace(URL);

			}catch(e){
				console.error(e)	
			}
				
		}
	}
})