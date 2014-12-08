# Activa una tarjeta en modo monitor
sudo bash ./airmon-ng start $1 2> /dev/null
ifconfig mon0 > /dev/null 2>&1 
sleep 1
if [ $? = 0 ] #si el comando anterior se realizó correctamente es porque existe la interfaz "mon0"
then
	INTERFAZ_MONITOR=mon0
fi
echo "Interfaz monitor: $INTERFAZ_MONITOR"