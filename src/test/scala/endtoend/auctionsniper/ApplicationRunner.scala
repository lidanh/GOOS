package endtoend.auctionsniper

/**
 * Created by lidan on 14/01/15.
 */
object ApplicationRunner {
  val SNIPER_ID = "sniper"
  val SNIPER_PASSWORD = "sniper"
}

class ApplicationRunner {
  import auctionsniper.Main
  import auctionsniper.ui.MainWindow
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
    driver = Some(d)
  }

  def showsSniperHasLostAuction(): Unit = {
    driver.foreach(_.showsSniperStatus(MainWindow.STATUS_LOST))
  }

  def stop(): Unit = {
    driver.foreach(_.dispose())
  }
}

