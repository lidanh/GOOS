package endtoend.auctionsniper

import auctionsniper.ui.MainWindow
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.{ComponentDriver, JFrameDriver, JTableDriver}
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.JLabelTextMatcher
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
//    new JLabelDriver(this, ComponentDriver.named(MainWindow.SNIPER_STATUS_NAME)).hasText(equalTo(statusText))
    new JTableDriver(this).hasCell(JLabelTextMatcher.withLabelText(equalTo(statusText)))
  }
}
