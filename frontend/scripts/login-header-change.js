import { config, artifact } from './config.js';

document.addEventListener("DOMContentLoaded", function () {
    // Check if the JWT token exists in local storage
    const peterParkerToken = localStorage.getItem("peterParkerToken");

    if (peterParkerToken) {
        // Get the header element
        const headerContainer = document.getElementById("header-container");

        // Update the header's inner HTML with the logged-in content
        headerContainer.innerHTML = `
	<div id="header" class="not-sticky">
		<div class="container">
			
			<!-- Left Side Content -->
			<div class="left-side">
				
				<!-- Logo -->
				<div id="logo">
					<a href="index.html"><img src="images/logo.png" alt=""></a>
					<a href="index.html" class="dashboard-logo"><img src="images/logo2.png" alt=""></a>
				</div>

				<!-- Mobile Navigation -->
				<div class="mmenu-trigger">
					<button class="hamburger hamburger--collapse" type="button">
						<span class="hamburger-box">
							<span class="hamburger-inner"></span>
						</span>
					</button>
				</div>

				<!-- Main Navigation -->
				<nav id="navigation" class="style-1">
					<ul id="responsive">

						<li><a href="index.html">首頁</a>
						</li>

						<li><a>如何使用</a>
							<ul>
								<li><a href="how_to_reserve.html">如何預約車位</a></li>
								<li><a href="dashboard-messages.html">如何出租車位</a></li>
							</ul>
						</li>

						<li><a>最新消息</a>
							<ul>
								<li><a href="dashboard.html">Dashboard</a></li>
								<li><a href="dashboard-messages.html">Messages</a></li>
								<li><a href="dashboard-bookings.html">Bookings</a></li>
								<li><a href="dashboard-wallet.html">Wallet</a></li>
								<li><a href="dashboard-my-listings.html">My Listings</a></li>
								<li><a href="dashboard-reviews.html">Reviews</a></li>
								<li><a href="dashboard-bookmarks.html">Bookmarks</a></li>
								<li><a href="dashboard-add-listing.html">Add Listing</a></li>
								<li><a href="dashboard-my-profile.html">My Profile</a></li>
								<li><a href="dashboard-invoice.html">Invoice</a></li>
							</ul>
						</li>

						<li><a href="#">聯絡我們</a></li>

						<li><a href="#">業主登入</a></li>
							
						
					</ul>
				</nav>
				<div class="clearfix"></div>
				<!-- Main Navigation / End -->
				
			</div>
			<!-- Left Side Content / End -->

			<!-- Right Side Content / End-->
			<div class="right-side">
				<!-- Header Widget -->
				<div class="header-widget">
					<!-- User Menu -->
					<div class="user-menu">
						<div class="user-name"><span><img id="avatar-preview" src="images/dashboard-avatar.jpg" alt=""></span><p style="display:inline "id="username2"></p></div>
						<ul>
							<li><a href="user_center.html"><i class="sl sl-icon-settings"></i>會員中心</a></li>
							<li><a href="user_profile.html"><i class="sl sl-icon-user"></i>會員個人資料</a></li>
						</ul>
					</div>
					<a href="index.html" id="logout_btn" class="button border with-icon" onclick="localStorage.removeItem('peterParkerToken'); localStorage.removeItem('userId');"></i>會員登出</a>
					</div>
				</div>
				</div>
				<!-- Header Widget / End -->
			</div>
			<!-- Right Side Content / End -->

		</div>
	</div>`;
		headerContainer.classList.add('dashboard');


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

        fetch(`http://${config}/${artifact}/user/showPhoto`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${peterParkerToken}`
            }
        })
        .then(response => {
            if (response.ok) {
                return response.blob(); // Convert the response to a blob
            } else if (response.status === 404){
				return "no photo found";
			}
			else {
                throw new Error('Failed to load image');
            }
        })
        .then(blob => {
            const imgURL = URL.createObjectURL(blob); // Create a local URL for the blob
            const avatarElement = document.getElementById('avatar-preview');
            avatarElement.src = imgURL;
        })
        .catch(error => {
            console.error('Error fetching profile photo:', error);
        });
}
});