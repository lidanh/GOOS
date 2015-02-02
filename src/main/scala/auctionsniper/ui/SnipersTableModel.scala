package auctionsniper.ui

import javax.swing.table.AbstractTableModel

import auctionsniper.SniperSnapshot

import scala.collection.mutable.ArrayBuffer

/**
 * Created by lidan on 01/02/15.
 */
class SnipersTableModel extends AbstractTableModel {
  private val snapshots = new ArrayBuffer[SniperSnapshot]

  this += SniperSnapshot.joining("item 1")

  def +=(snapshot: SniperSnapshot): Unit = {
    snapshots += snapshot
    fireTableRowsInserted(snapshots.length - 1, snapshots.length - 1)
  }

  override def getRowCount: Int = 1

  override def getColumnCount: Int = Column.values.length

  override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = {
    Column.at(columnIndex).value(snapshots(rowIndex))
  }

  def sniperStatusChanged(sniperSnapshot: SniperSnapshot): Unit = {
    snapshots(0) = sniperSnapshot
    fireTableRowsUpdated(0, 0)
  }
}
