package auction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Evgenia
 */
public class BidService {
    private List<Bid> bids = new ArrayList<>();

    private INotificationService notificationService;

    public BidService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public INotificationService getNotificationService() {
        return notificationService;
    }

    public void placeBid(Bid bid) {
        if (bid.getAmount().compareTo(bid.getProduct().getMinimalPrice()) < 0) {
            notificationService.sendSorryNotification(bid);
            return;
        }

        if (bid.getAmount().compareTo(bid.getProduct().getReservedPrice()) >= 0) {
            notificationService.sendWinNotification(bid);
            bid.setWinning(true);
            return;
        }

        bids.add(bid);

        List<Bid> productBids = getBidsByProduct(bid.getProduct());
        notificationService.sendBidWasPlacedNotification(bid, productBids);
        notifyOverbidUsers(bid, productBids);
    }

    private void notifyOverbidUsers(Bid bid, List<Bid> productBids) {
        productBids.stream()
            .filter(b -> bid.getAmount().compareTo(b.getAmount()) > 0)
            .filter(b -> b.getUser().isGetOverbidNotifications())
            .forEach(b -> notificationService.sendOverbidNotification(b, bid));
    }

    private List<Bid> getBidsByProduct(Product product) {
        return bids.stream()
                    .filter(b -> product.equals(b.getProduct()))
                    .collect(Collectors.toList());
    }
}
