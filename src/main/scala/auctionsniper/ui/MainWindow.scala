package auctionsniper.ui

import java.awt.Color
import javax.swing.border.LineBorder
import javax.swing.{JFrame, JLabel}

/**
 * Created by lidan on 14/01/15.
 */
class MainWindow extends JFrame("Auction Sniper") {
  import auctionsniper.ui.MainWindow._

  val sniperStatus: JLabel = createLabel(STATUS_JOINING)

  setName(MAIN_WINDOW_NAME)
  add(sniperStatus)
  pack()
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setVisible(true)

  def showStatus(s: String) = {
    sniperStatus.setText(s)
  }
}

object MainWindow {
  val MAIN_WINDOW_NAME = "Auction Sniper Main"
  val SNIPER_STATUS_NAME = "sniper status"

  val STATUS_JOINING = "Joining"
  val STATUS_LOST= "Lost"

  private def createLabel(initialText: String): JLabel = {
    val result = new JLabel(initialText)
    result.setName(SNIPER_STATUS_NAME)
    result.setBorder(new LineBorder(Color.BLACK))
    result
  }
}
