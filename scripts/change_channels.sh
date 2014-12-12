CANALES=`iwlist $1 frequency | egrep -o "Channel [[:digit:]]* :" | cut -d ' ' -f2`

while (true)
do
  for CANAL in $CANALES
  do  
    iwconfig $1 channel $CANAL 
    sleep 0.001
  done
done