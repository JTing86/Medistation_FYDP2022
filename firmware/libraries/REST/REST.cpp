#include "REST.h"

REST::REST() {
    
}

// sends HTTP request
// port is 443 for HTTPS, 80 for HTTP
String REST::sendRequest(String type, String base_url, String path, String payload, uint8_t port, String header) {
  secureClient.setInsecure();//skip verification
  if (!secureClient.connect(base_url.c_str(), 443)){
    Serial.println("Connection failed!");
    return "Error";
  }
  else {
    Serial.println("Connected to server!");
    // Make a HTTP request:
    
    secureClient.println(type + " https://" + base_url + path + " HTTP/1.0");
    secureClient.println("Host: " + base_url);
    secureClient.println("Connection: close");

    if (header != "") {
      secureClient.println(header);
    }
    
    if (type != "GET") {
      secureClient.print("Content-Length: ");
      secureClient.println(payload.length());
    }
    
    secureClient.println();

    if (type != "GET") {
      secureClient.println(payload);
    }

    while (secureClient.connected()) {
      String line = secureClient.readStringUntil('\n');
      if (line == "\r") {
        Serial.println("headers received");
        break;
      }
    }
    // if there are incoming bytes available
    // from the server, read them and print them:
    String response;
    while (secureClient.available()) {
      response += (char)secureClient.read();
    }
    
    secureClient.stop();

    return response;
  }
}