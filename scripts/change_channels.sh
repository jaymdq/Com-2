CANALES=`iw list | grep dBm | cut -d' ' -f 4 | egrep -o [[:digit:]]*`

echo $CANALES
while (true)
do
  for CANAL in $CANALES
  do  
    iwconfig mon0 channel $CANAL
    
    echo $CANAL
    
    sleep 0.001
  done
done


