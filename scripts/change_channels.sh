while (true)
do
  for CANAL in $2
  do  
    iwconfig $1 channel $CANAL 
    sleep 0.001
  done
done          