mvn clean compile install spring-boot:run

http://localhost:9001/addMusic
{
  "songName":"testSong",
  "artist":{
    "name":"testNameArtist",
    "country":"UP"
  }
}