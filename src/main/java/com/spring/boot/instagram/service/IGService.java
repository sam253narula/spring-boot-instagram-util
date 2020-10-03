package com.spring.boot.instagram.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IGService {

	private final String username;
	private final String password;

	private Instagram4j instagram;

	IGService(@Value("${user}") String username, @Value("${password}") String password)
			throws ClientProtocolException, IOException {
		this.username = username;
		this.password = password;
		instagram = Instagram4j.builder().username(username).password(password).build();
		instagram.setup();
		instagram.login();
	}

	Set<String> myFollowerUserNames = new HashSet<String>();
	Set<String> myFollowingUserNames = new HashSet<String>();
	Set<String> theirFollowers = new HashSet<String>();

	public Set<String> getmyFollowers() throws ClientProtocolException, IOException {
		InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
		System.out.println("ID for @github is " + userResult.getUser().getPk());
		System.out.println("Number of followers: " + userResult.getUser().getFollower_count());
		InstagramGetUserFollowersResult githubFollowers = instagram
				.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));

		List<InstagramUserSummary> users = githubFollowers.getUsers();
		int followersListSize = users.size();
		System.out.println(followersListSize);
		for (InstagramUserSummary user : users) {
			myFollowerUserNames.add(user.getUsername());
			System.out.println("User " + user.getUsername() + " follows Github!");
		}
		return myFollowerUserNames;
	}

	public Set<String> getmyFollowings() throws ClientProtocolException, IOException {
		InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
		System.out.println("ID for " + username + " is " + userResult.getUser().getPk());
		InstagramGetUserFollowersResult followings = instagram
				.sendRequest(new InstagramGetUserFollowingRequest(userResult.getUser().getPk()));
		List<InstagramUserSummary> users = followings.getUsers();
		for (InstagramUserSummary user : users) {
			myFollowingUserNames.add(user.getUsername());
		}
		return myFollowingUserNames;
	}

	public Set<String> getUserNamesOfWhoDoNotFollowYouAndYouFollowThem() throws ClientProtocolException, IOException {
		Set<String> followers = getmyFollowers();
		Set<String> following = getmyFollowings();
		Set<String> userNamesOfWhoDoNotFollowYouAndYouFollowThem = new HashSet<String>();
		for (String followingUsername : following) {
			if (!followers.contains(followingUsername)) {
				userNamesOfWhoDoNotFollowYouAndYouFollowThem.add(followingUsername);
			}
		}
		return userNamesOfWhoDoNotFollowYouAndYouFollowThem;
	}

	public String unFollowWhoDoNotFollowYou() throws ClientProtocolException, IOException {
		Set<String> userNamesOfWhoDoNotFollowYouAndYouFollowThem = getUserNamesOfWhoDoNotFollowYouAndYouFollowThem();
		for (String username : userNamesOfWhoDoNotFollowYouAndYouFollowThem) {
			InstagramSearchUsernameResult userResult = instagram
					.sendRequest(new InstagramSearchUsernameRequest(username));
			instagram.sendRequest(new InstagramUnfollowRequest(userResult.getUser().getPk()));
		}

		return "done";
	}

	public String unFollowSomeOne(String username) throws ClientProtocolException, IOException {
		InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
		instagram.sendRequest(new InstagramUnfollowRequest(userResult.getUser().getPk()));
		return username + " unfollowed";
	}

	public String unFollowAlltheseUsernames(List<String> usernames) throws ClientProtocolException, IOException {
		for (String username : usernames) {
			InstagramSearchUsernameResult userResult = instagram
					.sendRequest(new InstagramSearchUsernameRequest(username));
			instagram.sendRequest(new InstagramUnfollowRequest(userResult.getUser().getPk()));
		}

		return "done";
	}

	public String FollowAlltheseUsernames(List<String> usernames) throws ClientProtocolException, IOException {
		for (String username : usernames) {
			InstagramSearchUsernameResult userResult = instagram
					.sendRequest(new InstagramSearchUsernameRequest(username));
			instagram.sendRequest(new InstagramFollowRequest(userResult.getUser().getPk()));
		}

		return "done";
	}

	public Set<String> getThisUsernameFollowings(String usernameofSomeElse)
			throws ClientProtocolException, IOException {
		Set<String> theirFollowings = new HashSet<String>();
		InstagramSearchUsernameResult userResult = instagram
				.sendRequest(new InstagramSearchUsernameRequest(usernameofSomeElse));
		System.out.println("ID for " + usernameofSomeElse + " is " + userResult.getUser().getPk());
		InstagramGetUserFollowersResult followings = instagram
				.sendRequest(new InstagramGetUserFollowingRequest(userResult.getUser().getPk()));
		List<InstagramUserSummary> users = followings.getUsers();
		for (InstagramUserSummary user : users) {
			theirFollowings.add(user.getUsername());
		}

		return theirFollowings;
	}

//	public Set<String> getThisUsernameFollowers(String usernameofSomeElse) throws ClientProtocolException, IOException {
//		
//		InstagramSearchUsernameResult userResult = instagram
//				.sendRequest(new InstagramSearchUsernameRequest(usernameofSomeElse));
//		System.out.println("ID for " + usernameofSomeElse + " is " + userResult.getUser().getPk());
//		String maxId= "";
//		InstagramGetUserFollowersResult followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
//		List<InstagramUserSummary> users;
//		users = followers.getUsers();
//		if (!users.isEmpty()) {
//			for (InstagramUserSummary user : users) {
//				theirFollowers.add(user.getUsername());
//			}
//		}
//		
//		while (followers.next_max_id != null) {
//			followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
//			users = followers.getUsers();
//			if (!users.isEmpty()) {
//				for (InstagramUserSummary user : users) {
//					theirFollowers.add(user.getUsername());
//				}
//			}
//		}
//
//		return theirFollowers;
//	}

}
