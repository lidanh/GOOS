package auctionsniper.ui

import java.awt.BorderLayout
import javax.swing.{JFrame, JScrollPane, JTable}

/**
 * Created by lidan on 14/01/15.
 */
class MainWindow(val snipers: SnipersTableModel) extends JFrame("Auction Sniper") {
  import auctionsniper.ui.MainWindow._

  setName(MAIN_WINDOW_NAME)
  setTitle(APPLICATION_TITLE)
  fillContentPane(new JTable(snipers) { setName(SNIPERS_TABLE_NAME) })
  pack()
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setVisible(true)

  private def fillContentPane(sniperTable: JTable): Unit = {
    val contentPane = getContentPane
    contentPane.setLayout(new BorderLayout())

    contentPane.add(new JScrollPane(sniperTable), BorderLayout.CENTER)
  }
}

object MainWindow {
  val APPLICATION_TITLE = "Auction Sniper"
  val MAIN_WINDOW_NAME = "Auction Sniper Main"
  val SNIPERS_TABLE_NAME = "snipers table"
}
