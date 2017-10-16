package controllers

import play.api.mvc.Controller
import play.api.i18n.I18nSupport
import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits.defaultContext


trait ControllerBase extends Controller with I18nSupport {
}