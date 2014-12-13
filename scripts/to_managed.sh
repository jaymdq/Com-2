# Bajo la tarjeta
ifconfig $1 down
# La seteo en modo managed
iw dev wlan0 set type managed
# Levanto la tarjeta
ifconfig $1 up