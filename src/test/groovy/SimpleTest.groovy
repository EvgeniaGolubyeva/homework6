import spock.lang.Specification

class SimpleTest extends Specification{
    def "simple test"() {
        expect:
        1 == 1;
    }
}