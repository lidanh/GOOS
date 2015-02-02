package auctionsniper

/**
 * Created by lidan on 16/01/15.
 */
class AuctionSniper(private val item: String, private val auction: Auction, private val sniperListener: SniperListener) extends AuctionEventListener {
  private var isWinning = false
  private var snapshot = SniperSnapshot.joining(item)

  override def currentPrice(price: Int, increment: Int, from: PriceSource): Unit = {
    isWinning = from == FromSniper

    if (isWinning) {
      snapshot = snapshot.winning(price)
    } else {
      val bid = price + increment
      auction.bid(bid)
      snapshot = snapshot.bidding(price, bid)
    }

    sniperListener.sniperStateChanged(snapshot)
  }

  override def auctionFailed(): Unit = {
    snapshot = snapshot.failed()
    sniperListener.sniperStateChanged(snapshot)
  }

  override def auctionClosed(): Unit = {
    snapshot = snapshot.closed()
    sniperListener.sniperStateChanged(snapshot)
  }
}
