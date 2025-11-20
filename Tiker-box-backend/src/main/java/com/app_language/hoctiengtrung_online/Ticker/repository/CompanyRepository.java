package com.app_language.hoctiengtrung_online.Ticker.repository;



import com.app_language.hoctiengtrung_online.Ticker.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

    Optional<Company> findByCode(String code);

    List<Company> findByActiveTrue();

    Optional<Company> findByCodeAndPassword(String code, String password);

    Optional<Company> findByEmail(String email);

    List<Company> findByNameContainingIgnoreCase(String name);

    @Query("{ 'active': true, 'name': { $regex: ?0, $options: 'i' } }")
    List<Company> findActiveCompaniesByName(String name);

    boolean existsByCode(String code);

    boolean existsByEmail(String email);

    @Query("{ 'active': true, $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'code': { $regex: ?0, $options: 'i' } } ] }")
    List<Company> searchActiveCompanies(String keyword);

    long countByActiveTrue();
}