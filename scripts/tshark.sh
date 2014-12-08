tshark -i $1 subtype probereq -l -Tfields -e frame.time -e wlan.sa -e wlan.da -e radiotap.dbm_antsignal -e wlan_mgt.ssid
#tshark -i $1 -l -Tfields -e frame.time -e wlan.sa -e wlan.da -e radiotap.dbm_antsignal -e wlan_mgt.ssid
