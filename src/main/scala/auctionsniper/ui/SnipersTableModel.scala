package auctionsniper.ui

import javax.swing.table.AbstractTableModel

/**
 * Created by lidan on 01/02/15.
 */
class SnipersTableModel extends AbstractTableModel {
  private var statusText = MainWindow.STATUS_JOINING

  override def getRowCount: Int = 1

  override def getColumnCount: Int = 1

  override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = statusText

  def setStatusText(newStatusText: String): Unit = {
    statusText = newStatusText
    fireTableRowsUpdated(0, 0)
  }
}
