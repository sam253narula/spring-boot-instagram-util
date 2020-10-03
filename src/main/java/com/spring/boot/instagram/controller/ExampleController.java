package com.spring.boot.instagram.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.boot.instagram.service.IGService;

@RestController
@RequestMapping("/hello")
public class ExampleController {
	
	@Autowired
	IGService igService;

	@GetMapping("/myFollowers")
	public Set<String> getmyFollowers() throws ClientProtocolException, IOException {
		Set<String> followerUserNames = igService.getmyFollowers();
		return followerUserNames;
	}
	
	@GetMapping("/myFollowings")
	public Set<String> getmyFollowings() throws ClientProtocolException, IOException {
		Set<String> followingUserNames = igService.getmyFollowings();
		return followingUserNames;
	}
	
//	@GetMapping("/getPoepleWhoIMFollowing")
//	public Set<String> getFollowing() throws ClientProtocolException, IOException {
//		Set<String> followingUserNames = igService.getmyFollowings();
//		return followingUserNames;
//	}
	
	@GetMapping("/unFollowWhoDoNotFollowYou")
	public String unFollowWhoDoNotFollowYou() throws ClientProtocolException, IOException {
		return igService.unFollowWhoDoNotFollowYou();
	}
	
	@PostMapping("/unfollowUsername")
	public String unfollowUsername(String username) throws ClientProtocolException, IOException {
		return igService.unFollowSomeOne(username);
	}
	
	@PostMapping("/unFollowAlltheseUsernames")
	public String unFollowAlltheseUsernames(@RequestBody List<String> usernames) throws ClientProtocolException, IOException {
		return igService.unFollowAlltheseUsernames(usernames);
	}
	
	@GetMapping("/thisUsernameFollowings")
	public Set<String> getThisUsernameFollowings(String usernameofSomeElse) throws ClientProtocolException, IOException {
		Set<String> followingUserNames = igService.getThisUsernameFollowings(usernameofSomeElse);
		return followingUserNames;
	}
	
//	@PostMapping("/thisUsernameFollowers")
//	public Set<String> getThisUsernameFollowers(String usernameofSomeElse) throws ClientProtocolException, IOException {
//		Set<String> userNamesOfFollowers = igService.getThisUsernameFollowers(usernameofSomeElse);
//		return userNamesOfFollowers;
//	}
	
	@PostMapping("/followAlltheseUsernames")
	public String FollowAlltheseUsernames(@RequestBody List<String> usernames) throws ClientProtocolException, IOException {
		return igService.FollowAlltheseUsernames(usernames);
	}
}
