# Mesajlaşma Uygulaması Projesi

## Proje Amacı

Bu proje, kullanıcıların birbirleriyle iletişim kurabilmesini sağlayan bir mesajlaşma uygulamasıdır. Uygulama, kullanıcıların arkadaş eklemelerini, arkadaşlarıyla mesajlaşmalarını ve bu mesajlaşmaların MongoDB tabanlı bir altyapı ile yönetilmesini sağlar.

## Çalışma Prensibi

Projenin temel işleyişi aşağıda adım adım açıklanmıştır:

### 1. Kullanıcı Girişi ve Arkadaş Listesi
- Kullanıcılar, uygulama üzerinde oturum açabilir ve arkadaş listelerini görüntüleyebilir.
- Kullanıcılar, başka bir kullanıcıyı kullanıcı adı ile arayarak arkadaş olarak ekleyebilir.
- Kullanıcılar, yeni bir arkadaş eklediğinde "Chats" koleksiyonunda yeni bir 'Chat' oluşur.


![Ekran görüntüsü 2024-12-16 184816](https://github.com/user-attachments/assets/0f9f4866-fe97-41e2-9672-d0ea766a4ae2)

*Yukarıdaki ekran, kullanıcıların giriş yapabileceği ekranı göstermektedir.*


![mainPage](https://github.com/user-attachments/assets/3dca178c-e60c-4585-828c-041b788c9e7c)

*Yukarıdaki ekran, uygulamanın ana ekranıdır.*

### 2. Sohbet Başlatma ve Mesajlaşma
- Kullanıcılar, arkadaş listesinden kullanıcı seçip onunla sohbet başlatabilir.
- Tüm mesajlar MongoDB veri tabanındaki "Chats" koleksiyonunda saklanır.


![chat](https://github.com/user-attachments/assets/a225a4dd-f4ff-46cc-a9f2-f6a0f037e037)

*Bu ekran, arkadaşlarınızla sohbet etmek için kullanılan arayüzdür.*

### 3. Veritabanı Kullanımı
- Kullanıcı bilgileri, arkadaş listeleri ve mesajlaşmalar MongoDB veri tabanında JSON dokümanları olarak depolanır.
- Bu sayede veriler esnek ve ölçeklenebilir bir yapıda saklanır.


![WhatsApp Image 2024-12-16 at 17 45 03 (1)](https://github.com/user-attachments/assets/b437ab7a-43fe-4705-9a77-5a567e660a3f)
![WhatsApp Image 2024-12-16 at 17 45 03](https://github.com/user-attachments/assets/861acac1-b2f4-4739-99a9-d257077d81d4)

*Veritabanı yapısını temsil eden bir ekran görüntüleri.*

### 4. Gerçek Zamanlı Mesaj Takibi
- MongoDB Change Stream kullanılarak, gelen yeni mesajlar ve yapılan değişiklikler gerçek zamanlı olarak izlenebilir.
- Kullanıcılar, mesajlar geldiğinde uygulamayı yenilemeden anında görüntüleyebilirler.


## Kullanılan Teknolojiler

Bu projede aşağıdaki teknolojiler kullanılmıştır:
- **Java**: Projenin ana dili olarak kullanılmıştır. Java, güçlü nesne yönelimli özellikleri ve geniş kütüphane desteğiyle uygulama geliştirmede sık tercih edilen bir dil.
- **Swing**: Kullanıcı arayüzü tasarımı için tercih edilmiştir. Swing, Java'da GUI (Graphical User Interface) oluşturmak için kullanılan bir kütüphanedir ve masaüstü uygulamaları için etkileşimli ve zengin arayüzler sağlar.
- **MongoDB Atlas**: veritabanlarını bulut üzerinde kolayca yönetebileceğiniz bir platform. Altyapı, yedekleme, güvenlik ve izleme gibi işlemler otomatik olarak yapılıyor, yani bunlarla uğraşmak zorunda kalmıyorsunuz. Ayrıca, yüksek performans sunuyor ve ihtiyaca göre kolayca ölçeklenebiliyor. Kısacası, veritabanınızı yönetmek çok daha basit ve pratik hale geliyor.
- **Maven**: Projedeki bağımlılık yönetimi için kullanılmıştır. Maven, Java projelerinde dış kütüphanelerin ve yapılandırmaların yönetilmesine yardımcı olan bir araçtır, bu da projenin derlenmesini ve dağıtılmasını daha verimli hale getirir.

## Kurulum ve Çalıştırma Rehberi

### Gerekli Araçlar
- **Java JDK 11 veya üstü**: Projenin çalışması için gereklidir.
- **MongoDB Atlas Hesabı**: Bulut tabanlı MongoDB kullanmak için gereklidir.
- **IntelliJ IDEA**: Projeyi derlemek ve geliştirmek için önerilen IDE.

### Kurulum Adımları

1. **Proje Kaynak Kodlarını Edinme**
   - Proje kaynak kodlarını GitHub üzerinden veya paylaşılan dosyadan bilgisayarınıza indirin.

2. **MongoDB Bağlantı Ayarları**
   - `ChatService` sınıfında ve birkaç farklı yerde yer alan `connectionString` değerini kendi MongoDB Atlas bağlantı bilgilerinize göre güncelleyin. Ya da mevcut projemizin veri tabanını kullanabilirsiniz.

3. **Projeyi Derleme ve Çalıştırma**
   - IntelliJ IDEA'da projeyi açın.
   - Maven bağımlılıklarının indirildiğinden emin olun.
   - `Main` sınıfını çalıştırarak uygulamayı başlatın.

## Kullanım Rehberi

1. **Kullanıcı Girişi**
   - Uygulama açıldığında, giriş ekranına yönlendirilirsiniz. Burada kullanıcı adı ve şifrenizi girerek giriş yapabilirsiniz.
   - Eğer uygulamaya kayıtlı değilseniz "Sign Up" butonuna tıklayarak kayıt olabileceğiniz ekrana yönlendirilirsiniz, burda kayıt olduktan sonra uygulamaya giriş yapabilirsiniz.
   - 'admin' kullanıcı adına sahip yeni bir kullanıcı oluşturarak, admin panele ulaşabilirsiniz. 

2. **Arkadaş Ekleme ve Silme**
   - Arkadaşınızın kullanıcı adını girerek ekleme yadad silme işlemini gerçekleştirebilirsiniz.
   - Ana sayfada "Add Friend" butonuna tıklayarak yeni bir arkadaş ekleyebilirsiniz.
   - Ana sayfada "Remove Friend" butonuna tıklayarak mevcut olan arkadaşınızı silebilirsiniz.

3. **Mesajlaşma**
   - Arkadaş listenizden birine tıklayarak sohbet ekranına geçebilirsiniz.
   - Mesajlarınızı yazıp gönderdiğinizde, mesajlar anında karşı tarafa iletilir.

4. **Gerçek Zamanlı Mesaj Takibi**
   - Mesajlar otomatik olarak güncellenir ve yeniden yüklemeye gerek kalmaz. Uygulama, gelen mesajları gerçek zamanlı olarak izler.

## Projenin Genişletilebilirliği

Bu proje ilerleyen aşamalarda aşağıdaki özelliklerle geliştirilebilir:
- **Grup Sohbetleri**: Kullanıcılar birden fazla kişiyle grup sohbeti oluşturabilir.
- **Dosya Paylaşımı**: Kullanıcılar, sohbetlerde dosya paylaşımı yapabilirler.
- **Bildirim Sistemi**: Gelen yeni mesajlar için bildirim sistemi entegre edilebilir.
- **Mobil Uygulama Desteği**: Proje, bir mobil uygulama için de geliştirilebilir.

---

## Sonuç

Bu proje, kullanıcıların arkadaşlarıyla güvenli ve verimli bir şekilde iletişim kurmasına olanak sağlayan bir platform sunmaktadır. MongoDB altyapısı ile verilerin depolanması ve esnekliği, gerçek zamanlı mesaj takibi, kullanıcı dostu arayüz ve genişletilebilir yapısı ile farklı ihtiyaçlara cevap verebilecek bir çözüm sunmaktadır.
