package auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgenia
 */
public class BidGenerator {
    private int count = 0;
    private List<Product> products = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public BidGenerator() {
        createUsers();
        createProducts();
    }

    public Bid generateBid() {
        Bid res = new Bid();

        res.id = count++;
        res.product = products.get((int) (Math.random() * products.size()));
        res.amount = new BigDecimal(Math.random()).multiply(res.product.reservedPrice).add(new BigDecimal(100));
        res.desiredQuantity = (int) (Math.random() * 3 + 1);
        res.user = users.get((int) (Math.random() * users.size()));
        res.bidTime = LocalDateTime.now();

        return res;
    }

    private void createUsers() {
        addUser(1, "User 1", "User1@gmail.com", true);
        addUser(2, "User 2", "User2@gmail.com", true);
        addUser(3, "User 3", "User3@gmail.com", false);
    }

    private void createProducts() {
        addProduct(111, "Monitor", "images/monitor.jpg", "The best monitor ever",
                   5, LocalDateTime.of(2014, 7, 15, 15, 1), 2, 100, 700);
//        addProduct(222, "SSD", "images/sdd.jpg", "The fastest SSD ever",
//                   2, LocalDateTime.of(2014, 5, 15, 15, 1), 2, 20, 120);
    }

    private void addUser(int id, String name, String email, boolean getOverbidNotifications) {
        User user = new User();
        user.id = id;
        user.name = name;
        user.email = email;
        user.getOverbidNotifications = getOverbidNotifications;
        users.add(user);
    }

    private void addProduct(int id, String title, String thumb, String description,
                            int quantity, LocalDateTime auctionEndTime, int watchers,
                            double minimalPrice, double reservedPrice) {
        Product product = new Product();

        product.id = id;
        product.title = title;
        product.thumb = thumb;
        product.description = description;
        product.quantity = quantity;
        product.auctionEndTime = auctionEndTime;
        product.watchers = watchers;
        product.minimalPrice = new BigDecimal(minimalPrice);
        product.reservedPrice = new BigDecimal(reservedPrice);

        products.add(product);
    }
}
