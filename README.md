
# Medical Prescription API #


A Computer Science class Project of:

* Allan S. Lim
* Chander Ahuja
* Richard Keener


## Technology use ##

* JAVA 8
* Spring Boot
* 

## To generate certificate ##

```
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650

```

## To inspect the certificate ##

```
 keytool -list -v -keystore keystore.p12 -storetype pkcs12
````