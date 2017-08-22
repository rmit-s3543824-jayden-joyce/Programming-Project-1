	var username = document.forms["regForm"]["username"];
	var first_name = document.forms["regForm"]["firstname"];
	var last_name = document.forms["regForm"]["lastname"];
	var age = document.forms["regForm"]["age"];
	var password = document.forms["regForm"]["password"];
	var confirm_Password = document.forms["regForm"]["confirmpassword"];
	
	var username_error = document.getElementById("username_error");
	var first_name_error = document.getElementById("firstname_error");
	var last_name_error = document.getElementById("lastname_error");
	var age_error = document.getElementById("age_error");
	var password_error = document.getElementById("password_error");
	
	username.addEventListener("blur", usernameVerify, true);
	first_name.addEventListener("blur", firstNameVerify, true);
	last_name.addEventListener("blur", lastNameVerify, true);
	age.addEventListener("blur", ageVerify, true);
	password.addEventListener("blur", passwordVerify, true);
	
	function Validate()
	{
		// username validation
		if(isEmpty(username.value) || userExists(username.value))
		{
			username.style.border = "1px solid red";
			username_error.textContent = "Valid username is required";
			username.focus();
			return false;
		}
		// first name validation
		if(isEmpty(firstname.value) || !valName(firstname.value))
		{
			first_name.style.border = "1px solid red";
			first_name_error.textContent = "Must have a capital letter at the start and only contains letters";
			first_name.focus();
			return false;
		}
		// last name validation
		if(isEmpty(lastname/value) || !valName(lastname.value))
		{
			last_name.style.border = "1px solid red";
			last_name_error.textContent = "Must have a capital letter at the start and only contains letters";
			last_name.focus();
			return false;
		}
		// age validation
		if(isEmpty(age.value) || !valAge(age.value))
		{
			age.style.border = "1px solid red";
			age_error.textContent = "Valid age is required";
			age.focus();
			return false;
		}
		// password validation
		if(isEmpty(password.value) || !valPassword(lastname.value))
		{
			password.style.border = "1px solid red";
			password_error.textContent = "Must have a capital letter at the start and only contains letters";
			password.focus();
			return false;
		}
		// check if passwords match
		if(password.value != confirm_password.value)
		{
			password.style.border = "1px solid red";
			confirm_password.style.border = "1px solid red";
			password_error.innerHTML = "Passwords do not match";
			return false;
		}
		
		return true;
	}
	
	function usernameVerify()
	{
		if(!isEmpty(username.value))
		{
			username.style.border = "1px solid black";
			username_error.innerHTML = "";
			return true;
		}	
	}
	
	function firstNameVerify()
	{
		if(!isEmpty(first_name.value))
		{
			first_name.style.border = "1px solid black";
			first_name_error.innerHTML = "";
			return true;
		}	
	}
	
	function lastNameVerify()
	{
		if(!isEmpty(last_name.value))
		{
			last_name.style.border = "1px solid black";
			last_name_error.innerHTML = "";
			return true;
		}	
	}
	
	function ageVerify()
	{
		if(!isEmpty(age.value))
		{
			age.style.border = "1px solid black";
			age_error.innerHTML = "";
			return true;
		}	
	}
	
	function passwordVerify()
	{
		if(!isEmpty(password.value))
		{
			password.style.border = "1px solid black";
			password_error.innerHTML = "";
			return true;
		}	
	}
	
	function usernameVerify()
	{
		if(!isEmpty(username.value))
		{
			//change format to normal
			return true;
		}	
	}
	
	function isEmpty(value)
	{
		if(value == "")
		{
			return true;
		}
		return false;	
	}
	
	function userExists(username)
	{
		var filePath = fileTool.USER_DATA_FILE;
	
		if(fileTool.searchFile(username, filePath) != null){
			return true;
		}
	}
	
	function valName(name)
	{
		var isMatch = name.match(/^[A-Z][a-z]+$/g);
		return isMatch;
	}
	
	function valAge(age)
	{
		if (age > 0 && age <= 99)
		{
			return true;
		}
	}
	
	function valPassword(password)
	{
		var isMatch = password.match(/^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$/g);
		return isMatch;
	}