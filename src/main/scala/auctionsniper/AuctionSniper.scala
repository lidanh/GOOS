package auctionsniper

/**
 * Created by lidan on 16/01/15.
 */
class AuctionSniper(
                     private val itemId: String,
                     private val auction: Auction,
                     private val sniperListener: SniperListener
                     ) extends AuctionEventListener {
  private var snapshot = SniperSnapshot.joining(itemId)

  override def currentPrice(price: Int, increment: Int, from: PriceSource): Unit = {
    snapshot = from match {
      case FromSniper => snapshot.winning(price)
      case FromOtherBidder =>
        val bid = price + increment
        auction.bid(bid)
        snapshot.bidding(price, bid)
    }

    notifyChange
  }

  override def auctionFailed(): Unit = {
    snapshot = snapshot.failed()
    notifyChange
  }

  override def auctionClosed(): Unit = {
    snapshot = snapshot.closed()
    notifyChange
  }

  private def notifyChange {
    sniperListener.sniperStateChanged(snapshot)
  }
}
