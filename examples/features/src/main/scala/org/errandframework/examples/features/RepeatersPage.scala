package org.errandframework.examples.features

import org.errandframework.components.{TableView, ListView}


/**
 * A page demonstrating the ListView and TableView repeaters.
 */
class RepeatersPage extends FeaturePage {

  def featureContent = {
    <div>
      <h2>ListView</h2>
      {numbersView.render}
      <h2>TableView</h2>
      {peopleView.render}
    </div>
  }

  private val numbersView = new ListView(1 to 10) {
    def content(item: Int) = "Item " + item
  }

  private val people = Seq(
    Person("Willis Blackburn", "wboyce@signetworks.com", 40, "New York, NY"),
    Person("Barack Obama", "president@whitehouse.gov", 50, "Washington, DC"),
    Person("Bruce Springsteen", "bruce@springsteen.net", 62, "Colts Neck, NJ"),
    Person("Bill Gates", "billg@microsoft.com", 56, "Medina, WA")
  )

  private val peopleView = new TableView(people) {
    val columns = Seq(
      Column("Name", _.name),
      Column("Email", p => <a href={"mailto:" + p.email}>{p.email}</a>),
      Column("Age", _.age.toString),
      Column("City", _.city)
    )
  }

  override def title = "Repeaters"
}

case class Person(name: String, email: String, age: Int, city: String)