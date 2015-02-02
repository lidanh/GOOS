package unit.auctionsniper

import javax.swing.event.TableModelListener

import auctionsniper.ui.{Column, SnipersTableModel}
import auctionsniper.{SniperSnapshot, SniperState}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.specification.Scope

/**
 * Created by lidan on 01/02/15.
 */
class SnipersTableModelTest extends Specification with Mockito {
  isolated

  private val model = new SnipersTableModel()

  trait mock extends Scope with Before {
    val listener = mock[TableModelListener]

    override def before: Any = {
      model.addTableModelListener(listener)
    }

    def assertColumnEquals(column: Column, expected: Any) = {
      model.getValueAt(0, column.ordinal) must_== expected
    }
  }

  "Sniper Table Model" should {
    "has enough columns" in new mock {
      model.getColumnCount must_== Column.values.length
    }

    "sets sniper values in columns" in new mock {
      model.sniperStatusChanged(SniperSnapshot("item id", 555, 666, SniperState.Bidding))

      assertColumnEquals(Column.ItemIdentifier, "item id")
      assertColumnEquals(Column.LastPrice, 555)
      assertColumnEquals(Column.LastBid, 666)
      assertColumnEquals(Column.SniperStatus, SniperState.Bidding)
    }
  }
}
