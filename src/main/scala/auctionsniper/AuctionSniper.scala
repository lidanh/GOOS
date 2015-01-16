package auctionsniper

/**
 * Created by lidan on 16/01/15.
 */
class AuctionSniper(private val auction: Auction, private val sniperListener: SniperListener) extends AuctionEventListener {
  override def currentPrice(price: Int, increment: Int): Unit = {
    auction.bid(price + increment)
    sniperListener.sniperBidding()
  }

  override def auctionFailed(): Unit = ???

  override def auctionClosed(): Unit = sniperListener.sniperLost()
}
