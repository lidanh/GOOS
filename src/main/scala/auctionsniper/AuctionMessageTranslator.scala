package auctionsniper

import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.{Chat, MessageListener}

import scala.collection.mutable

/**
 * Created by lidan on 15/01/15.
 */
class AuctionMessageTranslator(private val listener: AuctionEventListener) extends MessageListener {
  override def processMessage(chat: Chat, message: Message): Unit = {
    val event = AuctionEvent from message.getBody

    event match {
      case Close() => listener.auctionClosed()
      case Price(price, increment) => listener.currentPrice(price, increment)
    }
  }
}

private object AuctionEvent {
  def from(messageBody: String): AuctionEvent = {
    val fields = messageBody.split(";").foldLeft(mutable.Map.empty[String, String]) { (acc, field) =>
      val pair = field.split(":")
      acc + (pair(0).trim -> pair(1).trim)
    }

    fields.get("Event") match {
      case Some("CLOSE") => Close()
      case Some("PRICE") =>
        (fields.get("CurrentPrice"), fields.get("Increment")) match {
          case (Some(price), Some(increment)) => Price(price.toInt, increment.toInt)
          case other => throw new UnknownFieldException(other.toString())
        }
      case other => throw new UnknownFieldException(other.toString)
    }
  }
}

trait AuctionEvent
case class Close() extends AuctionEvent
case class Price(currentPrice: Int, increment: Int) extends AuctionEvent
private class UnknownFieldException(fieldName: String) extends Exception(s"Unknown field: $fieldName")
