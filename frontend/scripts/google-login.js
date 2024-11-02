function handleCredentialResponse(response) {
    console.log("Google JWT Token: " + response.credential);

    // Send the Google credential to your backend for verification
    fetch(`http://localhost:8081/PeterParkerSpring/user/googleLogin`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ googleToken: response.credential }),
    })
    .then(response => {
		console.log("response" +response);
        if (response.status == 200) {
            return response.json(); // Convert the response to JSON
        } else if (response.status == 202){
			window.location.href = 'user_google_registration.html';
		} else {  
            console.error('Unexpected response:', response.status);
            throw new Error(`Unexpected status code: ${response.status}`);
        }
    })
    .then(data => {
        // Assuming your backend returns a JWT token after verifying Google sign-in
        if (data.jwtToken) {
            localStorage.setItem('peterParkerToken', data.jwtToken);
            localStorage.setItem('userId',data.userId);
            // window.location.href = 'user_center.html'; // Redirect on success
        } else {
            console.error('JWT not found in response');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}