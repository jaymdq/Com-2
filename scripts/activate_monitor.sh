# Activa una tarjeta en modo monitor
./airmon-ng start $1 2> /dev/null
ifconfig mon0 > /dev/null 2>&1 
until [  $? = 0 ]; do
	sleep 1
done
if [ $? = 0 ] #si el comando anterior se realizó correctamente es porque existe la interfaz "mon0"
then
	INTERFAZ_MONITOR=mon0
else
	INTERFAZ_MONITOR=$1
fi
echo "Interfaz monitor: $INTERFAZ_MONITOR"