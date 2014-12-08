# Activa una tarjeta en modo monitor
sudo bash ./airmon-ng start $1 | grep "(monitor mode enabled on" | egrep -o "mon[[:digit:]]"
