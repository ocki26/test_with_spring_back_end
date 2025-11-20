package com.app_language.hoctiengtrung_online.Ticker.repository;


import com.app_language.hoctiengtrung_online.Ticker.model.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {

    List<Artist> findByCompanyCode(String companyCode);

    List<Artist> findByActiveTrue();

    List<Artist> findByNameContainingIgnoreCase(String name);

    @Query("{ 'companyCode': ?0, 'active': true }")
    List<Artist> findActiveArtistsByCompany(String companyCode);

    Optional<Artist> findByNameAndCompanyCode(String name, String companyCode);

    @Query("{ 'companyCode': ?0, 'name': { $regex: ?1, $options: 'i' } }")
    List<Artist> findByCompanyAndNameLike(String companyCode, String name);

    boolean existsByNameAndCompanyCode(String name, String companyCode);

    long countByCompanyCode(String companyCode);

    @Query("{ 'companyCode': ?0, 'active': ?1 }")
    List<Artist> findByCompanyCodeAndActive(String companyCode, boolean active);
}