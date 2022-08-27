package com.devskiller.dao;

import com.devskiller.model.Item;
import com.devskiller.model.Review;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemDao extends HibernateDaoSupport {

    private static final int PAGE_SIZE = 10;

    private static final String FIND_ITEMS_QUERY = "SELECT i FROM Item i ORDER BY i.id";
    private static final String FIND_REVIEWS_QUERY = "SELECT r FROM Review r ORDER BY r.id";


    private static final String FIND_ITEMS_BY_RATING =
            "SELECT i FROM Item i " +
            "JOIN i.reviews r " +
            "WHERE AVG(r.rating) < :rtg GROUP BY i";

    private static final String FIND_ITEMS_BY_RATING2 =
            "SELECT i FROM Item i " +
            "LEFT JOIN Review r ON i.id=r.id " +
            "WHERE AVG(r.rating) < :rtg GROUP BY i";

    private static final String FIND_ITEMS_BY_RATING_NATIVE_QUERY =
            "SELECT item.id, item.description, item.title " +
                    "FROM item " +
                    "LEFT JOIN review ON item.id = review.item_id " +
                    "GROUP BY item.id, item.description, item.title " +
                    "HAVING avg(review.rating)<:rtg";

    @Transactional
    public Page<Item> findItems(PageRequest pageRequest) {

        //First solution
//        List<Item> items = getHibernateTemplate().find(FIND_ITEMS_QUERY)
//                .stream()
//                .map(i -> (Item) i)
//                .collect(Collectors.toList());
//
//        PagedListHolder<Item> page = new PagedListHolder<>(items);
//        page.setPageSize(PAGE_SIZE); //set page size
//        page.setPage(pageRequest.getPageNumber());
//
//        return new Page<>(page.getPageList(), pageRequest.getPageNumber(), pageRequest.getCount());

        //second solution
        List<Item> resultList = currentSession().createQuery(FIND_ITEMS_QUERY, Item.class)
                .setMaxResults(PAGE_SIZE)
                .setFirstResult(PAGE_SIZE * pageRequest.getPageNumber())
                .getResultList();

        return new Page<>(resultList, pageRequest.getPageNumber(), pageRequest.getCount());
    }

    @Transactional
    public List<Item> findItemsWithAverageRatingLowerThan(Integer rating) {

        //first solution - ClassCastException or SQL Error -458
//        double ratingDouble = rating.doubleValue();
//        List<Item> items = currentSession().createQuery(FIND_ITEMS_BY_RATING2, Item.class)
//                .setParameter("rtg", ratingDouble) //ClassCastException - cannot cast Integer to Double
////                .setDouble("rtg", ratingDouble) //WARN  SQL Error: -458, SQLState: S1000
//                .getResultList();

        //second solution - native query
        List<Item> items = currentSession().createNativeQuery(FIND_ITEMS_BY_RATING_NATIVE_QUERY, Item.class)
                .setParameter("rtg", rating)
                .getResultList();

//        //third solution - malformed numeric constant
//        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
//        CriteriaQuery<Item> criteriaQuery = criteriaBuilder.createQuery(Item.class);
//        Root<Item> item = criteriaQuery.from(Item.class);
//        Join<Object, Object> reviews = (Join) item.fetch("reviews", JoinType.LEFT);
//
//        ParameterExpression<Double> parameter = criteriaBuilder.parameter(Double.class);
//        criteriaQuery.select(item)
//                .groupBy(item.get("id"),
//                        item.get("title"),
//                        item.get("description"),
//                        item.get("reviews"))
//                .where(criteriaBuilder.lessThan(
//                        criteriaBuilder.avg(reviews.get("rating")),
//                        parameter
//                ));
//
//        Query<Item> query = currentSession().createQuery(criteriaQuery);
//        query.setParameter(parameter, rating.doubleValue());
//        List<Item> items = query.getResultList();

//        //fourth attempt - criteriabuilder - join instead of fetch, removed .setParameter method
//        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
//        CriteriaQuery<Item> criteriaQuery = criteriaBuilder.createQuery(Item.class);
//        Root<Item> item = criteriaQuery.from(Item.class);
//        Join<Object, Object> reviews = item.join("reviews", JoinType.LEFT);
//        criteriaQuery.select(item)
//                .groupBy(item.get("id"),
//                        item.get("description"),
//                        item.get("title")
//                )
//                .having(criteriaBuilder.lessThan(
//                        criteriaBuilder.avg(reviews.get("rating")),
//                        rating.doubleValue()
//                ));
//
//        Query<Item> query = currentSession().createQuery(criteriaQuery);
//        List<Item> items = query.getResultList();


//        //working solution, but low DB efficiency
//        List<Item> items = new ArrayList<>();
//
//        Map<Item, List<Review>> reviewsByItemId = currentSession().createQuery(FIND_REVIEWS_QUERY, Review.class)
//                .stream()
//                .collect(Collectors.groupingBy(Review::getItem));
//
//        reviewsByItemId.forEach(
//                (item, reviews) -> {
//                    double sumOfRating = getSumOfRating(reviews);
//
//                    double avg = sumOfRating / reviews.size();
//
//                    if (avg < rating.doubleValue()) {
//                        items.add(item);
//                    }
//                }
//        );

        return items;
    }

    private double getSumOfRating(List<Review> reviews) {
        return reviews
                .stream()
                .map(Review::getRating)
                .mapToDouble(i -> i.doubleValue())
                .sum();
    }
}
