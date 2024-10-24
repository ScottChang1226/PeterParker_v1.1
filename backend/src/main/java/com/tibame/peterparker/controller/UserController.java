package com.tibame.peterparker.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.tibame.peterparker.dto.UserChangePasswordRequestDTO;
import com.tibame.peterparker.dto.UserLoginRequestDTO;
import com.tibame.peterparker.dto.UserUpdatePasswordDTO;
import com.tibame.peterparker.dto.UserVerificationCodeDTO;
import com.tibame.peterparker.entity.UserVO;
import com.tibame.peterparker.service.EmailService;
import com.tibame.peterparker.service.ForgetPasswordMailService;
import com.tibame.peterparker.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:5500/")
@RestController
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "Tomcat runs successfully!"; // or the name of your default view
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginRequestDTO loginRequestDTO) {
        Map<String, Object> response = new HashMap<>();

        // Extract the fields from the DTO
        String userAccount = loginRequestDTO.getUserAccount();
        String userPassword = loginRequestDTO.getUserPassword();
        boolean rememberMe = loginRequestDTO.isRememberMe();


        UserVO userVO = userService.findUserByUserAccountAndUserPassword(userAccount, userPassword);

        if (userVO == null) {
            response.put("status", "error");
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        String jwtToken;
        String SECRET_KEY = "TheSecretKeyForOurBelovedProjectPeterParker";

        if (!rememberMe) {
            jwtToken = Jwts.builder()
                    .setSubject(String.valueOf(userVO.getUserId()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour in milliseconds
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // Signing with byte array
                    .compact();
        } else {
            jwtToken = Jwts.builder()
                    .setSubject(String.valueOf(userVO.getUserId()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 1 week in milliseconds
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // Signing with byte array
                    .compact();
        }

        response.put("jwtToken", jwtToken);
        response.put("user_account", userVO.getUserAccount());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/googleLogin")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> googleTokenObject) {
        String googleToken = googleTokenObject.get("googleToken");
        Map<String, Object> response = new HashMap<>();

        final String SECRET_KEY = "TheSecretKeyForOurBelovedProjectPeterParker"; // Consider moving this to application properties
        final String CLIENT_ID = "690875404460-brvmd1jtk6sfkl5jb2vhsmbc481b4u72.apps.googleusercontent.com";
        String jwtToken = null;

        try {
            // Setup Google ID Token verifier
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID)) // Set your client ID here
                    .build();

            if(googleToken == null || googleToken.isBlank()) {
                response.put("status", "error");
                response.put("message", "IdToken is empty");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Verify and parse the ID token
            GoogleIdToken idToken = verifier.verify(googleToken);


            if (idToken == null) {
                response.put("error", "Invalid ID token");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            // Extract payload from the verified ID token
            GoogleIdToken.Payload payload = idToken.getPayload();
            String googleId = payload.getSubject();  // User's unique Google ID
            String userAccount = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String userName = (String) payload.get("name");

            // Check if user exists in the system
            boolean userTaken = userService.isUserAccountTaken(userAccount);
            if (!userTaken) {
                // User does not exist, ask for more information
                response.put("status", "need more information");
                return new ResponseEntity<>(response, HttpStatus.CONTINUE);
            }

            // Retrieve existing user information
            UserVO existingUser = userService.findByUserAccount(userAccount);
            if (existingUser == null) {
                response.put("error", "User not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Link Google account if not already linked
            String existingGoogleToken = existingUser.getGoogleToken();
            if (existingGoogleToken == null) {
                userService.updateGoogleToken(userAccount, googleToken);
            }

            // Generate JWT token for future authentication
            jwtToken = Jwts.builder()
                    .setSubject(String.valueOf(existingUser.getUserId()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour in milliseconds
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // Signing with byte array
                    .compact();

        } catch (GeneralSecurityException | IOException e) {
            response.put("error", "Token verification failed");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return the JWT token to the client
        return ResponseEntity.ok(Map.of("jwtToken", jwtToken));
    }

    @PostMapping("/addUser")
    public ResponseEntity<Map<String, Object>> AddUser(@Valid @RequestBody UserVO userVO) {
        boolean userTaken = userService.isUserAccountTaken(userVO.getUserAccount());

        Map<String, Object> response = new HashMap<>();

        if (userTaken) {
            response.put("status", "error");
            response.put("message", "User already exists");
            // Return 409 Conflict with JSON response
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }

        String verificationCode = String.valueOf((int) (Math.random() * 10000));

        try (Jedis jedis = new Jedis()) {
            Gson gson = new Gson(); //Creates an instance of the Gson library.
            String userJson = gson.toJson(userVO);  //This line converts the userVO object into a JSON string representation.
            // Store the JSON string in Redis with a key
            jedis.setex("code", 1800, verificationCode);
            jedis.setex("pendingUser", 1800, userJson);
            System.out.println("code added to:" + jedis.get("code"));
            System.out.println("pending User added to jedis: " + jedis.get("pendingUser"));
        }

        EmailService emailService = new EmailService();
        emailService.sendMail(userVO.getUserAccount(), verificationCode);

        response.put("status", "success");
        response.put("message", "Verification email successfully sent");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/verifyCode")
    public ResponseEntity<Map<String, Object>> VerifyCode(@Valid @RequestBody UserVerificationCodeDTO verificationCodeDTO) {
        String code;
        String pendingUserString;
        Map<String, Object> response = new HashMap<>();

        try (Jedis jedis = new Jedis("localhost", 6379)) {
            pendingUserString = jedis.get("pendingUser");
            code = jedis.get("code");
        }

        if (pendingUserString == null) {
            response.put("message", "Pending user not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Gson gson = new Gson();
        UserVO pendingUser = gson.fromJson(pendingUserString, UserVO.class);

        if (verificationCodeDTO.getCode().trim().equals(code.trim())) {
            try {
                userService.insertUser(pendingUser);
            } catch (Exception e) {
                response.put("Error adding user: ", e);
                response.put("message", "Failed to add user");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.put("message", "User added successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (code == null) {
            response.put("message", "Code Expired. Please re-register");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (!verificationCodeDTO.getCode().equals(code)) {
            response.put("message", "Code unmatched");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        response.put("message", "Unknown error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> UserInfo(@RequestHeader(value = "Authorization", required = false) String authHeader){
        Map<String, Object> response = new HashMap<>();
        String jwtToken = authHeader.substring(7);  // Extract JWT token by removing "Bearer "
        String SECRET_KEY = "TheSecretKeyForOurBelovedProjectPeterParker";

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("status", "error");
            response.put("message", "Invalid Authorization header");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) // Use the same key for verification
                .parseClaimsJws(jwtToken) // This will throw an exception if the token is invalid
                .getBody();

        // Extract user information from the JWT claims
        String userIdString = claims.getSubject();
        Integer userId = Integer.parseInt(userIdString);

        // Create JSON response with user details
        UserVO userVO = userService.findUserByUserId(userId);

        response.put("user_account", userVO.getUserAccount());
        response.put("user_name", userVO.getUserName());
        response.put("user_phone", userVO.getUserPhone());
        response.put("user_id", userId);
        response.put("car_number", userVO.getCarNumber());
        response.put("user_password", userVO.getUserPassword());

        return new ResponseEntity<>(response, HttpStatus.OK);}

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> UpdateUser(@Valid @RequestBody UserVO userVO) {
        Map<String, Object> response = new HashMap<>();
        userService.updateUser(userVO);
        response.put("status", "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/requestPasswordReset")
    public ResponseEntity<Map<String, Object>> ChangePasswordRequest (@Valid @RequestBody UserChangePasswordRequestDTO changePasswordRequestDTO) {
        Map<String, Object> response = new HashMap<>();

        boolean userTaken = userService.isUserAccountTaken(changePasswordRequestDTO.getUserAccount());

        System.out.println(changePasswordRequestDTO.getUserAccount());
        if(!userTaken){
            response.put("error", "user did not register");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        long expirationTime = 30 * 60 * 1000; // 30 minutes in milliseconds
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        String secretKey = "ResetPasswordSecretKeyForOurBelovedProjectPeterParker";
        String userAccount = changePasswordRequestDTO.getUserAccount();

        // Generate JWT for password reset link
        String passwordResetCode = Jwts.builder()
                .setSubject(userAccount) // Set the user email as the subject
                .setIssuedAt(now)
                .setExpiration(expiryDate) // Set expiration time
                .signWith(SignatureAlgorithm.HS256, secretKey) // Sign with the secret key
                .compact();

        // Send the reset password link email
        ForgetPasswordMailService forgetPasswordMailService = new ForgetPasswordMailService();
        forgetPasswordMailService.sendMail(userAccount, passwordResetCode);

        response.put("status", "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<Map<String, Object>> UpdatePassword(@Valid @RequestBody UserUpdatePasswordDTO updatePasswordDTO) {
        Map<String, Object> response = new HashMap<>();
        userService.updatePassword(updatePasswordDTO);
        response.put("status", "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
