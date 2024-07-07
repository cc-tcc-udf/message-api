package org.campus.connect.message.auth.users.records;

public class RegisterDTO {
  private String name;
  private String email;
  private String password;
  private String telefone;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(final String telefone) {
    this.telefone = telefone;
  }
}
