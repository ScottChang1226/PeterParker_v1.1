import { config, artifact } from './config.js';


/*-------------------------------------------*/
/*Login
/*-------------------------------------------*/


document.getElementById('login_form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    // let formData = new FormData(this); // Collect form data
    // let jsonData = {};
    let errorMessage = document.getElementById('errorMessage');
    let userAccount = document.getElementById('login_user_account');
    let userPassword = document.getElementById('login_user_password');
    let isValid = true;
    let rememberMe = document.getElementById('login_remember_me');

    // Validate inputs
    if (userAccount.value.trim() === "") {
        errorMessage.style.display = "block";
        errorMessage.innerText = "請輸入您的郵件信箱。";
        isValid = false;
        return;
    } else if (userPassword.value.trim() === "") {
        errorMessage.style.display = "block";
        errorMessage.innerText = "請輸入您的帳號密碼。";
        isValid = false;
        return;
    }

    // Convert form data to JSON format
    if (isValid) {
        login();}
});


  
function login(){
    const overlay = document.getElementById('loadingOverlay');
   overlay.style.display = 'block';

    let errorMessage = document.getElementById('errorMessage');
    let userAccount = document.getElementById('login_user_account');
    let userPassword = document.getElementById('login_user_password');
    let isValid = true;
    let rememberMe = document.getElementById('login_remember_me');

    const loginData = {
        userAccount: userAccount.value,
        userPassword: userPassword.value,
        rememberMe: rememberMe.checked // This will be true or false
    };
    
    // Send the form data as JSON using the fetch API
    fetch(`http://${config}/${artifact}/user/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
    .then(response => {
        if (response.ok) {
            return response.json(); // Convert the response to JSON
        } else if (response.status === 404) {
            errorMessage.style.display = "block";
            errorMessage.innerText = "輸入的帳號密碼有誤，請重新輸入。";
            throw new Error('Invalid credentials');
        } else {
            console.error('Unexpected response:', response.status);
            throw new Error(`Unexpected status code: ${response.status}`);
        }
    })
    .then(data => {
        // Check if the jwtToken is present in the response
        if (data.jwtToken) {
            
            localStorage.setItem('peterParkerToken', data.jwtToken);
            localStorage.setItem('userId',data.user_id);
            window.location.href = 'user_center.html'; // Redirect on success
        } else {
            console.error('JWT not found in response');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    })
    .finally(() => {
        // Hide spinner when done
        overlay.style.display = 'none';
    });
};


/*-------------------------------------------*/
/*Registration
/*-------------------------------------------*/


document.getElementById('register_form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission


	let userName_registry = document.getElementById('username_registry');
	let userPhone_registry = document.getElementById('userphone_registry');
	let userAccount_registry=document.getElementById('useraccount_registry');
	let userPassword_registry = document.getElementById('userpassword_registry');
	let carNumber_registry = document.getElementById('carNumber_registry');
	let errorMessage_registry = document.getElementById('errorMessageRegistry');
	let isFilled = true;

    function isValidTaiwanPhoneNumber(phoneNumber) {
        // Regex to match Taiwanese mobile and landline phone numbers
        const taiwanMobilePattern = /^09\d{8}$/; // Mobile format
        const taiwanLandlinePattern = /^(0[2-9])\d{7,8}$/; // Landline format
    
        return taiwanMobilePattern.test(phoneNumber) || taiwanLandlinePattern.test(phoneNumber);
    }


	if(userName_registry.value.trim() === "") {
		errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "請輸入您的名字。";
		isFilled = false;
        return;
	} else if (userPhone_registry.value.trim() === "") {
		errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "請輸入您的電話。";
		isFilled = false;
        return;
	} else if (userAccount_registry.value.trim() === ""){
		errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "請輸入您的郵件信箱。";
		isFilled = false;
        return;
	} else if (userPassword_registry.value.trim()===""){
		errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "請輸入您的密碼。";
		isFilled = false;
        return;
	} else if (!isValidTaiwanPhoneNumber(userPhone_registry.value)){
        errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "輸入電話號碼格式不正確。";
		isFilled = false;
        return;
    }

    // Convert form data to JSON format
   let jsonData_registry = {
    userName: userName_registry.value,
    userPhone: userPhone_registry.value,
    userAccount: userAccount_registry.value,
    userPassword: userPassword_registry.value,
    carNumber: carNumber_registry.value
   };

   const overlay = document.getElementById('loadingOverlay');
   overlay.style.display = 'block';

    // Send the form data as JSON using the fetch API
    if(isFilled){
		fetch(`http://${config}/${artifact}/user/addUser`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(jsonData_registry)
		})
		.then(response => response.json()) // Parse the JSON response
		.then(data => {
			if (data.message === "Verification email successfully sent") {
				// Redirect to the user_center.html page on successful login
				window.location.href = 'user_verify_code.html';} 
				else if(data.message == "User already exists"){
					errorMessage_registry.style.display = 'block';
					errorMessage_registry.innerText = "帳號已註冊，請登入使用。"
			} else { 
				// Handle any error messages (e.g., invalid credentials)
				errorMessage_registry.style.display = 'block';
				errorMessage_registry.innerText = "輸入的帳號密碼有誤，請重新輸入。"
				
			}
		})
		.catch((error) => {
			console.error('Error:', error);
			console.error(JSON.stringify(jsonData));
			errorMessage_registry.style.display = 'block';
			errorMessage_registry.innerText = "系統忙線中，請等候。"
		})
        .finally(() => {
            // Hide spinner when done
            overlay.style.display = 'none';
        });;
	}
	
})



