# Bajo la tarjeta
ifconfig $1 down
# La seteo en modo monitor
iw dev wlan0 set type monitor
# Levanto la tarjeta
ifconfig $1 up

#Crea una nueva interfaz en modo monitor
#sudo bash ./scripts/airmon-ng start $1 | grep "(monitor mode enabled on" | egrep -o "mon[[:digit:]]"
