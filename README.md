# Restaurant Yönetim Sistemi

## Proje Tanımı
Bu proje, bir restoran yönetim sistemi geliştirmeyi amaçlamaktadır. Kullanıcılar restoranları listeleyebilir, yorum yapabilir ve puan verebilir. Kıdemli kullanıcılar yeni restoran ekleyebilirken, yöneticiler restoran ekleyip silebilir. Yorum ve puanlama, hizmet, lezzet ve fiyat gibi kategorilere ayrılmıştır.

## Özellikler

### 1. Restoran Listeleme ve Detay Görüntüleme
- **Tüm restoranları listeleme**: Kullanıcılar, sistemde tanımlı olan tüm restoranları liste halinde görüntüleyebilir. Restoranların liste görünümünde, kullanıcılar restoranların isimlerini, türlerini ve her restoran için verilmiş ortalama puanı görebilir.
- **Restoran detay sayfası**: Her restoranın kendine özel bir sayfası vardır. Bu sayfada restoranın:
  - Adı,
  - Türü (örneğin, kebapçı, pizzacı, esnaf lokantası),
  - Adresi,
  - Restoranın bir fotoğrafı,
  - Kullanıcıların restoran hakkında yaptığı tüm yorumlar ve verilen puanlar listelenir.

### 2. Yorum ve Puanlama Sistemi
- **Yorum yapma kısıtlaması**: Aynı kullanıcı bir restorana yalnızca bir kez yorum yapabilir ve puan verebilir. Bu, sistemin kullanıcıların sürekli olarak bir restorana tekrar tekrar yorum yapmasını engeller.
- **Puanlama**: Kullanıcılar her restoran için hizmet, lezzet ve fiyat başlıkları altında 1 ile 10 arasında puan verebilir. Her kategori için verilen puanlar restoranın genel puan ortalamasını etkiler.
- **Yorum düzenleme ve silme**: Kullanıcılar daha önce yaptıkları yorumları güncelleyebilir ya da silebilir. Ancak, sadece kendi yorumları üzerinde bu işlemleri yapabilirler.

### 3. Kullanıcı Yönetimi
- **Kayıt olma ve giriş**: Kullanıcılar sisteme kayıt olabilir ve giriş yapabilirler ve her e-posta adresi ve username yalnızca bir kez kullanılabilir.
- **Kullanıcı bilgileri güncelleme**: Kullanıcılar kendi hesap bilgilerini (isim, e-posta, şifre vb.) güncelleyebilirler (api paylaşılırsa).
- **Kimlik doğrulama ve güvenlik**: Projede Spring Security ile güçlü bir kimlik doğrulama sistemi entegre edilmiştir. Kullanıcıların giriş yaptıktan sonra oturumları güvenli bir şekilde yönetilir.

### 4. Yetkilendirme ve Roller
- **Standart Kullanıcı**: Standart kullanıcılar sadece restoranlara yorum yapabilir, puan verebilir ve restoranları görüntüleyebilir.
- **Kıdemli Kullanıcı**: Kıdemli kullanıcılar standart kullanıcı haklarına ek olarak yeni restoranlar ekleyebilir ve mevcut restoranları düzenleyebilir. Ancak, kıdemli kullanıcılar restoran silemez.
- **Yönetici (Admin)**: Yöneticiler, restoran ekleyebilir, düzenleyebilir ve silebilir. Ayrıca, sistemdeki kullanıcıların rollerini değiştirme yetkisine sahiptirler (örneğin, bir kullanıcıyı kıdemli kullanıcı yapabilirler) (api paylaşılırsa).

### 5. Restoran Yönetimi
- **Restoran ekleme**: Yalnızca kıdemli kullanıcılar ve yöneticiler yeni restoran ekleyebilir. Restoran eklenirken restoranın adı, türü, adresi, fotoğrafı gibi bilgiler sisteme girilir.
- **Restoran güncelleme**: Kıdemli kullanıcılar ve yöneticiler mevcut restoranların bilgilerini (örneğin adres ya da fotoğraf) güncelleyebilirler.
- **Restoran silme**: Yalnızca yöneticiler bir restoranı sistemden silebilir. Kıdemli kullanıcılar restoranları silemez.

### 6. Performans ve Verimlilik
- **Ortalama puan hesaplama**: Her restoran için yapılan tüm puanlamaların ortalaması, kullanıcılara sunulmaktadır.

### 7. Güvenlik Özellikleri
- **Spring Security**: Projede Spring Security kullanılarak güçlü bir güvenlik katmanı oluşturulmuştur. Her bir kullanıcının rolüne göre sistemdeki erişim yetkileri belirlenmiştir.

### 8. Özel Sayfalar
- **Ana sayfa**: Sisteme giriş yaptıktan sonra kullanıcılar ana sayfaya yönlendirilir. Burada en popüler restoranlar ya da son eklenen restoranlar gibi bilgiler gösterilir.
- **Kayıt ve Giriş sayfaları**: Kullanıcılar sisteme kayıt olmak ya da giriş yapmak için özel yaratılmış sayfalara yönlendirilir.

## API Endpointleri

### **AuthenticationController**
- `/auth/register` : Kullanıcı kaydı. (**POST**)
- `/auth/login` : Kullanıcı giriş işlemi. (**POST**)
- `/auth/logout` : Oturum kapatma. (**POST**)
- `/auth/current-user` : Geçerli kullanıcıyı alır. (**GET**)

### **UserController**
- `/api/users/create` : Yeni kullanıcı oluşturur. (**POST**)
- `/api/users/get-all` : Tüm kullanıcıları listeler. (**GET**)
- `/api/users/get/{id}` : ID'ye göre kullanıcıyı getirir. (**GET**)
- `/api/users/update/{id}` : Kullanıcıyı günceller. (**PUT**)
- `/api/users/delete/{id}` : Kullanıcıyı siler. (**DELETE**)
- `/api/users/update-role/{id}` : Kullanıcı rolünü günceller. (**PUT**)

### **RestaurantController**
- `/api/restaurants/add` : Yeni restoran ekler. (**POST**)
- `/api/restaurants/get-all` : Tüm restoranları listeler. (**GET**)
- `/api/restaurants/get/{id}` : ID'ye göre restoranı getirir. (**GET**)
- `/api/restaurants/update/{id}` : Restoran bilgilerini günceller. (**PUT**)
- `/api/restaurants/delete/{id}` : Restoranı siler. (**DELETE**)
- `/api/restaurants/average-scores` : Restoranların puan ortalamasını getirir. (**GET**)

### **ReviewController**
- `/api/reviews/add` : Yorum ve puan ekler. (**POST**)
- `/api/reviews/get-all` : Tüm yorumları listeler. (**GET**)
- `/api/reviews/get/{id}` : ID'ye göre yorumu getirir. (**GET**)
- `/api/reviews/update/{id}` : Yorumu günceller. (**PUT**)
- `/api/reviews/delete/{id}` : Yorumu siler. (**DELETE**)

### **RoleController**
- `/api/admin/roles/create` : Yeni rol ekler. (**POST**)
- `/api/admin/roles/get-all` : Tüm rolleri listeler. (**GET**)
- `/api/admin/roles/get/{id}` : ID'ye göre rolü getirir. (**GET**)
- `/api/admin/roles/update/{id}` : Rolü günceller. (**PUT**)
- `/api/admin/roles/delete/{id}` : Rolü siler. (**DELETE**)

### Diğer Endpointler
- `/home` : Ana sayfa. (**GET**, **POST**)
- `/register` : Kayıt sayfası. (**GET**, **POST**)
- `/login` : Giriş sayfası. (**GET**, **POST**)
- `/restaurant/{id}` : Belirli restoran sayfası. (**GET**)
- `/restaurant/edit/{id}` : Restoran düzenleme sayfası. (**GET**)
- `/restaurant/new` : Yeni restoran ekleme sayfası. (**GET**)


## Kullanıcı Rolleri ve Yetkileri
- **Standart Kullanıcı**: Yorum yapabilir, puan verebilir.
- **Kıdemli Kullanıcı**: Yeni restoran ekleyebilir ve düzenleyebilir.
- **Yönetici (Admin)**: Restoran silebilir, kullanıcı rolleri üzerinde değişiklik yapabilir.

## Güvenlik ve Yetkilendirme
Proje, Spring Security kullanarak kimlik doğrulama ve yetkilendirme sağlar. Kullanıcılar rollerine göre farklı erişim seviyelerine sahiptir:
- Standart kullanıcılar yalnızca yorum yapabilir ve puan verebilir.
- Kıdemli kullanıcılar yeni restoran ekleyebilir ve düzenleyebilir.
- Yalnızca yöneticiler restoran silebilir.

**SecurityConfig.java** dosyasında, çeşitli rol ve yetki tanımlamaları yapılmıştır. Kullanıcıların hangi işlemleri gerçekleştirebileceği bu dosyada belirlenmiştir.

## Veritabanı Yapısı
Proje, ilişkisel bir veritabanı kullanır. Veritabanı tabloları arasında kullanıcılar, restoranlar, yorumlar ve roller bulunur.

- **User**: Kullanıcı bilgileri ve rolleri tutulur.
- **Restaurant**: Restoran bilgileri, türü ve adresi.
- **Review**: Yorum ve puanlamalar.
- **Role**: Kullanıcı rolleri (Standart, Kıdemli, Admin).

## Model Hiyerarşisi ve İlişkiler

Proje içerisindeki ana model yapıları ve bu yapıların birbirleriyle olan ilişkileri aşağıda açıklanmıştır:

### 1. User (Kullanıcı)
- **Açıklama**: Sistemdeki kullanıcıları temsil eder.
- **Alanlar**:
  - `id`: Kullanıcının benzersiz kimliği.
  - `username`: Kullanıcı adı.
  - `email`: Kullanıcının e-posta adresi.
  - `password`: Kullanıcının şifresi
  - `reviews`: Kullanıcının yaptığı yorumlar (birçok `Review` nesnesi ile ilişkilidir).
  - `roles`: Kullanıcının sahip olduğu roller (birçok `Role` ile ilişkilidir).
- **İlişkiler**:
  - `@OneToMany` (reviews): Bir kullanıcı, birden fazla yoruma sahip olabilir.
  - `@ManyToMany` (roles): Kullanıcıların birden fazla rolü olabilir.

### 2. Restaurant (Restoran)
- **Açıklama**: Bu sınıf, restoranları temsil eder.
- **Alanlar**:
  - `id`: Restoranın benzersiz kimliği.
  - `name`: Restoranın adı.
  - `type`: Restoranın türü (örneğin, kebapçı, pizzacı vs.).
  - `address`: Restoranın adresi.
  - `photoUrl`: Restoranın fotoğrafı için URL.
  - `reviews`: Restoran ile ilişkili yorumlar.
- **İlişkiler**:
  - `@OneToMany` (reviews): Bir restoran, birçok yoruma sahip olabilir.

### 3. Review (Yorum)
- **Açıklama**: Kullanıcıların restoranlar hakkında yaptığı yorumları temsil eder.
- **Alanlar**:
  - `id`: Yorumun benzersiz kimliği.
  - `comment`: Yorumun içeriği.
  - `serviceScore`: Hizmet puanı (1-10).
  - `tasteScore`: Lezzet puanı (1-10).
  - `priceScore`: Fiyat puanı (1-10).
- **İlişkiler**:
  - `@ManyToOne` (restaurant): Bir yorum, bir restorana aittir.
  - `@ManyToOne` (user): Bir yorum, bir kullanıcı tarafından yapılır.

### 4. Role (Rol)
- **Açıklama**: Sistemdeki kullanıcı rollerini temsil eder (örneğin, standart kullanıcı, kıdemli kullanıcı, admin).
- **Alanlar**:
  - `id`: Rolün benzersiz kimliği.
  - `name`: Rolün adı (örneğin, "ROLE_USER", "ROLE_ADMIN", "ROLE_SENIOR").
- **İlişkiler**:
  - `@ManyToMany` (users): Bir rol, birden fazla kullanıcıya atanabilir.

### Model Hiyerarşisi Özeti
- **User ↔ Role**: Bir kullanıcı birden fazla role sahip olabilir (örneğin, hem standart kullanıcı hem kıdemli kullanıcı olabilir).
- **User ↔ Review**: Bir kullanıcı birçok yorum yapabilir, ancak her yorum bir kullanıcıya aittir.
- **Restaurant ↔ Review**: Bir restoran birçok yoruma sahip olabilir, ancak her yorum bir restorana aittir.



## Kurulum ve Çalıştırma
1. Projeyi klonlayın:
   ```bash
   git clone <repository-url>
   ```
2. Gerekli bağımlılıkları yükleyin:
   ```bash
   mvn install
   ```
3. Veritabanı bağlantı yapılandırmasını yapın (application.properties) (veritabanımı import edin.):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/veritanbanı-adı
   spring.datasource.username=root
   spring.datasource.password=password
   ```
4. Uygulamayı çalıştırın:
   ```bash
   mvn spring-boot:run
   ```

## Teknolojiler
- **Spring Boot 2.7.12**: Hızlı ve kolay yapılandırma ile uygulama geliştirme.
- **Thymeleaf**: Şablon motoru.
- **Spring Security**: Güvenlik katmanı.
- **Spring Data JPA**: Veritabanı yönetimi.
- **Spring Web**: Web uygulamaları ve REST API geliştirme.
- **Spring Validatio**n: Veri doğrulama.
- **MySQL**: Veritabanı yönetimi için kullanılan RDBMS.
- **Maven**: Proje yapısı ve bağımlılık yönetimi.