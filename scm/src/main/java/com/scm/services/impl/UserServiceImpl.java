package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepository;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	@Override
	public User saveUser(User user) {
		String userId = UUID.randomUUID().toString();

		user.setUserId(userId);

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		user.setRoleList(List.of(AppConstants.ROLE_USER));
		return userRepo.save(user);
	}

	@Override
	public Optional<User> getUserById(String id) {
		return userRepo.findById(id);
	}

	@Override
	public Optional<User> UpdateUser(User user) {

		User userDb = userRepo.findById(user.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

		userDb.setName(user.getName());
		userDb.setEmail(user.getEmail());
		userDb.setPassword(user.getPassword());
		userDb.setAbout(user.getAbout());

		User saveUser = userRepo.save(userDb);
		return Optional.ofNullable(saveUser);

	}

	@Override
	public void deleteUser(String Id) {
		User userDb = userRepo.findById(Id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		userRepo.delete(userDb);

	}

	@Override
	public boolean isUserExist(String userId) {
		User userDb = userRepo.findById(userId).orElse(null);
		return userDb != null ? true : false;
	}

	@Override
	public boolean isUserExistByEmail(String email) {
		User userDb = userRepo.findByEmail(email).orElse(null);
		return userDb != null ? true : false;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email).orElse(null);
	}

}
