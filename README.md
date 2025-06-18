# 🌡️ SMART TEMPERATURE & HUMIDITY MONITORING APP

This Android app enables **real-time monitoring of temperature and humidity**, and **remote LED control** using Firebase. Perfect for smart home and IoT automation projects.

---

## 1. 🔥 FEATURES

- 📡 Real-time monitoring of temperature and humidity via ESP32 sensors
- 💡 Remote control to turn LED on/off via app
- ☁️ Firebase integration for cloud-based data synchronization
- 🏠 Smart home ready – easily adaptable to IoT systems

---

## 2. 📱 APP DEMO

| App UI | Firebase Database | ESP32 & Sensor |
|--------|-------------------|----------------|
| <img src="https://github.com/user-attachments/assets/78c19ba1-7df3-468e-840f-49366538138a" width="200"/> | <img src="https://github.com/user-attachments/assets/6e165d5e-f56b-402b-9f74-3618a6f45905" width="200"/> | <img src="https://github.com/user-attachments/assets/827856b9-8fab-49b0-a3c7-9370f5360036" width="200"/> |
 

🎥 **Demo video**: [[Watch on YouTube](https://www.youtube.com/watch?v=your-video-id)](https://youtu.be/XZl87-dAdTg?si=AB-KasloOCUd7sy5)
📦  Download APK: [Click here to download](https://drive.google.com/file/d/1ln_NeGqV1gBH3xlCzrCAfBNyY6q4rKQj/view?usp=sharing)
---

## 3. 🛠️ TECHNOLOGIES USED

- Android (Kotlin)
- ESP32 + DHT11/DHT22 Sensor
- Firebase Realtime Database
- Arduino IDE + Firebase Library
- Gradle Kotlin DSL

---

## 4. ⚙️ HOW TO BUILD & RUN

### 4.1. Requirements:
- Android Studio (Electric Eel or later)
- Firebase project with Realtime Database enabled
- ESP32 board, LED, DHT22 sensor

### 4.2. Steps: git clone https://github.com/0862897614/smart-temp-humidity-app.git


- Open the project in Android Studio (`File > Open`)
- Connect Firebase or replace the `google-services.json` file
- Run the app on a physical device (API 21+)
- Upload code to ESP32 from the `ESP32SensorProject` folder using Arduino IDE

---

## 5. 🔌 HARDWARE CONNECTION TABLE

| Component        | ESP32 Pin  |
|------------------|------------|
| DHT22 (Signal)   | D4         |
| LED (Anode)      | D2         |
| GND              | GND        |
| VCC (3.3V/5V)    | 3.3V       |



---

## 6. 📄 LICENSE

This project is licensed under the **MIT License** – feel free to use and modify it with proper attribution.
