import auction.Bid
import auction.BidGenerator
import spock.lang.Shared
import spock.lang.Specification

/**
 * @author Evgenia
 */
class BidGeneratorTest extends Specification {
    @Shared
    BidGenerator generator = new BidGenerator()

    def "should generate bids with not null fields"() {
        setup:
        Bid bid = generator.generateBid();

        expect:
        bid.id >= 0
        bid.product != null
        bid.amount != null
        bid.desiredQuantity >= 0
        bid.user != null
        bid.bidTime != null
        !bid.isWinning
    }


    def "should create bids with unique id"() {
        setup:
        Set<Integer> ids = new HashSet<Integer>()

        expect:
        for (int i = 0; i < 10; i++) {
            Bid bid = generator.generateBid();
            assert ids.add(bid.id);
        }
    }
}
