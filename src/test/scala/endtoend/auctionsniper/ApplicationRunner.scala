package endtoend.auctionsniper

import auctionsniper.SniperState
import auctionsniper.ui.MainWindow

/**
 * Created by lidan on 14/01/15.
 */
object ApplicationRunner {
  val SNIPER_ID = "sniper"
  val SNIPER_PASSWORD = "sniper"
  val SNIPER_XMPP_ID = SNIPER_ID + "@" + FakeAuctionServer.XMPP_HOSTNAME + "/Auction"
}

class ApplicationRunner {

  import auctionsniper.Main
  import endtoend.auctionsniper.ApplicationRunner._

  private[this] var driver: Option[AuctionSniperDriver] = None

  def startBiddingIn(auction: FakeAuctionServer) {
    val thread = new Thread("Test Application") {
      override def run(): Unit = {
        try {
          Main.main(Array(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId))
        } catch {
          case e: Exception => e.printStackTrace()
        }
      }
    }

    thread.setDaemon(true)
    thread.start()
    val d = new AuctionSniperDriver(1000)
    d.hasTitle(MainWindow.APPLICATION_TITLE)
    d.hasColumnTitles()
    driver = Some(d)
//    d.showsSniperStatus(JOINING.itemId, JOINING.lastPrice, JOINING.lastBid, SniperState.Joining)
  }

  def hasShownSniperIsBidding(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int): Unit = {
    driver.foreach(_.showsSniperStatus(auction.itemId, lastPrice, lastBid, SniperState.Bidding.toString))
  }

  def showsSniperHasLostAuction(): Unit = {
    driver.foreach(_.showsSniperStatus(SniperState.Lost.toString))
  }

  def showsSniperHasWonAuction(auction: FakeAuctionServer, lastPrice: Int) = {
    driver.foreach(_.showsSniperStatus(auction.itemId, lastPrice, lastPrice, SniperState.Won.toString))
  }

  def hasShownSniperIsWinning(auction: FakeAuctionServer, winningBid: Int) = {
    driver.foreach(_.showsSniperStatus(auction.itemId, winningBid, winningBid, SniperState.Winning.toString))
  }

  def stop(): Unit = {
    driver.foreach(_.dispose())
  }
}

