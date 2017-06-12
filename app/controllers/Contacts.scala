package controllers

import javax.inject.Inject

import models.ContactService
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class Contacts @Inject()(contactService: ContactService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = Action { implicit request =>
    val contacts = contactService.all

    Ok(views.html.index(contacts, contactService.form))
  }

  def create = Action { implicit request =>
    contactService.form.bindFromRequest.fold(
      errors => BadRequest(views.html.index(contactService.all, errors)),
      contact => {
        contactService.create(contact)
        Redirect(routes.Contacts.index())
      }
    )
  }

  def edit(id: Long) = Action { implicit request =>
    val contact = contactService.get(id)
    Ok(views.html.edit(id, contactService.form.fill(contact)))
  }

  def update(id: Long) = Action { implicit request =>
    contactService.form.bindFromRequest.fold(
      errors => BadRequest(views.html.edit(id, errors)),
      contact => {
        contactService.update(id, contact)
        Redirect(routes.Contacts.index())
      }
    )
  }

  def delete(id: Long) = Action {
    contactService.delete(id)
    Redirect(routes.Contacts.index())
  }
}
