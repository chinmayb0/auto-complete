# Auto-Complete App
### Build and Run 
```sh
Build : mvn clean install 
Run : mvn spring-boot:run
```
#### Get Suggestions
```sh
endpoints:
/suggestions/{inputWord}
/suggestions/{inputWord}?limit={numericLimit}
```

#### Upload Data set
```sh 
endpoint : 
/uploadFile 

cURL : 
curl -F "file=@{filePath}"  http://localhost:8080/uploadFile
```
#### Insert specific word
`/insertWord/{word}`

### Requirements
- java
- mvn
