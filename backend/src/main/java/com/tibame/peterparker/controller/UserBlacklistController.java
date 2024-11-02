package com.tibame.peterparker.controller;

import com.tibame.peterparker.dto.UserBlacklistDTO;
import com.tibame.peterparker.dto.UserBlacklistParkingDTO;
import com.tibame.peterparker.service.UserBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserBlacklistController {

    @Autowired
    private UserBlacklistService userBlacklistService;

    @GetMapping("/addBlacklist")
    public ResponseEntity<?> addBlacklist(@Valid @RequestBody UserBlacklistDTO userBlacklistDTO) {
        Integer userId = userBlacklistDTO.getUserId();
        Integer parkingId = userBlacklistDTO.getParkingId();

        System.out.print("userId from Postman: "+userId);
        System.out.print("parkingId from Postman: "+parkingId);

        userBlacklistService.addUserBlacklist(userId, parkingId);
        return ResponseEntity.ok("Blacklist parking spot added!");
    }

    @DeleteMapping("/deleteBlacklist")
    public ResponseEntity<?> deleteBlacklist(@Valid @RequestBody UserBlacklistDTO userBlacklistDTO) {
        Integer userBlacklistId = userBlacklistDTO.getUserBlacklistId();
        userBlacklistService.deleteUserBlacklist(userBlacklistId);
        return ResponseEntity.ok("Blacklist parking spot removed!");
    }

    @GetMapping("/findAllBlacklist")
    public ResponseEntity<?> getUserBlacklist(@RequestHeader(value = "Authorization", required = false) String authHeader) {

        String jwtToken = authHeader.substring(7);  // Extract JWT token by removing "Bearer "
        String SECRET_KEY = "TheSecretKeyForOurBelovedProjectPeterParker";

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JWT token not found");
        }

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) // Use the same key for verification
                .parseClaimsJws(jwtToken) // This will throw an exception if the token is invalid
                .getBody();

        // Log parsed claims for debugging
        System.out.println("Parsed Claims: " + claims);

        // Extract user information from the JWT claims
        String userIdString = claims.getSubject();
        Integer userId = Integer.parseInt(userIdString);


        List<UserBlacklistParkingDTO> favourites = userBlacklistService.findParkingByUserUserId(userId);

        if (favourites.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        return ResponseEntity.ok(favourites);
    }
}
