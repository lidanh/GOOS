package unit.auctionsniper

/**
 * Created by lidan on 25/01/15.
 */
class SniperState(val ordindal: Int) {
  def whenAuctionClosed: SniperState = throw new RuntimeException("Auction is already closed")
}

object SniperState {
  object Joining extends SniperState(0) {
    override def whenAuctionClosed: SniperState = Lost
  }

  object Bidding extends SniperState(1) {
    override def whenAuctionClosed: SniperState = Lost
  }

  object Winning extends SniperState(2) {
    override def whenAuctionClosed: SniperState = Won
  }

  object Lost extends SniperState(3)
  object Won extends SniperState(4)
}
