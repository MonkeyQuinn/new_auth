package org.example.new_auth.service.user;

import org.example.new_auth.domain.User;

import java.util.List;

public interface UserQueryService {

    User getUserById(Long id);

    User getUserByUsername(String username);

    User saveUser(User user);

    List<User> filterUsersByUsernames(List<User> users, List<String> usernames);

    List<String> extractAreasFromUsers(List<User> users);

    List<String> extractOperationsFromUsers(List<User> users);

}
