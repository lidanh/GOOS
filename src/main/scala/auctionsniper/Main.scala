package auctionsniper

import javax.swing.SwingUtilities

import auctionsniper.ui.MainWindow
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.{Chat, MessageListener, XMPPConnection}

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
    val chat = connection.getChatManager().createChat(
      auctionId(itemId, connection),
      new MessageListener() {
        override def processMessage(chat: Chat, message: Message): Unit = {
          SwingUtilities.invokeLater(new Runnable {
            override def run(): Unit = ui.foreach(_.showStatus(MainWindow.STATUS_LOST))
          })
        }
      })

    notToBeGCd = Some(chat)
    chat.sendMessage(new Message())
  }

  def auctionId(itemId: String, connection: XMPPConnection): String = {
    AUCTION_ID_FORMAT.format(itemId, connection.getServiceName)
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

  val main = new Main
  main.joinAuction(connectTo(args(ARG_HOSTNAME), args(ARG_USERNAME), args(ARG_PASSWORD)), args(ARG_ITEM_ID))

  private def connectTo(hostname: String, username: String, password: String): XMPPConnection = {
    val connection = new XMPPConnection(hostname)
    connection.connect()
    connection.login(username, password, AUCTION_RECOURSE)

    connection
  }
}
