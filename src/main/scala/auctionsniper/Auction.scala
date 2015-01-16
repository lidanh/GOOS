package auctionsniper

/**
 * Created by lidan on 16/01/15.
 */
trait Auction {
  def bid(amount: Int)
  def join()
}
