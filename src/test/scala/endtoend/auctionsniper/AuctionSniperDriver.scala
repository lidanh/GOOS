package endtoend.auctionsniper

import javax.swing.table.JTableHeader

import auctionsniper.ui.MainWindow
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.{ComponentDriver, JFrameDriver, JTableDriver, JTableHeaderDriver}
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.{IterableComponentsMatcher, JLabelTextMatcher}
import org.hamcrest.CoreMatchers._

/**
 * Created by lidan on 14/01/15.
 */
class AuctionSniperDriver(timeoutMillis: Int) extends JFrameDriver(new GesturePerformer(),
                                                                    JFrameDriver.topLevelFrame(
                                                                      ComponentDriver.named(MainWindow.MAIN_WINDOW_NAME),
                                                                      ComponentDriver.showingOnScreen()),
                                                                    new AWTEventQueueProber(timeoutMillis, 100)) {
  def showsSniperStatus(statusText: String): Unit = {
    new JTableDriver(this).hasCell(JLabelTextMatcher.withLabelText(equalTo(statusText)))
  }

  def showsSniperStatus(itemId: String, lastPrice: Int, lastBid: Int, statusText: String): Unit = {
    val table = new JTableDriver(this)
    table.hasRow(IterableComponentsMatcher.matching(
    JLabelTextMatcher.withLabelText(itemId),
    JLabelTextMatcher.withLabelText(lastPrice.toString),
    JLabelTextMatcher.withLabelText(lastBid.toString),
    JLabelTextMatcher.withLabelText(statusText)
    ))
  }

  def hasColumnTitles(): Unit = {
    val headers = new JTableHeaderDriver(this, classOf[JTableHeader])
    headers.hasHeaders(IterableComponentsMatcher.matching(
    JLabelTextMatcher.withLabelText("Item"),
    JLabelTextMatcher.withLabelText("Last Price"),
    JLabelTextMatcher.withLabelText("Last Bid"),
    JLabelTextMatcher.withLabelText("Status")
    ))
  }
}
