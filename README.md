# encryptedQRCode
android qr code scanner implemented with elliptic curve encryption

the retful api is integrated with the app

change addUrl and fetchUrl as per the host
currently locally hosted on 

uses GET method 

addUrl="http://192.168.43.62:8080/qr_api/user";
fetchUrl="http://192.168.43.62:8080/qr_api/getuser"

change the values in APICommunication class
the api is based on 

https://github.com/katastreet/RESTFULapi
