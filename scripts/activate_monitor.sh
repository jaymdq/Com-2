# Activa una tarjeta en modo monitor
echo "----------- Activate monitor -----------"
MAC_INTERFAZ=`ifconfig "$1" | grep -oE '([[:xdigit:]]{1,2}:){5}[[:xdigit:]]{1,2}' | awk '{print toupper($0)}'` #extraemos la MAC de la interfaz del comando 'ifconfig'
if [ "$MAC_INTERFAZ" = "" ] #si la interfaz esta en modo monitor el procedimiento para extraer la MAC es distinto
then
	MAC_INTERFAZ=`ifconfig "$1" | grep "HWaddr" | awk -F 'HWaddr ' '{print $2}' | cut -c-17 | sed 's/-/:/g'`
fi
./airmon-ng start $1 2>/dev/null
ifconfig mon0 > /dev/null 2>&1
if [ $? = 0 ] #si el comando anterior se realizó correctamente es porque existe la interfaz "mon0"
then
	INTERFAZ_MONITOR=mon0
else
	INTERFAZ_MONITOR=$1
fi
echo "Interfaz monitor: $INTERFAZ_MONITOR"
echo "----------------------------------------"