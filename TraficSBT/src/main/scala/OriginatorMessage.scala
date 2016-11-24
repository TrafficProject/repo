/**
  * Created by Cristina on 11/8/2016.
  */
import scala.beans.BeanProperty
//remove if not needed

class OriginatorMessage(@BeanProperty var originator: ConnectionToClient, @BeanProperty var message: AnyRef)

