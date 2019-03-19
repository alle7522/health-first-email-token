package com.email.token;

import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
public class GenerateToken {

	// defines the method as the function invoked when the /tokenize endpoint is accessed
	// accepts an "email" parameter in a POST request and returns a JWT token containing it,
	// signed with a statically defined secret here, but should ideally be generated
	// specifically for the token request in a production environment
	@RequestMapping(value="/tokenize", method=RequestMethod.POST)
	public ResponseEntity<String> emailToJwt(@RequestParam(value = "email") String email) {
		
		// verify that the provided string is a valid email address, using the RFC standard regex
		String pattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
		if (!email.toLowerCase().matches(pattern)) {
			// return a relevant error otherwise
			return ResponseEntity.badRequest().body("Request is not a valid email address");
		}
		
		SecretKey key = null;
		try {
			// generate a default key from the string below for testing
			// TODO: Should be changed out for random key generation before being used in production
			key = Keys.hmacShaKeyFor("-secret--000000--000000--000000-".getBytes("UTF-8"));
			
		} catch (Exception e) {
			// catch the relevant exceptions if they arise so they can be addressed
			// TODO: Log the errors in the chosen logger rather than printing to the console
			e.printStackTrace();
			
			// if an error occurred parsing the key, return an internal server error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		// Build the JWT token with a default HS256 header, default ID, timestamp and expiration 
		// the given email as the subject and the generated key for a signature
		String jwt = Jwts.builder().setSubject(email).signWith(key).compact();
		
		// return the token as a response entity with an http OK status
		return ResponseEntity.ok(jwt);
	}
	
	// A GET passthrough, if that's easier to use.
	@RequestMapping(value="/tokenize", method=RequestMethod.GET)
	public ResponseEntity<String> emailToJwtGet(@RequestParam(value = "email") String email) {
		return emailToJwt(email);
	}
	
}
