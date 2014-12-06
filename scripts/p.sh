#!/bin/sh

red='\033[0;31m'
NC='\033[0m' # No Color
XSeconds=0.001

#COMPRUEBA SI HAY INTERFACES EN MODO MONITOR Y, SI LAS HAY, LAS DESACTIVA
desactivar_todos_monX()
{
INTERFACES_MONITOR=`iwconfig --version | grep "Recommend" | awk '{print $1}' | grep mon`
let CUANTAS=`echo $INTERFACES_MONITOR | wc -w`
let CONT=1
while [ $CONT -le $CUANTAS ]
do
	MON=`echo $INTERFACES_MONITOR | awk '{print $'$CONT'}'`
	./airmon-ng stop $MON > /dev/null 2>&1
	let CONT=$CONT+1
done
}

#DETIENE POSIBLES PROCESOS EN MARCHA
matar_procesos()
{
PROCESOS=$(ps $PARAMETRO_PS | egrep -a -n '(xterm|ifconfig|dhcpcd|dhclient|NetworkManager|networkmanager|wpa_supplicant|airbase-ng|wicd|wicd-client)' | grep -v grep)
while [ "$PROCESOS" != "" ]
do
	killall -q xterm ifconfig dhcpcd dhclient NetworkManager networkmanager wpa_supplicant airbase-ng wicd wicd-client >/dev/null 2>&1
	PROCESOS=$(ps $PARAMETRO_PS | egrep -a '(xterm|ifconfig|dhcpcd|dhclient|NetworkManager|networkmanager|wpa_supplicant|airbase-ng|wicd|wicd-client)' | grep -v grep)
done
desactivar_todos_monX
}

#SELECCIÓN DE LA TARJETA WiFi
seleccionar_tarjeta()
{
TARJETAS_WIFI_DISPONIBLES=`iwconfig --version | grep "Recommend" | awk '{print $1}' | sort`
N_TARJETAS_WIFI=`echo $TARJETAS_WIFI_DISPONIBLES | awk '{print NF}'`
if [ "$TARJETAS_WIFI_DISPONIBLES" = "" ]
then
	echo -e ""$rojoC"ERROR: No se detectó ninguna tarjeta WiFi"
	echo -e "$grisC"
	pulsar_una_tecla "Pulsa una tecla para salir..."
else
	echo -e ""$cyan"Tarjetas WiFi disponibles:"$grisC""
	echo
	x=1
	while [ $x -le $N_TARJETAS_WIFI ]
	do
		INTERFAZ=`echo $TARJETAS_WIFI_DISPONIBLES | awk '{print $'$x'}'`
		DRIVER=`ls -l /sys/class/net/$INTERFAZ/device/driver | sed 's/^.*\/\([a-zA-Z0-9_-]*\)$/\1/'`
		MAC=`ifconfig "$INTERFAZ" | grep -oE '([[:xdigit:]]{1,2}:){5}[[:xdigit:]]{1,2}' | awk '{print toupper($0)}' | cut -c-8` #extraemos la MAC XX:XX:XX (sólo los 3 primeros pares)
		if [ "$MAC" = "" ] #si la interfaz esta en modo monitor el procedimiento para extraer la MAC es distinto
		then
	        	MAC=`ifconfig "$INTERFAZ" | grep "HWaddr" | awk -F 'HWaddr ' '{print $2}' | cut -c-17 | sed 's/-/:/g'`
        	fi
        	#FABRICANTE_INTERFAZ=`$FABRICANTE "$MAC"`
		if [ $x -eq 1 ]
		then
			echo -e ""$cyan" Nº\tINTERFAZ\tDRIVER\t\tFABRICANTE"
			echo -e ""$cyan" ══\t════════\t══════\t\t══════════"
		fi
		CARACTERES_DRIVER=`echo $DRIVER | wc -c` 
		if [ $CARACTERES_DRIVER -gt 8 ] #CONTROLA LA TABULACIÓN DEPENDIENDO DE LOS CARACTERES QUE TENGA LA VARIABLE "DRIVER"
		then
			TAB=""
		else
			TAB="\t"
		fi
		echo -e ""$amarillo" $x)\t$INTERFAZ \t\t$DRIVER\t"$TAB
		x=$((x+1))
	done
	if [ $N_TARJETAS_WIFI -gt 1 ] # SI DETECTA MAS DE UNA NOS PREGUNTA CUAL QUEREMOS
	then
		echo -e "\n"$cyan"\nSelecciona una tarjeta WiFi:\033[K\c"
		echo -e ""$amarillo" \c"
		read -n 1 OPCION
		while [[ $OPCION < 1 ]] || [[ $OPCION > $N_TARJETAS_WIFI ]]
		do
			echo -en "\a\033[10C"$rojoC"OPCIÓN NO VÁLIDA"$grisC"    "
			sleep 1
			echo -en ""$cyan"\rSelecciona una tarjeta WiFi: "$amarillo"\033[K\c"
			read -n 1 OPCION
		done
	else
		OPCION=1
	fi
fi
if [ $N_TARJETAS_WIFI -gt 1 ] # SI DETECTA MÁS DE UNA VARÍA EL MENSAJE
then
	INTERFAZ=`echo $TARJETAS_WIFI_DISPONIBLES | awk '{print $'$OPCION'}'`
	MAC=`ifconfig "$INTERFAZ" | grep -oE '([[:xdigit:]]{1,2}:){5}[[:xdigit:]]{1,2}' | awk '{print toupper($0)}' | cut -c-8` #extraemos la MAC XX:XX:XX (sólo los 3 primeros pares)
	FABRICANTE_INTERFAZ=`$FABRICANTE "$MAC"`
	echo -e "\n"
	echo -e ""$cyan"Has seleccionado: "$verdeC"$INTERFAZ $grisC[$FABRICANTE_INTERFAZ]"
	echo
else
	echo
	echo -e ""$cyan"Sólo se ha detectado una tarjeta WiFi: "$verdeC"$INTERFAZ"$grisC"\033[K"
	echo
fi
}

#LOGO
mostrar_logo()
{
echo -e "${red}#####  #####   ####  #####  #####    #####      ##     #####      ##     ##### ${NC}"
echo -e "${red}##  ## ##  ## ##  ## ##  ## ##       ##  ##    ####    ##  ##    ####    ##  ##${NC}"
echo -e "${red}#####  #####  ##  ## #####  ####     #####    ##  ##   ##  ##   ##  ##   ##### ${NC}"
echo -e "${red}##     ## ##  ##  ## ##  ## ##       ## ##   ########  ##  ##  ########  ## ## ${NC}"
echo -e "${red}##     ##  ##  ####  #####  #####    ##  ## ##      ## #####  ##      ## ##  ## ${NC}"
echo
echo -e "${red}Por CAIMMI, Brian & VALLEJOS, Sebastián${NC}"
echo

}

#INICIALIZACIÓN DE LA TARJETA
iniciar_tarjeta()
{
echo -e ""$cyan"Iniciando la tarjeta WiFi..."$grisC""
echo
ifconfig $INTERFAZ down
ifconfig $INTERFAZ up
iwconfig $INTERFAZ rate 1M
}

#ACTIVA EL MODO MONITOR DE LA INTERFAZ
activar_modo_monitor()
{
#software/./reiniciar_interfaz.sh $INTERFAZ
echo $INTERFAZ
./reiniciar_interfaz.sh $INTERFAZ
MAC_INTERFAZ=`ifconfig "$INTERFAZ" | grep -oE '([[:xdigit:]]{1,2}:){5}[[:xdigit:]]{1,2}' | awk '{print toupper($0)}'` #extraemos la MAC de la interfaz del comando 'ifconfig'
echo $MAC_INTERFAZ
if [ "$MAC_INTERFAZ" = "" ] #si la interfaz esta en modo monitor el procedimiento para extraer la MAC es distinto
then
	MAC_INTERFAZ=`ifconfig "$INTERFAZ" | grep "HWaddr" | awk -F 'HWaddr ' '{print $2}' | cut -c-17 | sed 's/-/:/g'`
fi
echo -e ""$cyan"Activando modo monitor en $blanco$INTERFAZ $grisC("$MAC_INTERFAZ")$cyanC..."$grisC""
./airmon-ng start $INTERFAZ 2>/dev/null
ifconfig mon0 > /dev/null 2>&1
if [ $? = 0 ] #si el comando anterior se realizó correctamente es porque existe la interfaz "mon0"
then
	INTERFAZ_MONITOR=mon0
else
	INTERFAZ_MONITOR=$INTERFAZ
fi
}

cambiar_canales()
{
CANALES=(1 2 3 4 5 6)
XSeconds=5
for ((;;))
do
  for CANAL in $CANALES
  do  
    iwconfig $INTERFAZ_MONITOR channel $CANAL
    
    echo -e -n "          \r"
    echo -e -n " CANAL: "$CANAL"\r"
    
    sleep $XSeconds
  done
done
}

test() {
for ((;;))
do
   echo hola
done
}

#MAIN
clear

#Se matan los procesos conflictivos
#matar_procesos

#mostrar el Logo
mostrar_logo

#Se carga la tarjeta en modo monitor
#seleccionar_tarjeta
#iniciar_tarjeta
#activar_modo_monitor

#Se corre en otra terminar el tShark
#xterm -bg black -fg red -geometry 100x30-0+150 -title "TSharK" -e tshark -i $INTERFAZ_MONITOR subtype probereq -l -Tfields -e frame.time -e wlan.sa -e wlan.da -e radiotap.dbm_antsignal -e wlan_mgt.ssid | xterm -title "Parser" -e java -jar proberadar.jar &

#CANALES=`iw list | grep dBm | cut -d' ' -f 4 | egrep -o [[:digit:]]*`
#echo "Canales disponibles: "$CANALES


#xterm -bg black -fg red -geometry 100x30-0+150 -title "Cambiando canales" -e ./cambiar.sh > /dev/null &

#gnome-terminal tshark -i $INTERFAZ_MONITOR subtype probereq -l -Tfields -e frame.time -e wlan.sa -e wlan.da -e radiotap.dbm_antsignal -e wlan_mgt.ssid | gnome-terminal -e java -jar proberadar.jar
#x-terminal-emulator -title "Cambiando canales" -e "bash cambiar_canales.sh"
#gnome-terminal tshark -i $INTERFAZ_MONITOR subtype probereq -l -Tfields -e frame.time -e wlan.sa -e wlan.da -e radiotap.dbm_antsignal -e wlan_mgt.ssid

#bash cambiar_canales.sh | java -jar test2.jar
#x-terminal-emulator -e "bash test.sh" &