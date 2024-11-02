// Ajax to show the photo
import { config, artifact } from './config.js';

document.addEventListener('DOMContentLoaded', function() {
    const peterParkerToken = localStorage.getItem('peterParkerToken');

    if (!peterParkerToken) {
        console.error('JWT token not found, redirecting to login');
        window.location.href = 'index.html';
        localStorage.setItem('originalUrl', window.location.href);
        return;
    }

    fetch(`http://${config}/${artifact}/user/showPhoto`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${peterParkerToken}`
        }
    })
    .then(response => {
        if (response.ok) {
            return response.blob(); // Convert the response to a blob
        } else if (response.status == 404){
            console.log("Photo not found, but this is normal if no photo is uploaded yet.");
            return null; // Return null if photo is not found
        }
        else {
            throw new Error('Failed to load image');
        }
    })
    .then(blob => {
        if(blob){ const imgURL = URL.createObjectURL(blob); // Create a local URL for the blob
            const avatarElement = document.getElementById('avatar-preview');
            avatarElement.src = imgURL;}
    })
    .catch(error => {
        console.error('Error fetching profile photo:', error);
    });
});