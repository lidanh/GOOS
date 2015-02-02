package auctionsniper.ui

import java.awt.BorderLayout
import javax.swing.{JFrame, JScrollPane, JTable}

import auctionsniper.SniperSnapshot

/**
 * Created by lidan on 14/01/15.
 */
class MainWindow extends JFrame("Auction Sniper") {
  import auctionsniper.ui.MainWindow._

  private val snipers = new SnipersTableModel()

  setName(MAIN_WINDOW_NAME)
  fillContentPane(makeSnipersTable)
  pack()
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setVisible(true)

  private def fillContentPane(sniperTable: JTable): Unit = {
    val contentPane = getContentPane
    contentPane.setLayout(new BorderLayout())

    contentPane.add(new JScrollPane(sniperTable), BorderLayout.CENTER)
  }

  private def makeSnipersTable: JTable = {
    new JTable(snipers) {
      setName(SNIPERS_TABLE_NAME)
    }
  }

  def sniperStatusChanged(snapshot: SniperSnapshot): Unit = {
    snipers.sniperStatusChanged(snapshot)
  }
}

object MainWindow {
  val MAIN_WINDOW_NAME = "Auction Sniper Main"
  val SNIPERS_TABLE_NAME = "snipers table"
}
