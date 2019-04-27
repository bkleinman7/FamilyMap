package kbrent.FMSDesign.Model;

public class Token {

    private String Token;
    private String Username;

    public Token(String Token, String Username) {
        this.Token = Token;
        this.Username = Username;
    }

    public void setToken(String Token) {this.Token = Token;}

    public void setUsername(String Username) {this.Username = Username;}

    public String getToken(){
        return Token;
    }

    public String getUsername(){
        return Username;
    }

}
