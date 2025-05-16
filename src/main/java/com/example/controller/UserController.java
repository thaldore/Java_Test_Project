package com.example.controller;

import java.util.ArrayList; // Dinamik liste işlemleri için
import java.util.List; // Liste işlemleri için

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;

@RestController // Bu sınıfın bir REST kontrolcüsü olduğunu belirtir
@RequestMapping("/api") // Tüm endpoint'ler /api ile başlar
public class UserController {
    
    private List<User> users = initializeUsers();

    // Başlangıç kullanıcı verilerini oluşturur
    private List<User> initializeUsers() {
        return new ArrayList<>(List.of(
            // 20 örnek kullanıcı oluşturuluyor
            new User(1, "Ahmet Yılmaz", "ahmet@example.com"),
            new User(2, "Mehmet Demir", "mehmet@example.com"),
            new User(3, "Ayşe Kaya", "ayse@example.com"),
            new User(4, "Fatma Şahin", "fatma@example.com"),
            new User(5, "Mustafa Öztürk", "mustafa@example.com"),
            new User(6, "Emine Çelik", "emine@example.com"),
            new User(7, "Hüseyin Arslan", "huseyin@example.com"),
            new User(8, "Zeynep Koç", "zeynep@example.com"),
            new User(9, "İbrahim Yıldız", "ibrahim@example.com"),
            new User(10, "Hatice Kaplan", "hatice@example.com"),
            new User(11, "Osman Tekin", "osman@example.com"),
            new User(12, "Seda Erdem", "seda@example.com"),
            new User(13, "Kemal Aydın", "kemal@example.com"),
            new User(14, "Elif Güler", "elif@example.com"),
            new User(15, "Cemal Bulut", "cemal@example.com"),
            new User(16, "Sevim Özdemir", "sevim@example.com"),
            new User(17, "Murat Karaca", "murat@example.com"),
            new User(18, "Gülşah Yılmaz", "gulsah@example.com"),
            new User(19, "Volkan Doğan", "volkan@example.com"),
            new User(20, "Leyla Aktaş", "leyla@example.com")
        ));
    }

    @GetMapping("/users") // GET /api/users için işleyici
    public List<User> getAllUsers() {
        return users; // Tüm kullanıcı listesini döndürür
    }

    @PostMapping("/users") // POST /api/users için işleyici
    public User addUser(@RequestBody User newUser) {
        users.add(newUser); // Yeni kullanıcı ekler
        return newUser; // Eklenen kullanıcıyı döndürür
    }

    @PutMapping("/users/{id}") // PUT /api/users/{id} için işleyici
    public User updateUser(@PathVariable("id") int id, @RequestBody User updatedUser) {
        return users.stream()
                .filter(user -> user.getId() == id) // ID'ye göre filtrele
                .findFirst()
                .map(user -> {
                    // Kullanıcı bilgilerini güncelle
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    return user;
                })
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + id));
    }

    @DeleteMapping("/users/{id}") // DELETE /api/users/{id} için işleyici
    public String deleteUser(@PathVariable("id") int id) {
        users.removeIf(user -> user.getId() == id); // ID'ye göre kullanıcı sil
        return "Kullanıcı silindi: " + id;
    }

    @GetMapping("/users/reset") // GET /api/users/reset için işleyici
    public String resetUsers() {
        users = initializeUsers(); // Verileri başlangıç durumuna sıfırla
        return "Veriler sıfırlandı!";
    }
}