# smartthings-datalogger
Log SmartThings device events using JSON over HTTP POST (setup for FluentD, but can easily be modified to suit other backends)

Find the following and modify to point to your endpoint (I haven't set this up in config, because I assume if you're using this, you're probably going to touch the code anyway...)

xxx.xxx.xxx.xxx:pppp - set this to the "normal" IP address and port of your FluentD endpoint (example: 192.168.0.100:8080)

XXXXXXXX:PPP - set this to the hex version of the IP address from above. (example for 192.168.0.100:8080 = C0A80064:1F90)
Note: I use this site's tool to quickly convert the IP: http://www.miniwebtool.com/ip-address-to-hex-converter/

Then, if you like, you can modify the HTTP POST body and JSON to suit your needs.
