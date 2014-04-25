package auction;

import java.util.List;

/**
 * @author Evgenia
 */
public interface NotificationService {
    void sendBidWasPlacedNotification(Bid bid, List<Bid> productBids);
    void sendOverbiddenNotification(Bid bid, Bid winningBid);
    void sendWinNotification(Bid bid);
    void sendSorryNotification(Bid bid);
}
