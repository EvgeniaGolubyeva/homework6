import auction.Bid
import auction.BidGenerator
import auction.BidService
import auction.INotificationService
import auction.Product
import auction.User
import spock.lang.Specification
import spock.lang.Shared
import spock.lang.Unroll

/**
 * @author Evgenia
 */
class BidServiceTest extends Specification {

    @Shared
    BidService bidService

    def setup() {
        bidService = new BidService(Mock(INotificationService))
    }

    def "should send Sorry Notification if bid amount less then product min price"() {
        setup:
        Bid bid = createBid(0, false)

        when:
        bidService.placeBid(bid)

        then:
        1 * bidService.getNotificationService().sendSorryNotification(bid)
    }

    @Unroll
    def "should send Win Notification if bid amount grater or equal then product reserves price #amount"() {
        setup:
        Bid bid = createBid(amount, false)

        when:
        bidService.placeBid(bid)

        then:
        1 * bidService.getNotificationService().sendWinNotification(bid)

        where:
        amount << [20, 21]
    }

    @Unroll
    def "should send Bid was placed Notification if bid amount is in product price range #amount"() {
        setup:
        Bid bid = createBid(amount, false)

        when:
        bidService.placeBid(bid)

        then:
        1 * bidService.getNotificationService().sendBidWasPlacedNotification(bid, Arrays.asList(bid))

        where:
        amount << [10, 15, 19]
    }

    def "should send notification to overbid users if they want to be notified"() {
        setup:
        Bid b1 = createBid(12, true)
        Bid b2 = createBid(15, true)

        when:
        bidService.placeBid(b1)
        bidService.placeBid(b2)

        then:
        1 * bidService.getNotificationService().sendOverbidNotification(b1, b2)
    }

    def "should not send notification to overbid users if they do not want to be notified"() {
        setup:
        Bid b1 = createBid(12, false)
        Bid b2 = createBid(15, true)

        when:
        bidService.placeBid(b1)
        bidService.placeBid(b2)

        then:
        0 * bidService.getNotificationService().sendOverbidNotification(_, _)
    }

    private Product product = new Product(1, "title", 10, 20);

    private Bid createBid(double amount, boolean getOverbidNotification) {
        def bid = new BidGenerator().generateBid()
        bid.setAmount(amount)
        bid.setProduct(product)
        bid.getUser().setGetOverbidNotifications(getOverbidNotification)

        return bid;
    }
}
