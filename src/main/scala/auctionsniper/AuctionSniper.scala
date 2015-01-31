package auctionsniper

/**
 * Created by lidan on 16/01/15.
 */
class AuctionSniper(private val auction: Auction, private val sniperListener: SniperListener) extends AuctionEventListener {
  private var isWinning = false

  override def currentPrice(price: Int, increment: Int, from: PriceSource): Unit = {
    isWinning = from == FromSniper

    if (isWinning) {
      sniperListener.sniperWinning()
    } else {
      auction.bid(price + increment)
      sniperListener.sniperBidding()
    }
  }

  override def auctionFailed(): Unit = ???

  override def auctionClosed(): Unit = {
    if (isWinning)
      sniperListener.sniperWon()
    else
      sniperListener.sniperLost()
  }
}
