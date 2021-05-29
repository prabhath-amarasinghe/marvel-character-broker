# marvel-character-broker

Marvel character broker reads all marvel characters using the developer api provided by the Marvel. This is available 
from https://developer.marvel.com/. Once all the character info is retrieved, we emit a random marvel character info to 
a kafka topic named 'marvel-character' once every 2 seconds. 

This 2 seconds delay will get longer as more characters are emitted. This is a result of emitting a single character once.
The character is selected randomly disregarding if it has been selected previously.

For this, I am also using Confluence running in a docker container. So as long as default values are not changed, this 
should be good to run as it is by following steps.

**Please also note that this project has a dependency on marvel-character-avro-schema. This will register a schema on the 
topic marvel-character.

Steps

1. Create your own marvel account. This will give you private and public key which will need to be updated on the 
application.properties file.
   
2. Make sure confluent docker container is running.

3. Make sure you have maven clean installed the marvel-character-avro-schema project
