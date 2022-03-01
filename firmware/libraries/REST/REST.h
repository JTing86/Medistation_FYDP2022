#include <WiFiClientSecure.h>

class REST{
  public:
    REST();

    String sendRequest(String type, String base_url, String path, String payload = "", uint8_t port = 443, String header = "");
  
  private:
    WiFiClientSecure secureClient;
};