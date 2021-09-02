#bin/bash

export functionName="eth_getBlockByNumber"

docker pull shazow/ethspam

docker run shazow/ethspam | grep "$functionName" > $functionName.txt

docker stop shazow/ethspam --time=5