import auction.Bid
import auction.BidGenerator
import auction.BidService
import auction.NotificationService
import auction.User
import spock.lang.Specification
import spock.lang.Shared
import spock.lang.Unroll

import java.lang.reflect.Array

/**
 * @author Evgenia
 */
class BidServiceTest extends Specification {

    @Shared
    Bid bid = new BidGenerator().generateBid()
    @Shared
    BidService bidService

    def setup() {
        bidService = new BidService(Mock(NotificationService))
        bid.product.minimalPrice = 10
        bid.product.reservedPrice = 20
    }

    def "should send Sorry Notification if bid amount less then product min price"() {
        setup:
        bid.amount = 0

        when:
        bidService.placeBid(bid)

        then:
        1 * bidService.getNotificationService().sendSorryNotification(bid)
    }

    @Unroll
    def "should send Win Notification if bid amount grater or equal then product reserves price"() {
        when:
        bid.amount = amount
        bidService.placeBid(bid)

        then:
        1 * bidService.getNotificationService().sendWinNotification(bid)

        where:
        amount << [20, 21]
    }

    def "should send Bid was placed Notification if bid amount is in product price range"() {
        when:
        bid.amount = amount
        bidService.placeBid(bid)

        then:
        1 * bidService.getNotificationService().sendBidWasPlacedNotification(bid, Arrays.asList(bid))

        where:
        amount << [10, 15, 19]
    }

    def "should send notification to overbidden users"() {
        setup:
        Bid b1 = bid
        b1.amount = 12
        b1.user = createUser(1, "1", "1@mail.com", true)

        Bid b2 = new BidGenerator().generateBid();
        b2.amount = 15
        b2.product = b1.product
        b2.user = createUser(2, "2", "2@mail.com", false)

        Bid b3 = new BidGenerator().generateBid();
        b3.amount = 18
        b3.product = b1.product
        b3.user = createUser(3, "3", "3@mail.com", true)

        Bid b4 = new BidGenerator().generateBid();
        b4.amount = 16
        b4.product = b1.product
        b4.user = createUser(4, "4", "4@mail.com", true)

        when:
        bidService.placeBid(b1)
        bidService.placeBid(b2)
        bidService.placeBid(b3)
        bidService.placeBid(b4)

        then:
        1 * bidService.getNotificationService().sendOverbiddenNotification(b1, b2)
        1 * bidService.getNotificationService().sendOverbiddenNotification(b1, b3)
        1 * bidService.getNotificationService().sendOverbiddenNotification(b1, b4)
    }

    //for now the code is duplicated from BidGenerator, how to avoid?
    //or may be it's a User constructor?
    //Eventually BidGenerator will be removed and code will exist only here
    def createUser(int id, String name, String email, boolean getOverbidNotifications) {
        User user = new User();
        user.id = id;
        user.name = name;
        user.email = email;
        user.getOverbidNotifications = getOverbidNotifications;
        return user;
    }
}
