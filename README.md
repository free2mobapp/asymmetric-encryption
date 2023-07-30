# Overview
This AsymmetricEncryption, is a poc to show how Asymmetric encryption/decryption works, with 
Public and Private keys created using openssl and converted to PCKS8 format.

# Concepts:
Two encoding standards, PKCS#8 and X.509, are supported by JDK.
java.security.spec.PKCS8EncodedKeySpec class can be used to 
  convert private keys encoded as byte strings into key spec objects.

java.security.spec.X509EncodedKeySpec class can be used to 
  convert public keys encoded as byte strings into key spec objects.

java.security.KeyFactory class can be used to 
  convert key spec objects back to private or public key objects.


# Generate the key
launch MINGw64 in C:\Program Files\Git\mingw64\bin 
which will give a linux like command prompt.

To generate the keys in PCKS8 format.
## First generate private key with pkcs1 encoding
openssl genrsa -out private_key_rsa_2048_pkcs1.pem 2048

## Now convert private key to pkcs8 encoding
openssl pkcs8 -topk8 -in private_key_rsa_2048_pkcs1.pem -inform pem -out private_key_rsa_2048_pkcs8-exported.pem -outform pem -nocrypt

## Private key will look like this
-----BEGIN PRIVATE KEY-----

MIIJQgIBADANBgkqhkiG9w0BAQEFAASCCSwwggkoAgEAAoICAQDVgLrCSDC5mLRL
JY+okYX5MOMGi+bvtRQ9qIQ90d3BO1gAao6ZsbPEFxnOTR9Q3bGsEE5oRlh/FSYS
.
.
kvCjd0ineNZ6OgPVJ/mhPULsZb11+noSUPmFqvClb8SQ0BipbKIcSTIJlQt1ZRZ2
INdXsP5kNlRK181jtU/xtQYfwSjkKA==

-----END PRIVATE KEY-----

## Export public key in pkcs8 format
openssl rsa -pubout -outform pem -in private_key_rsa_2048_pkcs8-exported.pem -out public_key_rsa_2048_pkcs8-exported.pem

## Public key will look like this
-----BEGIN PUBLIC KEY-----

MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA1YC6wkgwuZi0SyWPqJGF
+TDjBovm77UUPaiEPdHdwTtYAGqOmbGzxBcZzk0fUN2xrBBOaEZYfxUmEkOFzPbF
.
.
oNta8CSsVrqgFW/tI6+MQwrQFEOcBPCbh6Pr7NbiuR2LrfoJhUJlD5ofz5eM0419
JSS0RvKh0dF3ddlOKV/TQUsCAwEAAQ==

-----END PUBLIC KEY-----

# Running and testing the application
Start AsymmetricEncryptionApplication

# From postman:
  POST - http://localhost:8080/rest/register
Request:
 {
  "header": {
     "requestId": "1234",
     "username": "bhasker"
    },
    "secureContext": "bhasker"
 }

Response:
 {
   "requestHeader": null,
   "secureContext": "bhasker"
 }

# Reference:
  if generating the keys using Java.
https://mkyong.com/java/java-asymmetric-cryptography-example/

This poc is based on the following reference
https://www.javacodegeeks.com/2020/04/encrypt-with-openssl-decrypt-with-java-using-openssl-rsa-public-private-keys.html

To learn how to use Bouncycastle and how it reduces
https://www.baeldung.com/java-read-pem-file-keys