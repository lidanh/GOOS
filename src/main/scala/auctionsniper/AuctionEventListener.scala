package auctionsniper

import java.util.EventListener

/**
 * Created by lidan on 15/01/15.
 */
trait AuctionEventListener extends EventListener {
  def currentPrice(price: Int, increment: Int)
  def auctionClosed()
  def auctionFailed()
}