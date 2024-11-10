package com.tibame.peterparker.controller;

import com.tibame.peterparker.dto.UserFavouriteDTO;
import com.tibame.peterparker.dto.UserFavouriteParkingDTO;
import com.tibame.peterparker.service.UserFavouriteService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserFavouriteController {

    @Autowired
    private UserFavouriteService userFavouriteService;

    @PostMapping("/addFavourite")
    public ResponseEntity<?> addFavourite(@Valid @RequestBody UserFavouriteDTO userFavouriteDTO) {
        Integer userId = userFavouriteDTO.getUserId();
        Integer parkingId = userFavouriteDTO.getParkingId();

        System.out.print("userId from Postman: "+userId);
        System.out.print("parkingId from Postman: "+parkingId);

        userFavouriteService.addUserFavourite(userId, parkingId);
        return ResponseEntity.ok("Favourite parking spot added!");
    }

    @DeleteMapping("/deleteFavourite")
    public ResponseEntity<?> deleteFavourite(@Valid @RequestBody UserFavouriteDTO userFavouriteDTO) {
        Integer userFavouriteId = userFavouriteDTO.getUserFavouriteId();
        userFavouriteService.deleteUserFavourite(userFavouriteId);
        return ResponseEntity.ok("Favourite parking spot removed!");
    }

    @GetMapping("/findAllFavourites")
    public ResponseEntity<?> getUserFavourites(@RequestHeader(value = "Authorization", required = false) String authHeader) {

        String jwtToken = authHeader.substring(7);  // Extract JWT token by removing "Bearer "
        String SECRET_KEY = "TheSecretKeyForOurBelovedProjectPeterParker";

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JWT token not found");
        }

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) // Use the same key for verification
                .parseClaimsJws(jwtToken) // This will throw an exception if the token is invalid
                .getBody();

        // Extract user information from the JWT claims
        String userIdString = claims.getSubject();
        Integer userId = Integer.parseInt(userIdString);


        List<UserFavouriteParkingDTO> favourites = userFavouriteService.findParkingByUserUserId(userId);
        if (favourites.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(favourites);
    }

    @GetMapping("/ifFavouritedAlready/{userId}/{parkingId}")
    public ResponseEntity<Map<String, String>> isFavourited(@PathVariable Integer userId, @PathVariable Integer parkingId) {
         boolean isFavourited = userFavouriteService.findExistingFavouriteId(userId, parkingId);
         Map response = new HashMap();
         String isFavouritedString = String.valueOf(isFavourited);
         response.put("isFavourited", isFavouritedString);
         response.put("FavouriteButton", "addingSection"+parkingId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
