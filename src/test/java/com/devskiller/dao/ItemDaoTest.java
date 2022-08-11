package com.devskiller.dao;

import com.devskiller.context.PersistenceConfiguration;
import com.devskiller.model.Item;
import com.devskiller.model.Review;
import com.devskiller.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfiguration.class)
public class ItemDaoTest {

  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private ItemDao itemDao;

  @Test
  public void shouldReturnFirstPageOfItems() {
    Page<Item> items = itemDao.findItems(new PageRequest(0, 10));
    assertThat("There should be 10 items on first page", items.getContent().size(), equalTo(10));
  }


  @Test
  public void shouldReturnItemsWithAverageRating() {
    assertThat("There should be 10 items with avg rating < 10",
        itemDao.findItemsWithAverageRatingLowerThan(10).size(), equalTo(10));
    assertThat("There should be 1 items with avg rating < 1",
        itemDao.findItemsWithAverageRatingLowerThan(1).size(), equalTo(1));
  }


  @Before
  public void prepareData() {
    Session session = sessionFactory.openSession();
    Transaction transaction = session.beginTransaction();
    User user = new User(RandomString.nextString(5));
    session.persist(user);
    for (int i=0; i<15; i++) {
      Item item = new Item("title " + i, "description");
      item.addReview(new Review( i < 10 ? i : 10, "Review "+i+"/1", user));
      item.addReview(new Review( i < 10 ? i+1 : 10, "Review "+i+"/2", user));
      session.persist(item);
    }
    session.flush();
    transaction.commit();
    session.close();
  }

  @After
  public void clearData() {

    Session session = sessionFactory.openSession();
    Transaction transaction = session.beginTransaction();
    session.createQuery("delete Review").executeUpdate();
    session.createQuery("delete User").executeUpdate();
    session.createQuery("delete Item").executeUpdate();
    session.flush();
    transaction.commit();
    session.close();
  }

}
