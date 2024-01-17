/*Starting SideBar JS*/

const sidebar = document.querySelector(".sidebar_left");
const menu = document.querySelector("#menu");

const main = document.querySelector(".main");

const menu_container = document.querySelector(".menu_container");
const logout_container = document.querySelector(".logout_container");

const icon_logout = document.querySelector(".icon_logout");
const listItems = document.querySelectorAll(".current");
const home = document.querySelector("#home");
const viewContact = document.querySelector("#viewContact");
const addContact = document.querySelector("#addContact");
const profile = document.querySelector("#profile");
const settings = document.querySelector("#settings");

let previousToggled = null;
let currentToggled = null;

home.addEventListener("click", (e) => {
	toggleMenu(home);
});

viewContact.addEventListener("click", (e) => {
	toggleMenu(viewContact);
});

addContact.addEventListener("click", (e) => {
	toggleMenu(addContact);
});

profile.addEventListener("click", (e) => {
	toggleMenu(profile);
});

settings.addEventListener("click", (e) => {
	toggleMenu(settings);
});

const toggleMenu = (button) => {
	if (previousToggled && button !== menu) {
		untoggleMenu(previousToggled);
	}

	button.classList.add("toggled");
	button.style.backgroundColor = "#8ed7c6";

	if (button !== menu) {
		previousToggled = button;
	}
};

const untoggleMenu = (button) => {
	button.classList.remove("toggled");
	button.style.backgroundColor = "#18c29c";
};

menu.addEventListener("click", (e) => {
	sidebar.classList.contains("active") ? closeMenu() : openMenu();
});


const openMenu = () => {
	sidebar.classList.add("active");
	sidebar.style.width = "250px";

	toggleMenu(menu);

	let menu_logo = document.createElement("h5");
	menu_logo.id = "menu_logo";
	menu_logo.innerText = "InfoSphere";
	menu_logo.style.marginTop = "8px";
	menu_container.insertBefore(menu_logo, menu_container.childNodes[0]);

	let p_home = document.createElement("p");
	p_home.id = "p_home";
	p_home.innerHTML = "Home";
	home.style.width = "220px";
	home.style.justifyContent = "left";
	home.appendChild(p_home);

	let p_viewContact = document.createElement("p");
	p_viewContact.id = "p_viewContact";
	p_viewContact.innerHTML = "View Contact";
	viewContact.style.width = "220px";
	viewContact.style.justifyContent = "left";
	viewContact.appendChild(p_viewContact);

	let p_addContact = document.createElement("p");
	p_addContact.id = "p_addContact";
	p_addContact.innerHTML = "Add Contact";
	addContact.style.width = "220px";
	addContact.style.justifyContent = "left";
	addContact.appendChild(p_addContact);

	let p_profile = document.createElement("p");
	p_profile.id = "p_profile";
	p_profile.innerHTML = "Profile";
	profile.style.width = "220px";
	profile.style.justifyContent = "left";
	profile.appendChild(p_profile);

	let p_settings = document.createElement("p");
	p_settings.id = "p_settings";
	p_settings.innerHTML = "Settings";
	settings.style.width = "220px";
	settings.style.justifyContent = "left";
	settings.appendChild(p_settings);

	icon_logout.style.width = "25%";

	let user_container = document.createElement("div");
	user_container.id = "user_container";

	let user_name = document.createElement("p");
	user_name.id = "user_name";
	user_name.innerHTML = "LogOut";

	user_container.appendChild(user_name);

	logout_container.insertBefore(user_container, logout_container.childNodes[0]);

	let logout_photo = document.createElement("img");
	logout_photo.id = "logout_photo";
	logout_photo.src = "/images/defaultProfile.png";
	logout_container.style.paddingLeft = "15px";
	logout_container.insertBefore(logout_photo, logout_container.childNodes[0]);

	main.style.width = "calc(100% - 250px)";
};

const closeMenu = () => {
	menu_container.removeChild(document.getElementById("menu_logo"));
	menu_container.style.paddingLeft = "0px";

	untoggleMenu(menu);

	home.removeChild(document.getElementById("p_home"));
	home.style.width = "50px";
	home.style.justifyContent = "center";

	viewContact.removeChild(document.getElementById("p_viewContact"));
	viewContact.style.width = "50px";
	viewContact.style.justifyContent = "center";

	addContact.removeChild(document.getElementById("p_addContact"));
	addContact.style.width = "50px";
	addContact.style.justifyContent = "center";

	profile.removeChild(document.getElementById("p_profile"));
	profile.style.width = "50px";
	profile.style.justifyContent = "center";

	settings.removeChild(document.getElementById("p_settings"));
	settings.style.width = "50px";
	settings.style.justifyContent = "center";

	logout_container.removeChild(document.getElementById("logout_photo"));
	logout_container.removeChild(document.getElementById("user_container"));
	logout_container.style.paddingLeft = "0px";

	icon_logout.style.width = "100%";

	sidebar.classList.remove("active");
	sidebar.style.width = "78px";

	main.style.width = "calc(100% - 78px)";
};

/*Ending SideBar JS*/


// Search JS
const search = () => {

	let query = document.querySelector("#search-input").value;
	if (query == '') {
		document.querySelector(".search-result").style.display = 'none';
	}
	else {
		//console.log(query);

		let url = `http://localhost:9090/search/${query}`;

		fetch(url).then(Response => {
			return Response.json();
		}).then(data => {

			let text = `<div class="list-group">`;

			data.forEach(contact => {

				text += `<a href="/user/contact/${contact.cId}" class="list-group-item list-group-action">${contact.name}</a>`

			});

			text += "</div>";

			document.querySelector(".search-result").innerHTML = text;
			if (data.length > 0) {
				document.querySelector(".search-result").style.display = 'block';
			}
			else {
				document.querySelector(".search-result").style.display = 'none';
			}
		})

	}
}

