package com.derdiedas.service;

import com.derdiedas.model.User;
import com.derdiedas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

	private final UserRepository userRepository;
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public User findByEmail(String login) {
		return userRepository.findByEmail(login);
	}

	public List<User> findAllPaged(int page, int size) {
		return userRepository.findAll(PageRequest.of(page, size)).getContent();
	}
}
