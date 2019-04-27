package kbrent.FMSDesign.Model;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * stores all info for any given person
 */
public class Person {

  private String Descendant;
  private String PersonID;
  private String Firstname;
  private String Lastname;
  private String Gender;
  private String FatherID;
  private String MotherID;
  private String SpouseID;

  public Person(String Descendant, String PersonID, String Firstname, String Lastname, String Gender,
               String FatherID, String MotherID, String SpouseID) {
    this.Descendant = Descendant;
    this.PersonID = PersonID;
    this.Firstname = Firstname;
    this.Lastname = Lastname;
    this.Gender = Gender;
    this.FatherID = FatherID;
    this.MotherID = MotherID;
    this.SpouseID = SpouseID;
  }

  public void setDescendant(String Descendant) {this.Descendant = Descendant;}

  public void setGender(String Gender) {this.Gender = Gender;}

  public String getPersonId(){
    return PersonID;
  }

  public String getDescendant(){
    return Descendant;
  }

  public String getFirstName() {
    return Firstname;
  }

  public String getLastName() {
    return Lastname;
  }

  public String getGender() {
    return Gender;
  }

  public String getFatherId(){
    return FatherID;
  }

  public String getMotherId() {
    return MotherID;
  }

  public String getSpouseId() {
    return SpouseID;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (o instanceof Person) {
      Person oPerson = (Person) o;
      return oPerson.getPersonId().equals(getPersonId()) &&
              oPerson.getSpouseId().equals(getSpouseId()) &&
              oPerson.getMotherId().equals(getMotherId()) &&
              oPerson.getDescendant().equals(getDescendant()) &&
              oPerson.getFatherId().equals(getFatherId()) &&
              oPerson.getFirstName().equals(getFirstName()) &&
              oPerson.getGender().equals(getGender()) &&
              oPerson.getLastName().equals(getLastName());
    }
    return false;
  }

}
