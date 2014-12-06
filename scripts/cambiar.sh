#!/bin/sh

CANALES=`iw list | grep dBm | cut -d' ' -f 4 | egrep -o [[:digit:]]*`

echo lalalalal

sleep 4

while (true)
do
  for CANAL in $CANALES
  do  
    iwconfig mon0 channel $CANAL
    
    echo -e -n "          \r"
    echo -e -n " CANAL: "$CANAL"\r"
    
    sleep 2
  done
done


