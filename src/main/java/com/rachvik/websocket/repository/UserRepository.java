package com.rachvik.websocket.repository;

import com.rachvik.websocket.model.Status;
import com.rachvik.websocket.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
  List<User> findAllByStatus(final Status status);
}
