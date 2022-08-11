package com.devskiller.dao;

import com.devskiller.model.Item;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.List;

public class ItemDao extends HibernateDaoSupport {

  public Page<Item> findItems(PageRequest pageRequest) {
    // TODO: Implement
    return new Page<>(new ArrayList<>(), 0, 0);
  }

  public List<Item> findItemsWithAverageRatingLowerThan(Integer rating) {
    return new ArrayList<>();
  }


}
