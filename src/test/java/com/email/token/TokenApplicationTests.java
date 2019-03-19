package com.email.token;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@RunWith(SpringRunner.class)
@WebMvcTest
public class TokenApplicationTests {

	// start up only the portions of the application required to perform a local endpoint request
	@Autowired
    private MockMvc mvc;
	
	// perform several http requests to the endpoint and compare the email in the subject of the received jwt token to the one encoded
	// print out the generated token and extracted email for reference
	@Test
	public void testJwtEncodePost() throws Exception {
		// POST tests
		String jwt = mvc.perform(post("/tokenize").param("email", "name@email.com")).andReturn().getResponse().getContentAsString();
		String email = testDecode(jwt);
		Assert.assertEquals(email, "name@email.com");
		jwt = mvc.perform(post("/tokenize").param("email", "mail@gmail.com")).andReturn().getResponse().getContentAsString();
		email = testDecode(jwt);
		System.out.println("Token: " + jwt);
		System.out.println("Embeded email: " + email);
		Assert.assertEquals(email, "mail@gmail.com");
		jwt = mvc.perform(post("/tokenize").param("email", "person@places.net")).andReturn().getResponse().getContentAsString();
		email = testDecode(jwt);
		System.out.println("Token: " + jwt);
		System.out.println("Embeded email: " + email);
		Assert.assertEquals(email, "person@places.net");
		jwt = mvc.perform(post("/tokenize").param("email", "help@wiki.org")).andReturn().getResponse().getContentAsString();
		email = testDecode(jwt);
		System.out.println("Token: " + jwt);
		System.out.println("Embeded email: " + email);
		Assert.assertEquals(email, "help@wiki.org");
		
		
		jwt = mvc.perform(post("/tokenize").param("email", "addressinvalid character@email.com")).andReturn().getResponse().getContentAsString();
		Assert.assertEquals(jwt, "Request is not a valid email address");
		jwt = mvc.perform(post("/tokenize").param("email", "notAnAddress")).andReturn().getResponse().getContentAsString();
		Assert.assertEquals(jwt, "Request is not a valid email address");
		jwt = mvc.perform(post("/tokenize").param("email", "This is a story from long ago. At that time, the languages and letters were quite different from those we use today")).andReturn().getResponse().getContentAsString();
		Assert.assertEquals(jwt, "Request is not a valid email address");
		
	}
	
	@Test
	public void testJwtEncodeGet() throws Exception {
		// GET tests
		String jwt = mvc.perform(get("/tokenize").param("email", "name@email.com")).andReturn().getResponse().getContentAsString();
		String email = testDecode(jwt);
		Assert.assertEquals(email, "name@email.com");
		jwt = mvc.perform(get("/tokenize").param("email", "mail@gmail.com")).andReturn().getResponse().getContentAsString();
		email = testDecode(jwt);
		System.out.println("Token: " + jwt);
		System.out.println("Embeded email: " + email);
		Assert.assertEquals(email, "mail@gmail.com");
		jwt = mvc.perform(get("/tokenize").param("email", "person@places.net")).andReturn().getResponse().getContentAsString();
		email = testDecode(jwt);
		System.out.println("Token: " + jwt);
		System.out.println("Embeded email: " + email);
		Assert.assertEquals(email, "person@places.net");
		jwt = mvc.perform(get("/tokenize").param("email", "help@wiki.org")).andReturn().getResponse().getContentAsString();
		email = testDecode(jwt);
		System.out.println("Token: " + jwt);
		System.out.println("Embeded email: " + email);
		Assert.assertEquals(email, "help@wiki.org");
	}
	
	@Test
	public void testErrorUrlTooShort() throws Exception {
		String jwt = mvc.perform(post("/tokenize").param("email", "urltooshort@e.")).andReturn().getResponse().getContentAsString();
		System.out.println(jwt);
		Assert.assertEquals(jwt, "Request is not a valid email address");
	}
	
	@Test
	public void testErrorDoubleDotinAddress() throws Exception {
		String jwt = mvc.perform(post("/tokenize").param("email", "address..doubledot@email.com")).andReturn().getResponse().getContentAsString();
		System.out.println(jwt);
		Assert.assertEquals(jwt, "Request is not a valid email address");
	}
	
	@Test
	public void testErrorAddressEndsWithDot() throws Exception {
		String jwt = mvc.perform(post("/tokenize").param("email", "addressendswithdot.@email.com")).andReturn().getResponse().getContentAsString();
		System.out.println(jwt);
		Assert.assertEquals(jwt, "Request is not a valid email address");
	}
	
	@Test
	public void testErrorSpecialCharInUrl() throws Exception {
		String jwt = mvc.perform(post("/tokenize").param("email", "specialcharinurl@em&ail.com")).andReturn().getResponse().getContentAsString();
		System.out.println(jwt);
		Assert.assertEquals(jwt, "Request is not a valid email address");
	}
	
	@Test
	public void testErrorInvalidChar() throws Exception {
		String jwt = mvc.perform(post("/tokenize").param("email", "addressinvalid character@email.com")).andReturn().getResponse().getContentAsString();
		System.out.println(jwt);
		Assert.assertEquals(jwt, "Request is not a valid email address");
	}
	
	@Test
	public void testErrorNotAnAddress() throws Exception {
		String jwt = mvc.perform(post("/tokenize").param("email", "notAnAddress")).andReturn().getResponse().getContentAsString();
		System.out.println(jwt);
		Assert.assertEquals(jwt, "Request is not a valid email address");
		jwt = mvc.perform(post("/tokenize").param("email", "This is a story from long ago. At that time, the languages and letters were quite different from those we use today")).andReturn().getResponse().getContentAsString();
		System.out.println(jwt);
		Assert.assertEquals(jwt, "Request is not a valid email address");
	}
	
	// Decode the provided JWT, extract the email from the subject field and return it
	// leave the email null if the decoding fails for any reason (causing the above asserts to fail)
	String testDecode (String jwt) {
		String email = null;
		
		try {
			
		    Jws<Claims> claims = Jwts.parser()
			.setSigningKey("-secret--000000--000000--000000-".getBytes("UTF-8"))
			.parseClaimsJws(jwt);
		    
		    email = claims.getBody().getSubject();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return email;
	}

}
