import auction.Auction;
import auction.ConsoleNotificationService;

/**
 * @author Evgenia
 */
public class AuctionLauncher {

    public static void main(String[] args) {
        Auction auction = new Auction(new ConsoleNotificationService());
        auction.start(10, 1000);
    }

}
