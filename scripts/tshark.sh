tshark -i $1 -l -Tfields -e frame.time -e wlan.fc.type_subtype -e wlan.sa -e wlan.da -e radiotap.dbm_antsignal -e wlan_mgt.ssid

