package unit.auctionsniper

import auctionsniper.{AuctionEventListener, AuctionMessageTranslator}
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
}

class AuctionMessageTranslatorTest extends Specification with Mockito {
  import unit.auctionsniper.AuctionMessageTranslatorTest._

  trait mock extends Scope {
    val listener = mock[AuctionEventListener]
    val translator = new AuctionMessageTranslator(listener)
  }

  "AuctionMessageTranslator" should {
    "notify auction closed when close message received" in new mock {
      val message = new Message() {
        setBody("SOLVersion: 1.1; Event: CLOSE;")
      }

      translator.processMessage(UNUSED_CHAT, message)

      there was one(listener).auctionClosed()
    }

    "notify bid details when current price message received" in new mock {
      val message = new Message() {
        setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;")
      }

      translator.processMessage(UNUSED_CHAT, message)

      there was one(listener).currentPrice(192, 7)
    }
  }
}
