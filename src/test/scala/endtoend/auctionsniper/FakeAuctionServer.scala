package endtoend.auctionsniper

import java.util.concurrent.{ArrayBlockingQueue, TimeUnit}

import auctionsniper.Main
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.{Chat, ChatManagerListener, MessageListener, XMPPConnection}
import org.specs2.matcher.{Matcher, MustMatchers}

/**
 * Created by lidan on 14/01/15.
 */
object FakeAuctionServer {
  val ITEM_ID_AS_LOGIN = "auction-%s"
  val AUCTION_RESOURCE = "Auction"
  val XMPP_HOSTNAME = "localhost"
  private val AUCTION_PASSWORD = "auction"
}

class FakeAuctionServer(val itemId: String) extends MustMatchers {
  import endtoend.auctionsniper.FakeAuctionServer._

  private[this] val connection = new XMPPConnection(XMPP_HOSTNAME)
  private[this] var currentChat: Option[Chat] = None
  private[this] val messageListener = new SingleMessageListener()

  def startSellingItem(): Unit = {
    connection.connect()
    connection.login(ITEM_ID_AS_LOGIN.format(itemId), AUCTION_PASSWORD, AUCTION_RESOURCE)
    connection.getChatManager.addChatListener(new ChatManagerListener {
      override def chatCreated(chat: Chat, createdLocally: Boolean): Unit = {
        currentChat = Some(chat)
        chat.addMessageListener(messageListener)
      }
    })
  }

  def reportPrice(price: Int, increment: Int, bidder: String): Unit = {
    currentChat.foreach(_.sendMessage("SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;".format(price, increment, bidder)))
  }

  def hasReceivedJoinRequestFrom(sniperId: String) = {
    receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT))
  }

  def hasReceivedBid(bid: Int, sniperId: String): Unit = {
    receivesAMessageMatching(sniperId, equalTo(Main.BID_COMMAND_FORMAT.format(bid)))
  }

  def receivesAMessageMatching[T >: String](sniperId: String, messageMatcher: Matcher[T]): Unit = {
    messageListener.receivesAMessage(messageMatcher)
    currentChat.foreach(_.getParticipant must equalTo(sniperId))
  }

  def announceClosed(): Unit = {
    currentChat.foreach(_.sendMessage("SOLVersion: 1.1; Event: CLOSE;"))
  }

  def stop(): Unit = {
    connection.disconnect()
  }

  class SingleMessageListener extends MessageListener {
    private[this] val messages = new ArrayBlockingQueue[Message](1)

    override def processMessage(chat: Chat, message: Message): Unit = {
      messages.add(message)
    }

    def receivesAMessage[T >: String](messageMatcher: Matcher[T]) = {
      val msg = messages.poll(5, TimeUnit.SECONDS)
      msg must not beNull
      val body = msg.getBody
      body must messageMatcher
    }
  }
}