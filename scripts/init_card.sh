# Inicia la tarjeta wifi
ifconfig $1 down
ifconfig $1 up
iwconfig $1 rate 1M
