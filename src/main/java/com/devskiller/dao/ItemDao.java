package com.devskiller.dao;

import com.devskiller.model.Item;
import com.devskiller.model.Review;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

public class ItemDao extends HibernateDaoSupport {

    private static final String FIND_ITEM_QUERY = "FROM Item i ORDER BY i.id";
    private static final String FIND_ITEM_QUERY2 = "FROM Review r RIGHT JOIN r.item WHERE r.rating > ?0";

    @Transactional
    public Page<Item> findItems(PageRequest pageRequest) {
        List<Item> items = getHibernateTemplate().find(FIND_ITEM_QUERY)
                .stream().map(i -> (Item) i)
                .collect(Collectors.toList());
        return new Page<>(items, pageRequest.getPageNumber(), pageRequest.getCount());
    }

    @Transactional
    public List<Item> findItemsWithAverageRatingLowerThan(Integer rating) {
        List<Item> items = getHibernateTemplate().find(FIND_ITEM_QUERY2, rating)
                .stream()
                .map(r -> (Review) r)
                .map(r -> r.getItem())
                .collect(Collectors.toList());
        return items;
    }

}
