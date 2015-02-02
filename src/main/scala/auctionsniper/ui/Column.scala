package auctionsniper.ui

import auctionsniper.SniperSnapshot

/**
 * Created by lidan on 01/02/15.
 */
sealed abstract class Column(val name: String) {
  lazy val ordinal = Column.values.indexOf(this)
  def value(snapshot: SniperSnapshot): AnyRef
}

object Column {
  lazy val values = List(ItemIdentifier, LastPrice, LastBid, SniperStatus)

  object ItemIdentifier extends Column("Item") {
    override def value(snapshot: SniperSnapshot) = snapshot.itemId
  }

  object LastPrice extends Column("Last Price") {
    override def value(snapshot: SniperSnapshot) = snapshot.lastPrice.asInstanceOf[AnyRef]
  }

  object LastBid extends Column("Last Bid") {
    override def value(snapshot: SniperSnapshot) = snapshot.lastBid.asInstanceOf[AnyRef]
  }

  object SniperStatus extends Column("Status") {
    override def value(snapshot: SniperSnapshot) = snapshot.state.toString
  }

  def at(offset: Int) = values(offset)
}
