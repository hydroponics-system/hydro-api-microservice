package com.hydro.insite_user_microservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.hydro.common.dictionary.data.User;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.insite_user_microservice.client.domain.request.UserGetRequest;
import com.hydro.insite_user_microservice.dao.UserProfileDAO;

/**
 * User Service class that handles all service calls to the dao
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Service
public class UserProfileService {

	@Autowired
	private JwtHolder jwtHolder;

	@Autowired
	private UserProfileDAO dao;

	/**
	 * Get users based on given request filter
	 * 
	 * @param request of the user
	 * @return User profile object {@link User}
	 * @throws Exception
	 */
	public List<User> getUsers(UserGetRequest request) {
		List<User> users = dao.getUsers(request);
		return users;
	}

	/**
	 * Get the current user from the jwt token.
	 * 
	 * @return User profile object {@link User}
	 * @throws Exception
	 */
	public User getCurrentUser() throws Exception {
		return getUserById(jwtHolder.getUserId());
	}

	/**
	 * Service to get a users profile given the user id
	 * 
	 * @param id of the user
	 * @return User profile object {@link User}
	 * @throws Exception
	 */
	public User getUserById(int id) throws Exception {
		return dao.getUserById(id);
	}

	/**
	 * This will check to see if the email exists. If it does then it will return
	 * true, otherwise false.
	 * 
	 * @param email The email to check
	 * @return {@link Boolean} to see if the email exists
	 * @throws Exception
	 */
	public boolean doesEmailExist(String email) {
		UserGetRequest request = new UserGetRequest();
		request.setEmail(Sets.newHashSet(email));
		List<User> users = getUsers(request);

		return users.size() > 0;
	}
}
