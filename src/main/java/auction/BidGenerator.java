package auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Evgenia
 */
public class BidGenerator {
    private int count = 0;
    private Random random = new Random();
    private List<Product> products = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public BidGenerator() {
        createUsers();
        createProducts();
    }

    public Bid generateBid() {
        Bid res = new Bid();

        res.setId(count++);

        int randomProductIndex = random.nextInt(products.size());
        Product product = products.get(randomProductIndex);
        res.setProduct(product);

        BigDecimal resPrice = res.getProduct().getReservedPrice();
        BigDecimal amount = new BigDecimal(random.nextDouble()).multiply(resPrice.add(new BigDecimal(100)));
        res.setAmount(amount);

        res.setDesiredQuantity(random.nextInt(3) + 1);

        int randomUserIndex = random.nextInt(users.size());
        User user = users.get(randomProductIndex);
        res.setUser(user);

        res.setBidTime(LocalDateTime.now());

        return res;
    }

    private void createUsers() {
        addUser(1, "User 1", "User1@mail.com", true);
        addUser(2, "User 2", "User2@mail.com", true);
        addUser(3, "User 3", "User3@mail.com", false);
    }

    private void createProducts() {
        addProduct(111, "Monitor", 100, 700);
//        addProduct(222, "SSD", 20, 120);
    }

    private void addUser(int id, String name, String email, boolean getOverbidNotifications) {
        User user = new User(id, name, email, getOverbidNotifications);
        users.add(user);
    }

    private void addProduct(int id,
                            String title,
                            double minimalPrice,
                            double reservedPrice) {
        Product product = new Product(id, title, new BigDecimal(minimalPrice), new BigDecimal(reservedPrice));
        products.add(product);
    }
}
