package auctionsniper

/**
 * Created by lidan on 25/01/15.
 */
class SniperState(val ordindal: Int) {
  def whenAuctionClosed: SniperState = throw new RuntimeException("Auction is already closed")
}

object SniperState {
  object Joining extends SniperState(0) {
    override def whenAuctionClosed: SniperState = Lost
    override def toString: String = "Joining"
  }

  object Bidding extends SniperState(1) {
    override def whenAuctionClosed: SniperState = Lost
    override def toString: String = "Bidding"
  }

  object Winning extends SniperState(2) {
    override def whenAuctionClosed: SniperState = Won
    override def toString: String = "Winning"
  }

  object Lost extends SniperState(3) {
    override def toString: String = "Lost"
  }

  object Won extends SniperState(4) {
    override def toString: String = "Won"
  }

  object Failed extends SniperState(5) {
    override def toString: String = "Failed"
  }
}
