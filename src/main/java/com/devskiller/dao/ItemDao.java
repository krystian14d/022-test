package com.devskiller.dao;

import com.devskiller.model.Item;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import javax.transaction.Transactional;
import java.util.List;

public class ItemDao extends HibernateDaoSupport {

    private static final int PAGE_SIZE = 10;

    private static final String FIND_ITEMS_BY_RATING_NATIVE_QUERY = "SELECT * FROM item JOIN item.id = review.id " +
            "WHERE AVG(CONVERT(review.rating, SQL_DOUBLE)) < :value GROUP BY item";
    private static final String FIND_ITEMS_QUERY = "SELECT i FROM Item i ORDER BY i.id";
    private static final String FIND_ITEMS_BY_RATING = "SELECT i FROM Item i JOIN i.reviews r " +
            "WHERE AVG(CAST(r.rating AS double)) < :rtg GROUP BY i";
    private static final String FIND_ITEMS_BY_RATING2 = "SELECT i FROM Item i JOIN i.reviews r " +
            "WHERE AVG(r.rating) < :rtg GROUP BY i";


    @Transactional
    public Page<Item> findItems(PageRequest pageRequest) {

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

        List<Item> resultList = currentSession().createQuery(FIND_ITEMS_QUERY, Item.class)
                .setMaxResults(PAGE_SIZE)
                .setFirstResult(PAGE_SIZE * pageRequest.getPageNumber())
                .getResultList();

        return new Page<>(resultList, pageRequest.getPageNumber(), pageRequest.getCount());
    }

    @Transactional
    public List<Item> findItemsWithAverageRatingLowerThan(Integer rating) {

        List<Item> items = currentSession().createQuery(FIND_ITEMS_BY_RATING2, Item.class)
                .setParameter("rtg", rating)
                .getResultList();

//        List<Item> items = (List<Item>) currentSession().createNativeQuery("SELECT * FROM item " +
//                        "JOIN item.id = review.id " +
//                        "WHERE AVG(1.0 * review.rating) < :value GROUP BY item")
//                .addEntity(Item.class)
//                .setParameter("value", rating.doubleValue())
//                .getResultList();
        return items;
    }

}
