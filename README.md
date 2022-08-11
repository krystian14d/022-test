There are three entities in `com.devskiller.model` package and one DAO with unimplemented method at `com.devskiller.dao`. 
Your job is to implement all methods at `ItemDao` class. There are two methods to implement:
 
- `Page<Item> findItems(PageRequest pageRequest)` - your implementation of this method should do paging and return a list of items for the page
specified by `pageRequest` object. If there are no elements for requested page, than an empty `content` list should be returned.
- `List<Item> findItemsWithAverageRatingLowerThan(Integer rating)` - your implementation of this method should find all items,
that have a rating lower than passed as an argument. Use reviews associated with each item to calculate item rating. 
If item has no reviews, than its rating should be zero. Your implementation should make as few database round trips as possible.

Use Spring helper classes for Hibernate to implement DAO methods.

There is a basic JUnit test in `test/java/com/devskiller/dao` folder, that will help you to verify if your DAO is working 
correctly, but keep in mind that your solution will be comprehensively tested with additional tests, that will check 
if all requirements are met.