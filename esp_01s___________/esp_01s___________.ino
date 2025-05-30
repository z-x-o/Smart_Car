#include <PubSubClient.h>
#include <ESP8266WiFi.h>
// 设置WiFi接入信息(请根据您的WiFi信息进行修改)
const char* ssid = "HONOR1";
const char* password = "12345678";
const char* mqttServer = "bemfa.com";

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

void setup() {
  pinMode(LED_BUILTIN, OUTPUT);     // 设置板上LED引脚为输出模式
  digitalWrite(LED_BUILTIN, HIGH);  // 启动后关闭板上LED
  Serial.begin(9600);               // 启动串口通讯
  
  // 设置ESP8266工作模式为无线终端模式
  WiFi.mode(WIFI_STA);
  
  // 连接WiFi
  connectWifi();
  
  // 设置MQTT服务器和端口号
  mqttClient.setServer(mqttServer, 1883);
  // 设置MQTT订阅回调函数
  mqttClient.setCallback(receiveCallback);
  
  // 连接MQTT服务器
  connectMQTTserver();
}

void loop() {
  if (mqttClient.connected()) { // 如果开发板成功连接服务器
    mqttClient.loop();          // 处理信息以及心跳
  } else {                      // 如果开发板未能成功连接服务器
    connectMQTTserver();        // 则尝试连接服务器
  }
}

// 连接MQTT服务器并订阅信息
void connectMQTTserver(){
  // 根据ESP8266的MAC地址生成客户端ID（避免与其它ESP8266的客户端ID重名）
  String clientId = "b56bd871125a42cd827d88fd8205878";// + WiFi.macAddress();
  
  // 连接MQTT服务器
  if (mqttClient.connect(clientId.c_str())) { 
    Serial.println("MQTT Server Connected.");
    Serial.println("Server Address:");
    Serial.println(mqttServer);
    Serial.println("ClientId: ");
    Serial.println(clientId);
    subscribeTopic(); // 订阅指定主题
  } else {
    Serial.print("MQTT Server Connect Failed. Client State:");
    Serial.println(mqttClient.state());
    delay(5000);
  }   
}

// 收到信息后的回调函数
void receiveCallback(char* topic, byte* payload, unsigned int length) {
 // Serial.print("Message Received [");
  //Serial.print(topic);
 // Serial.print("] ");
  
  String receivedMessage = "";
  for (int i = 0; i < length; i++) {
    receivedMessage += (char)payload[i];
  }
  
  Serial.print(receivedMessage);
  //mqttClient.publish(topic, receivedMessage.c_str());
  //Serial.print("Message Length(Bytes): ");
 // Serial.println(length);
  
  if (!strcmp(receivedMessage.c_str(),"open_led")) {     // 如果收到的信息是“open_led”
    digitalWrite(LED_BUILTIN, LOW);        // 则点亮LED。
   // Serial.println("LED ON");
  } 
  else if (!strcmp(receivedMessage.c_str(),"close_led")) { // 如果收到的信息是“close_led”
    digitalWrite(LED_BUILTIN, HIGH);       // 则熄灭LED。
    //Serial.println("LED OFF");
  }
  else {
   // Serial.println("Unknown command.");
  }
}

// 订阅指定主题
void subscribeTopic(){
  // 建立订阅主题。主题名称以Taichi-Maker-Sub为前缀，后面添加设备的MAC地址。
  // 这么做是为确保不同设备使用同一个MQTT服务器测试消息订阅时，所订阅的主题名称不同
  String topicString = "mqtt_SmartCar";
  char subTopic[topicString.length() + 1];  
  strcpy(subTopic, topicString.c_str());
  
  // 通过串口监视器输出是否成功订阅主题以及订阅的主题名称
  if(mqttClient.subscribe(subTopic,1)){
    Serial.println("Subscribed to Topic:");
    Serial.println(subTopic);
  } else {
    Serial.print("Failed to Subscribe to Topic: ");
    Serial.println(subTopic);
  }  
}

// ESP8266连接WiFi
void connectWifi(){
  WiFi.begin(ssid, password);
  
  // 等待WiFi连接,成功连接后输出成功信息
  while (WiFi.status() != WL_CONNECTED) {
    delay(300);
    digitalWrite(LED_BUILTIN, LOW);
    delay(300);
    digitalWrite(LED_BUILTIN, HIGH);
    Serial.print(".");
  }
  digitalWrite(LED_BUILTIN, HIGH);
  Serial.println("");
 Serial.println("WiFi Connected!");  
}