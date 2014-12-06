# Inicia la tarjeta wifi

echo "-------------- Init Card ---------------"
echo "Iniciando la tarjeta WiFi $1"
ifconfig $1 down
echo "Abajo"
ifconfig $1 up
echo "Arriba"
iwconfig $1 rate 1M
echo "----------------------------------------"
