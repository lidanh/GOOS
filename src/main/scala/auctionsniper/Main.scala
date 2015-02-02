package auctionsniper

import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing.SwingUtilities

import auctionsniper.ui.MainWindow
import org.jivesoftware.smack.{Chat, XMPPConnection, XMPPException}

/**
 * Created by lidan on 14/01/15.
 */
class Main {
  import auctionsniper.Main._

  private[this] var ui: Option[MainWindow] = None
  private[this] var notToBeGCd: Option[Chat] = None

  startUserInterface()

  private def startUserInterface(): Unit = {
    SwingUtilities.invokeAndWait(new Runnable {
      override def run(): Unit = {
        ui = Some(new MainWindow())
      }
    })
  }

  private def joinAuction(connection: XMPPConnection, itemId: String): Unit = {
    disconnectWhenUICloses(connection)

    val chat = connection.getChatManager.createChat(auctionId(itemId, connection), null)
    notToBeGCd = Some(chat)

    val auction = new XMPPAuction(chat)

    chat.addMessageListener(new AuctionMessageTranslator(connection.getUser, new AuctionSniper(itemId, auction, new SniperStateDisplayer())))

    auction.join()
  }

  private def disconnectWhenUICloses(connection: XMPPConnection): Unit = {
    ui.foreach(_.addWindowListener(new WindowAdapter {
      override def windowClosed(e: WindowEvent): Unit = connection.disconnect()
    }))
  }

  def auctionId(itemId: String, connection: XMPPConnection): String = {
    AUCTION_ID_FORMAT.format(itemId, connection.getServiceName)
  }

  class XMPPAuction(private val chat: Chat) extends Auction {
    override def bid(amount: Int): Unit = {
      sendMessage(BID_COMMAND_FORMAT.format(amount))
    }

    override def join(): Unit = {
      sendMessage(JOIN_COMMAND_FORMAT)
    }

    private[this] def sendMessage(message: String): Unit = {
      try {
        chat.sendMessage(message)
      } catch {
        case e: XMPPException => e.printStackTrace()
      }
    }
  }

  class SniperStateDisplayer extends SniperListener {
    override def notify(snapshot: SniperSnapshot): Unit = {
      SwingUtilities.invokeLater(new Runnable {
        override def run(): Unit = {
          ui.foreach(_.sniperStatusChanged(snapshot))
        }
      })
    }
  }
}

object Main extends App {
  private val ARG_HOSTNAME = 0
  private val ARG_USERNAME = 1
  private val ARG_PASSWORD = 2
  private val ARG_ITEM_ID = 3

  val AUCTION_RECOURSE = "Auction"
  val ITEM_ID_AS_LOGIN = "auction-%s"
  val AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RECOURSE

  val BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;"
  val JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;"

  val main = new Main
  main.joinAuction(connectTo(args(ARG_HOSTNAME), args(ARG_USERNAME), args(ARG_PASSWORD)), args(ARG_ITEM_ID))

  private def connectTo(hostname: String, username: String, password: String): XMPPConnection = {
    val connection = new XMPPConnection(hostname)
    connection.connect()
    connection.login(username, password, AUCTION_RECOURSE)

    connection
  }
}
