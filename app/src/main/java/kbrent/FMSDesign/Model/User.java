package kbrent.FMSDesign.Model;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * stores info for a given user
 */
public class User {

  private String Username;
  private String Password;
  private String Email;
  private String Firstname;
  private String Lastname;
  private String Token;
  private String PersonID;
  private String Gender;

  public User( String Username, String Password, String Email, String Firstname,
           String Lastname, String Token, String PersonID, String Gender){
    this.Username = Username;
    this.Password = Password;
    this.Email = Email;
    this.Firstname = Firstname;
    this.Lastname = Lastname;
    this.Token = Token;
    this.PersonID = PersonID;
    this.Gender = Gender;
  }

  public void setUserName(String Username) { this.Username = Username; }

  public String getUserName(){
    return Username;
  }

  public String getPassWord(){
    return Password;
  }

  public String getEmail() {
    return Email;
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

  public String getToken() {
    return Token;
  }

  public String getPersonId() {
    return PersonID;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (o instanceof User) {
      User oUser = (User) o;
      return oUser.getPersonId().equals(getPersonId()) &&
              oUser.getUserName().equals(getUserName()) &&
              oUser.getToken().equals(getToken()) &&
              oUser.getLastName().equals(getLastName()) &&
              oUser.getFirstName().equals(getFirstName()) &&
              oUser.getGender().equals(getGender()) &&
              oUser.getPassWord().equals(getPassWord()) &&
              oUser.getEmail().equals(getEmail());
    }
    return false;
  }

}
