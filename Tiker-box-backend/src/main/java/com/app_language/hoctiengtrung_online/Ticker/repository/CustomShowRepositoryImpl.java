package com.app_language.hoctiengtrung_online.Ticker.repository;



import com.app_language.hoctiengtrung_online.Ticker.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CustomShowRepositoryImpl implements CustomShowRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Show> findShowsWithTicketAvailability() {
        Query query = new Query();
        query.addCriteria(Criteria.where("active").is(true)
                .and("ticketTypes.availableQuantity").gt(0));
        return mongoTemplate.find(query, Show.class);
    }

    @Override
    public List<Show> findShowsByMultipleArtists(List<String> artistIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("active").is(true)
                .and("artistIds").in(artistIds));
        return mongoTemplate.find(query, Show.class);
    }

    @Override
    public List<Show> findShowsWithTicketPriceRange(Double minPrice, Double maxPrice) {
        Query query = new Query();
        query.addCriteria(Criteria.where("active").is(true)
                .and("ticketTypes.price").gte(minPrice).lte(maxPrice));
        return mongoTemplate.find(query, Show.class);
    }

    @Override
    public List<Show> findPopularShows(int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("active").is(true));
        query.limit(limit);
        // Có thể thêm sắp xếp theo số lượng vé đã bán hoặc các tiêu chí khác
        return mongoTemplate.find(query, Show.class);
    }
}