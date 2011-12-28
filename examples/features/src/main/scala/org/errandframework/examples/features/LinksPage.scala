package org.errandframework.examples.features


import FeaturesExampleApplication._
import org.errandframework.components.pages.{PageLink, PageResponse, Page}
import org.errandframework.http._
import org.errandframework.components.{Image, ActionLink, Link}
import ResourceServerControllerProvider.urlForResource
import DynamicControllerProvider.{registerController, urlForControllerId}

/**
 * Demonstrates various types of links that are available.
 * Note that we can pass parameters when instantiating pages.  We can instantiate the same Page subclass from
 * different URLs but have it behave differently.
 */
class LinksPage(private var from: String) extends FeaturePage {

  def featureContent = {
    <p><i>You've arrived at this page from {from}.</i></p>
    <p>There are several different types of links.</p>
    <ul>
      <li>{simpleLink.render}</li>
      <li>{pageLink.render}</li>
      <li>{actionLink.render}</li>
      <li>{arbitraryControllerLink.render}</li>
    </ul>
    <p>
      The content of the link can be any component, not just a string.
    </p>
    <ul>
      <li>{stringContentLink.render}</li>
      <li>{xhtmlContentLink.render}</li>
      <li>{imageContentLink.render}</li>
    </ul>
    <p>
      You can define links as vals, or just define them on the fly.  You can even just define a regular A tag.
      No need to use components if plain old HTML will do.  You can resolve the link URL dynamically even if you're
      not using components.
    </p>
    <ul>
      <li>{valLink.render}</li>
      <li>{Link("Link defined on the fly", linksLocation.toUrl()).render}</li>
      <li><a href="http://www.google.com">Plain old HTML link</a></li>
      <li><a href={if (from.contains("links2")) linksLocation.toUrl() else links2Location.toUrl()}>Plain old HTML link with dynamically-generated URL</a></li>
    </ul>
  }

  override def title = "Links"

  // Note that all of these components are instantiated as soon as the page is instantiated.
  // We could make them lazy val to instantiate each one the first time it's used, or def to instantiate a new
  // component each time it's used.  The difference is that as vals, the components are instantiated before the
  // any parameters are parsed from the request (since the page has to exist before PageController can find out what
  // parameters it's expecting) and before the PageResponse is instantiated.  As lazy vals or defs, the components
  // will be instantiated while the PageResponse is being sent.  Another difference is that components defined as
  // lazy val or def will not be instantiated unless they are actually used.

  private val simpleLink = Link("The Link class defines a simple link to a URL", linksLocation.toUrl())

  private val pageLink = PageLink("PageLink is a link to a specific page (which may not yet exist)",
    new LinksPage("the PageLink"))

  private val actionLink = ActionLink("ActionLink invokes a callback when the user clicks it") {

    // This code runs when the user clicks the link.  It must return a Response.
    // Here we just return a PageResponse so the page containing the link is re-rendered.
    // Note that the page content will be the same, but the browser will have the URL of the link.  We'd have to
    // return RedirectResponse(linksLocation.toUrl()) to go back to the original URL--but then we'd lose the "from"
    // change because the original URL maps to a new LinksPage instance.  In order to both keep the URL and track
    // the "from" change, we'd either have to make "from" a SessionVar, or define LinksPage itself as a val or a
    // SessionVar and not regenerate it each time.  It's okay to create a page as a val or SessionVar.  If it's a
    // val, everyone shares the same page instance.  If it's a SessionVar, then each session gets its own instance.

    from += ", followed by the ActionLink"

    PageResponse(LinksPage.this)
  }

  /**
   * An arbitrary controller that just returns some information about the request.
   * PageLink and ActionLink are both just shortcuts for instantiating and registering such a controller
   * and constructing a link to it.  PageLink uses PageController while ActionLink builds a controller from whatever
   * callback you give it.
   */
  private val arbitraryController = new Controller {
    def respond(request: Request) = {
      val parameters = for ((name, values) <- request.parameterMap; value <- values) yield name + "=" + value
      val s = "Request: " + request.method + " " + request.contextServletPath.toComponentString + request.path.toComponentString + "\nParameters:\n" + parameters.mkString("\n")
      CharacterBufferResponse(MediaType("text", "plain"), s)
    }
  }

  private lazy val arbitraryControllerId = registerController(arbitraryController)

  private lazy val arbitraryControllerLink = Link("Using DynamicController we can build links to any controller",
    urlForControllerId(arbitraryControllerId))

  private val stringContentLink = Link("Link with String content", linksLocation.toUrl())

  private val xhtmlContentLink = Link(<span>Link with <b>XHTML</b> content.</span>, linksLocation.toUrl())

  private val imageContentLink = Link(Image(urlForResource(Path(classOf[LinksPage].getPackage) / "ImageLink.png")), linksLocation.toUrl())

  private val valLink = Link("Link defined as a val", linksLocation.toUrl())
}
