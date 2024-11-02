import { config, artifact } from './config.js';


	document.addEventListener('DOMContentLoaded', function() {
    const peterParkerToken = localStorage.getItem('peterParkerToken');

	
    if (!peterParkerToken) {
        console.error('JWT token not found, redirecting to login');
        window.location.href = 'index.html';  // Redirect to login if token is missing
        return;
    }

    fetch(`http://${config}/${artifact}/user/userinfo`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${peterParkerToken}`,  // Include JWT in the Authorization header
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json(); // Parse the JSON response
        } else if(response === 500){
            localStorage.removeItem('peterParkerToken');
            localStorage.removeItem('userId');
            window.location.href = 'index.html';
            
    }   else {
            return response.json().then(err => {
                console.error('Error response:', err); // Log error response for debugging
                localStorage.removeItem('peterParkerToken');
                localStorage.removeItem('userId');
                window.location.href = 'index.html';
            });
        }
    })
    .then(data => {   
       // Get the element by its ID
       const userNameDiv = document.getElementById('username2');
        
       // Update only the text, keeping the existing content intact
       userNameDiv.innerText = `${data.user_name} 你好！`; // Set new text
    })
    .catch(error => {
        console.error('Error:', error);
        if (error.message.includes('token')) {
            localStorage.removeItem('peterParkerToken');
            localStorage.removeItem('userId');
            window.location.href = 'index.html'
        }
    });
});
