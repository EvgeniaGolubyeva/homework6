package auction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Evgenia
 */
public class BidService {
    private List<Bid> bids = new ArrayList<>();
    private NotificationService notificationService;

    public BidService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void placeBid(Bid bid) {
        if (bid.amount.compareTo(bid.product.minimalPrice) < 0) {
            notificationService.sendSorryNotification(bid);
            return;
        }

        if (bid.amount.compareTo(bid.product.reservedPrice) >= 0) {
            notificationService.sendWinNotification(bid);
            bid.isWinning = true;
            return;
        }

        bids.add(bid);

        List<Bid> productBids = getBidsByProduct(bid.product);
        notificationService.sendBidWasPlacedNotification(bid, productBids);
        notifyOverbiddenUsers(bid, productBids);
    }

    private void notifyOverbiddenUsers(Bid bid, List<Bid> productBids) {
        productBids.stream()
            .filter(b -> bid.amount.compareTo(b.amount) > 0)
            .filter(b -> b.user.getOverbidNotifications)
            .forEach(b -> notificationService.sendOverbiddenNotification(b, bid));
    }

    private List<Bid> getBidsByProduct(Product product) {
        return bids.stream()
                    .filter(b -> product.equals(b.product))
                    .collect(Collectors.toList());
    }
}
