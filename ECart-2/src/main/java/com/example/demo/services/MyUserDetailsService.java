package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

import jakarta.transaction.Transactional;
@Service
public class MyUserDetailsService implements UserDetailsService {
@Autowired
private UserRepository repository;
	
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		//Fetch the user form DB,based on Email
		User dbUser=repository.findByEmail(email)
				.orElseThrow(()->new UsernameNotFoundException("Please Login with valid Email"));
		//username(email)-maur@gmail.com
		//dbuser-id username,email,password,roles
		//return the UserDetails(email,password,authorities)
//		System.out.println(dbUser);
		
		//return UserDetails (email,password,authorities)
		//id ,username,roles for frontEND ,send through token subject
		//email,password,authorities for SB security
		
		UserDetails user=new User(
				dbUser.getId(),
				dbUser.getUsername(),
				dbUser.getEmail(),
				dbUser.getPassword(),
				dbUser.getRoles(),
				dbUser.getRoles()
				
					.stream()
					.map(role->new SimpleGrantedAuthority(role))
					.toList()
				
				
				
				);
				
				
		
		
		return user;
	}

}
