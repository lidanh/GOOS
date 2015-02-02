package auctionsniper

import java.util.EventListener

/**
 * Created by lidan on 16/01/15.
 */
trait SniperListener extends EventListener {
  def notify(snapshot: SniperSnapshot)
}
