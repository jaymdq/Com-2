# Mata todos los procesos que puedan interferir
PROCESOS=$(ps aux | egrep -a -n '(xterm|ifconfig|dhcpcd|dhclient|NetworkManager|networkmanager|wpa_supplicant|airbase-ng|wicd|wicd-client)' | grep -v grep)
while [ "$PROCESOS" != "" ]
do
	killall -q xterm ifconfig dhcpcd dhclient NetworkManager networkmanager wpa_supplicant airbase-ng wicd wicd-client >/dev/null 2>&1
	PROCESOS=$(ps $PARAMETRO_PS | egrep -a '(xterm|ifconfig|dhcpcd|dhclient|NetworkManager|networkmanager|wpa_supplicant|airbase-ng|wicd|wicd-client)' | grep -v grep)
done
