package unit.auctionsniper

import auctionsniper.{AuctionEventListener, AuctionMessageTranslator, FromOtherBidder, FromSniper}
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.specification.Scope

/**
 * Created by lidan on 15/01/15.
 */
object AuctionMessageTranslatorTest {
  val UNUSED_CHAT: Chat = null
  val SNIPER_ID = "sniper"
}

class AuctionMessageTranslatorTest extends Specification with Mockito {
  import unit.auctionsniper.AuctionMessageTranslatorTest._

  trait mock extends Scope {
    val listener = mock[AuctionEventListener]
    val translator = new AuctionMessageTranslator(SNIPER_ID, listener)
  }

  "AuctionMessageTranslator" should {
    "notify auction closed when close message received" in new mock {
      val message = new Message() {
        setBody("SOLVersion: 1.1; Event: CLOSE;")
      }

      translator.processMessage(UNUSED_CHAT, message)

      there was one(listener).auctionClosed()
    }

    "notify bid details when current price message received from other bidder" in new mock {
      val message = new Message() {
        setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;")
      }

      translator.processMessage(UNUSED_CHAT, message)

      there was one(listener).currentPrice(192, 7, FromOtherBidder)
    }

    "notify bid details when current price message received from sniper" in new mock {
      val message = new Message() {
        setBody(s"SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: $SNIPER_ID")
      }

      translator.processMessage(UNUSED_CHAT, message)

      there was one(listener).currentPrice(234, 5, FromSniper)
    }
  }
}
