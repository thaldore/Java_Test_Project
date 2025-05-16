package com.example.model;

public class User {
    private int id;         // Kullanıcı ID'si (primary key)
    private String name;    // Kullanıcının tam adı
    private String email;   // Kullanıcı email adresi

    public User() {} // JSON serileştirme için boş constructor (Spring MVC gerektirir)

    // Parametreli constructor (yeni nesne oluşturmak için)
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getter ve Setter metodları (Spring MVC ve JSON serileştirme için gerekli)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}