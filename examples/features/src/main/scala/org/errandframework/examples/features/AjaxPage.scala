package org.errandframework.examples.features

import org.errandframework.components.{ActionLink, IdBehavior, Component}
import org.errandframework.components.ajax.{AjaxResponse, AjaxLinkBehavior}

/**
 * A simple AJAX example.
 */
class AjaxPage extends FeaturePage {

  def featureContent = {
    <div>
      <p>Errand's AJAX support allows the application to partially update a page in response to an event.
        In order to use AJAX, the application must:</p>
      <ol>
        <li>Define one or more components with IdBehavior. IdBehavior gives the component an XML ID so that
          it may be targeted for update later.</li>
        <li>Add one of the AJAX behaviors to the component that will trigger the update.
            <ul>
              <li>AjaxLinkBehavior transforms a link into an AJAX link.  Upon being clicked, instead of loading the
                link URL as a page, the link will make an asynchronous request to the link URL, which must
                return a set of XHTML tags.  These tags will replace the elements with the same IDs in the page.
                AjaxLinkBehavior works well with ActionLink.</li>
              <li>AjaxButtonBehavior similarly transforms a button into an AJAX button.  When clicked, the button will
                submit the form in which it is contained to the action URL, but instead of displaying the result
                as a new page, the application will handle response in same manner as AjaxLinkBehavior.
                AjaxButtonBehavior works well with ActionButton.</li>
              <li>AjaxFormActionBehavior is the most complex AJAX behavior.  It can attach an AJAX action to
                any event and can submit a set of fields from the containing form along with the request.  It
                can also delay a certain number of milliseconds before making the AJAX request.</li>
            </ul>
        </li>
        <li>In the controller invoked by the AJAX behavior, execute whatever logic is needed and then return
          an AjaxResponse containing the components that you want to update.  The components don't have to be
          the same Scala objects that you used when originally rendering the page, but they do need to have IDs
          that correspond to the elements that you want to replace.</li>
      </ol>
      <h2>AjaxLinkBehavior</h2>
      {favoritePetDisplay.render}
    </div>
  }

  private var favoritePet: Option[String] = None

  private val favoritePetDisplay = new Component {

    // Note: since we're rendering a custom component, we need to pass the XML through the transform method
    // in order to apply the behaviors.  Component doesn't do this automatically because the XML element to be
    // transformed may not be the first one.

    def render() = transform(favoritePet match {
      case Some(pet) => <div><p>My favorite pet is: {pet}</p><p>{startOverLink.render}</p></div>
      case None => <div><p>What is your favorite pet?</p><p>{dogLink.render} {catLink.render} {platypusLink.render}</p></div>
    })

    override val behaviors = Seq(IdBehavior())
  }

  private val dogLink: ActionLink = ActionLink("Dog", AjaxLinkBehavior) {
    favoritePet = Some("Dog")
    AjaxResponse(AjaxPage.this, favoritePetDisplay)
  }

  private val catLink: ActionLink = ActionLink("Cat", AjaxLinkBehavior) {
    favoritePet = Some("Cat")
    AjaxResponse(AjaxPage.this, favoritePetDisplay)
  }

  private val platypusLink: ActionLink = ActionLink("Platypus", AjaxLinkBehavior) {
    favoritePet = Some("Platypus")
    AjaxResponse(AjaxPage.this, favoritePetDisplay)
  }

  private val startOverLink: ActionLink = ActionLink("Start Over", AjaxLinkBehavior) {
    favoritePet = None
    AjaxResponse(AjaxPage.this, favoritePetDisplay)
  }

  override def title = "AJAX"
}