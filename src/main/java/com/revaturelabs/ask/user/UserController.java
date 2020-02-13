package com.revaturelabs.ask.user;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;
import com.revaturelabs.ask.question.Question;
import com.revaturelabs.ask.tag.TagService;

@RestController
@RequestMapping(path = "/users")
/*
 * @MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB maxFileSize = 1024 * 1024 * 10, //
 * 10 MB maxRequestSize = 1024 * 1024 * 15, // 15 MB location = "/")
 */

/**
 * 
 * @author Carlos Santos, Chris Allen
 *
 */
public class UserController {

  @Autowired
  UserService userService;
  
  @Autowired
  TagService tagService;

  @GetMapping
  public ResponseEntity<List<User>> findAll(@RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {

    if (page == null) {
      page = 0;
    }
    if (size == null) {
      size = 20;
    }
    return ResponseEntity.ok(userService.findAll(page, size).getContent());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable int id) {
    try {
      return ResponseEntity.ok(userService.findById(id));
    } catch (UserNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
    }
  }

  @PutMapping("/{id}")
  public User createOrUpdate(@RequestBody User user, @PathVariable int id) {
    user.setId(id);
    try {
      return userService.createOrUpdate(user);
    } catch (UserConflictException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists", e);
    }
  }

  @PatchMapping("/{id}")
  public User updateUser(@RequestBody User user, @PathVariable int id) {
    user.setId(id);
    
    user.setExpertTags(tagService.getValidTags(user.getExpertTags()));
    try {
      return userService.update(user);
    } catch (UserConflictException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User name already exists", e);
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
    }
    
  }
  
  @PatchMapping("/profile/{id}")
  public String updateUserInfo(MultipartHttpServletRequest user, @PathVariable int id) {
    
    //TEST
    System.out.println("ID: " + id);
   
    
    String key = "";
    
      try {
        Part name = user.getPart("name");
        System.out.println("NAME: " + name);
        MultipartFile image = user.getFile("myImage");
        System.out.println("IMAGE: " + image);
        key = userService.uploadProfilePicture(image);
        
        User updatedUser = userService.findById(id);
        updatedUser.setProfilePic(key);
        userService.update(updatedUser);
        
      } catch (IOException e) {
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "IOException on File.");
      } catch (ServletException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad multipart file request");
      }
      
    return key;
    
  }

  @PostMapping
  public User createUser(@RequestBody User user) {
    return userService.create(user);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable int id) {
    try {
      userService.delete(id);
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
    }
  }

  /**
   * 
   * Takes HTTP GET requests and returns the set of questions associated with the specified user
   * 
   * @param id
   * @return The set of questions associated with the user
   */
  @GetMapping("/{id}/questions")
  public ResponseEntity<Set<Question>> getQuestions(@PathVariable int id) {
    try {
      return ResponseEntity.ok(userService.findById(id).getQuestions());
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
    }
  }
  
  /**
   * 
   * Takes HTTP PUT requests and returns the updated user after setting the tags to be updated
   * @param user The user object with tags to be changed
   * @return A User JSON after updating
   */
  @PutMapping("/{id}/tags")
  public ResponseEntity<User> updateUserTags(@RequestBody User user, @PathVariable int id){
    user.setExpertTags(tagService.getValidTags(user.getExpertTags()));
    user.setId(id);
    try {
      return ResponseEntity.ok(userService.updateTags(user));
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
    }
  }
}


