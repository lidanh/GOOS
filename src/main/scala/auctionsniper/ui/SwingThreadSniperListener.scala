package auctionsniper.ui

import javax.swing.SwingUtilities

import auctionsniper.{SniperListener, SniperSnapshot}

/**
 * Created by lidan on 02/02/15.
 */
class SwingThreadSniperListener(val snipers: SniperListener) extends SniperListener {
  override def sniperStateChanged(snapshot: SniperSnapshot): Unit = {
    SwingUtilities.invokeLater(new Runnable {
      override def run(): Unit = {
        snipers.sniperStateChanged(snapshot)
      }
    })
  }
}
