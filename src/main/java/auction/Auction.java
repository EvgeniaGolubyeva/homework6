package auction;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import static utils.StringUtils.currencyFormat;

/**
 * @author Evgenia
 */
public class Auction {

    private final BidService bidService;
    private final BidGenerator generator;

    public Auction(NotificationService notificationService) {
        bidService = new BidService(notificationService);
        generator = new BidGenerator();
    }

    public void start(final int count, int period) {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Bid bid = generator.generateBid();
                printBid(bid);

                bidService.placeBid(bid);

                if (bid.id == count - 1) timer.cancel();
            }
        }, 0, period);
    }

    private void printBid(Bid bid) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("Placing bid");
        System.out.println("\t user = " + bid.user.id);
        System.out.println("\t product = " + bid.product.title +
                " [min: " + currencyFormat(bid.product.minimalPrice) +
                ", reserved: " + currencyFormat(bid.product.reservedPrice) + "]");
        System.out.println("\t amount = " + currencyFormat(bid.amount));
    }

}
