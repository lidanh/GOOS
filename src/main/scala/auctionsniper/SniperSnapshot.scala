package auctionsniper

/**
 * Created by lidan on 01/02/15.
 */
case class SniperSnapshot (
                          itemId: String,
                          lastPrice: Int,
                          lastBid: Int,
                          state: SniperState
                            ) {
  def bidding(newLastPrice: Int, newLastBid: Int) = copy(lastPrice = newLastPrice, lastBid = newLastBid, state = SniperState.Bidding)

  def winning(newLastPrice: Int) = copy(lastPrice = newLastPrice, lastBid = newLastPrice, state = SniperState.Winning)

  def losing(newLastPrice: Int) = copy(lastPrice = newLastPrice, state = SniperState.Lost)

  def failed() = copy(itemId = itemId, lastPrice = 0, lastBid = 0, state = SniperState.Failed)

  def closed() = copy(state = state.whenAuctionClosed)
}

object SniperSnapshot {
  def joining(itemId: String) = SniperSnapshot(itemId, 0, 0, SniperState.Joining)
}
