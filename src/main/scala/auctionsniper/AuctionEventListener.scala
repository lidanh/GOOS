package auctionsniper

import java.util.EventListener

/**
 * Created by lidan on 15/01/15.
 */
trait AuctionEventListener extends EventListener {
  def currentPrice(price: Int, increment: Int, from: PriceSource)
  def auctionClosed()
  def auctionFailed()
}

trait PriceSource
object FromSniper extends PriceSource
object FromOtherBidder extends PriceSource
