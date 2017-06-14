package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.ContactService
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc._
import utils.auth.{ DefaultEnv, WithProvider }

import scala.concurrent.Future

class Contacts @Inject() (contactService: ContactService, val messagesApi: MessagesApi, silhouette: Silhouette[DefaultEnv]) extends Controller with I18nSupport {

  def index = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit request =>
    contactService.all.map(contacts => Ok(views.html.index(contacts, contactService.form)))
  }

  def create = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit request =>
    contactService.form.bindFromRequest.fold(
      errors => contactService.all.map(contacts => BadRequest(views.html.index(contacts, errors))),
      contact => {
        contactService.create(contact)
        Future(Redirect(routes.Contacts.index()))
      }
    )
  }

  def edit(id: Long) = Action.async { implicit request =>
    contactService.get(id).map(contact => Ok(views.html.edit(id, contactService.form.fill(contact))))
  }

  def update(id: Long) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)) { implicit request =>
    contactService.form.bindFromRequest.fold(
      errors => BadRequest(views.html.edit(id, errors)),
      contact => {
        contactService.update(id, contact)
        Redirect(routes.Contacts.index())
      }
    )
  }

  def delete(id: Long) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)) {
    contactService.delete(id)
    Redirect(routes.Contacts.index())
  }
}
