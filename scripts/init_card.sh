# Inicia la tarjeta wifi

echo "Iniciando la tarjeta WiFi $1"
ifconfig $1 down
ifconfig $1 up
iwconfig $1 rate 1M