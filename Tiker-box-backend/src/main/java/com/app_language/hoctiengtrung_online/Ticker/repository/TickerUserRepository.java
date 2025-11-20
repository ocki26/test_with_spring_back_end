package com.app_language.hoctiengtrung_online.Ticker.repository;


import com.app_language.hoctiengtrung_online.Ticker.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TickerUserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndPassword(String username, String password);

    List<User> findByRole(User.UserRole role);

    List<User> findByActiveTrue();

    @Query("{ 'active': true, 'role': ?0 }")
    List<User> findActiveUsersByRole(User.UserRole role);

    @Query("{ 'active': true, 'username': { $regex: ?0, $options: 'i' } }")
    List<User> findActiveUsersByUsernameLike(String username);

    @Query("{ 'active': true, $or: [ { 'username': { $regex: ?0, $options: 'i' } }, { 'email': { $regex: ?0, $options: 'i' } }, { 'fullName': { $regex: ?0, $options: 'i' } } ] }")
    List<User> searchActiveUsers(String keyword);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("{ 'username': ?0, 'active': true }")
    Optional<User> findActiveUserByUsername(String username);

    @Query("{ 'email': ?0, 'active': true }")
    Optional<User> findActiveUserByEmail(String email);

    @Query(value = "{ 'role': ?0 }", count = true)
    long countByRole(User.UserRole role);

    @Query(value = "{ 'active': true, 'role': ?0 }", count = true)
    long countActiveUsersByRole(User.UserRole role);

    @Query("{ 'role': { $in: [ 'ADMIN', 'COMPANY_USER' ] } }")
    List<User> findAdminAndCompanyUsers();
}