echo "-------------- Tshark ------------------"
tshark -i $1 subtype probereq -l -Tfields -e frame.time -e wlan.sa -e wlan.da -e radiotap.dbm_antsignal -e wlan_mgt.ssid
echo "----------------------------------------"