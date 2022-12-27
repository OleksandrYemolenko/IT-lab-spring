package com.example.demo1.services;

import com.example.demo1.entities.User;
import com.example.demo1.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) throws Exception {
        logger.info("Insert user into DB: {}", user);

        try {
            final User insertedUser = userRepository.save(user);

            logger.debug("User was inserted into DB: {}", insertedUser);

            return insertedUser;
        } catch (Exception ex) {
            logger.error("Error while inserting user into DB: " + user, ex);
            throw new Exception("An error occurred while inserting user", ex);
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public User update(User user) throws Exception {
        logger.info("Update user in DB: {}", user);

        final User actualUser = getByGuid(user.getGuid());

        try {
            actualUser.setName(user.getName());
            actualUser.setEmail(user.getEmail());
            actualUser.setPhoneNumber(user.getPhoneNumber());

            final User updatedUser = userRepository.save(actualUser);

            logger.debug("User was updated in DB: {}", updatedUser);

            return updatedUser;
        } catch (Exception ex) {
            logger.error("Error while updating user in DB: " + actualUser, ex);
            throw new Exception("An error occurred while updating user", ex);
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void deleteAll() throws Exception {
        logger.info("Delete all users except admin");
        try {
            userRepository.deleteAll();

            logger.debug("All users was deleted from DB");
        } catch (Exception ex) {
            logger.error("Error while deleting all users from DB:");
            throw new Exception("An error occured while deleting all users", ex);
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void deleteByGuid(UUID guid) throws Exception {
        logger.info("Delete user from DB by guid: {}", guid);

        final User user = getByGuid(guid);

        try {
            userRepository.deleteById(user.getIdUser());

            logger.debug("User was deleted from DB: {}", user);
        } catch (Exception ex) {
            logger.error("Error while deleting user from DB: " + user, ex);
            throw new Exception("An error occurred while deleting user", ex);
        }
    }

    @Transactional(readOnly = true, transactionManager = "transactionManager")
    public User getByGuid(UUID guid) throws Exception {
        logger.info("Get user from DB by guid: {}", guid);

        try {
            final User user = userRepository.findByGuid(guid)
                    .orElseThrow(() -> new NoSuchElementException("User was not found"));

            logger.debug("User was gotten from DB by guid: {}", user);
            return user;
        } catch (Exception ex) {
            logger.error("Error while getting user from DB by guid: " + guid, ex);
            throw new Exception("An error occurred while getting user", ex);
        }
    }
}
