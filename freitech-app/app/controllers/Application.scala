package controllers

import play.api.i18n.MessagesApi
import javax.inject.Inject

class Application @Inject()(val messagesApi: MessagesApi) extends ControllerBase{
  
}