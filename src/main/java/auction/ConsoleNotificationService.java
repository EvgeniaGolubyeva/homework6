package auction;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;

/**
 * @author Evgenia
 */
public class ConsoleNotificationService implements NotificationService {

    public void sendBidWasPlacedNotification(Bid bid, List<Bid> productBids) {
        printHeader(bid.user.email);
        printMessage("Your bid was successfully placed.");
        printProductBids(bid, productBids);
    }

    public void sendOverbiddenNotification(Bid bid, Bid winningBid) {
        printHeader(bid.user.email);

        String newAmount = currencyFormat(winningBid.amount);
        String oldAmount = currencyFormat(bid.amount);

        printMessage("You were overbidden. New bid amount " + newAmount + " is more than you placed " + oldAmount);
    }

    public void sendWinNotification(Bid bid) {
        printHeader(bid.user.email);

        String resPrice = currencyFormat(bid.product.reservedPrice);
        String amount   = currencyFormat(bid.amount);

        printMessage("Congratulations! Your bid " + amount + " is winning, product reserved price " + resPrice);
    }

    public void sendSorryNotification(Bid bid) {
        printHeader(bid.user.email);

        String minPrice = currencyFormat(bid.product.minimalPrice);
        String amount   = currencyFormat(bid.amount);

        printMessage("Sorry! The amount you placed " + amount + " is less than product minimum price " + minPrice);
    }



    private void printHeader(String email) {
        System.out.println();
        System.out.println("*** Message to " + email + ":");
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    private void printProductBids(Bid currentBid, List<Bid> productBids) {
        System.out.print("Current bids for " + currentBid.product.title + " are: ");

        Comparator<Bid> comparator = (o1, o2) -> o2.amount.compareTo(o1.amount);

        productBids.stream()
                .sorted(comparator.thenComparing(b -> b.amount))
                .forEach(b -> printBid(b, b.equals(currentBid)));

        System.out.println();
    }

    private void printBid(Bid b, boolean shouldMark) {
        System.out.print(shouldMark ? "*" : "");
        System.out.print(currencyFormat(b.amount) + "(" + b.user.id);
        System.out.print(b.user.getOverbidNotifications ? "+)" : "-)");
        System.out.print(shouldMark ? "* " : " ");
    }

    private String currencyFormat(BigDecimal value) {
        return NumberFormat.getCurrencyInstance().format(value);
    }
}
