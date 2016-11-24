/**
  * Created by Cristina on 11/8/2016.
  */
import java.util._
//remove if not needed
import scala.collection.JavaConversions._

class AdaptableClient(host: String, port: Int, private var client: ObservableClient)
  extends AbstractClient(host, port) {

  override protected def connectionClosed() {
    client.connectionClosed()
  }

  override protected def connectionException(exception: Exception) {
    client.connectionException(exception)
  }

  override protected def connectionEstablished() {
    client.connectionEstablished()
  }

  protected def handleMessageFromServer(msg: AnyRef) {
    client.handleMessageFromServer(msg)
  }
}