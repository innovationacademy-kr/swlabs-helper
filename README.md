# 42 helper

- 42helper는 도움을 원하는 카뎃과 도움을 줄 수 있는 카뎃을 이어주는 플랫폼 입니다.

# Running locally server

### Java 설치

JDK 1.8 버전을 사용합니다.
 - [자바 jdk 8 설치 방법 - okdevtv](https://okdevtv.com/mib/java)
 - java 를 미리 실행 환경에 설치해주세

### DB 설치
- 로컬에서 실행하기 위한 별도의 DB설정이 필요 없습니다. (H2 Database 사용)
- 서버 실행 후 `http://localhost:8080/h2-console` 로 데이터베이스를 관리 할 수 있습니다.

### API 키 발급
```bash
export SECRET42={발급받은 UID}
export SECRET42={발급받은 SECRET}
```
- 42 api 발급 방법은 [api 발급 가이드](https://www.notion.so/epicarts2/42-API-f817378828524392be3fc4432c780bc3) 를 참고하세요
- 42api를 발급받고 `[application.properties](http://application.properties)` 에서 사용할 `UID42`와 `SECRET42`
  환경변수 등록이 필요합니다. 

### run spring-boot server
```bash
./mvnw spring-boot:run
```

- 개발 도구에서 Maven프로젝트로 불러오기를 하거나, 위의 Maven wrapper명령어를 통해 실행할 수 있습니다.
- 실행 후 `http://localhost:8080`으로 접속할 수 있습니다.

# Server deployment

- 아래 사항을 따라 실제 서버를 기동할 수 있습니다.
- 사전에 준비되어있지
  않다면 [인스턴스 셋업 가이드](https://github.com/innovationacademy-kr/swlabs-helper/wiki/%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4-%EC%85%8B%EC%97%85-%EB%B0%A9%EB%B2%95)를
  참고할 수 있습니다.

## Database setup

- 10.4.20 MariaDB Server 혹은 호환되는 MariaDB버전을 사용할 수 있습니다.

### Setup scheme

- DB가 있다면 미리 준비된 스키마로 쉽게 셋업할 수 있습니다.

```bash
mysql -u [YOUR_USERNAME] -p [YOUR_DATABASE] < src/main/resources/db/h2/schema.sql
```

## Environment Setup

- export명령 등을 통해 아래의 환경변수를 미리 지정해야 합니다.

```bash
UID42
SECRET42
HELPER42_DB_USERNAME_RELEASE
HELPER42_DB_PASSWORD_RELEASE
HELPER42_DB_NAME_RELEASE
```

- `UID42` : 발급 받은 42 API ID
- `SECRET42` : 발급 받은 42 API Secret
- `HELPER42_DB_USERNAME_RELEASE` : DB 계정 이름
- `HELPER42_DB_PASSWORD_RELEASE` : DB 계정 패스워드
- `HELPER42_DB_NAME_RELEASE` : DB 이름

## Run server

### Java

- JDK 1.8 버전을 사용합니다.

### Start server

- 내장된 maven wrapper로 빌드한 뒤 실행합니다.

```bash
./mvnw package
cd ./target
java -jar helper-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/application.properties --spring.profiles.active=release
```

### deploy script

- 지속적인 실행이 필요한 경우 deploy 스크립트를 제공하고 있습니다. 환경에 맞게끔 수정 후 사용하세요.
- 위치 : `/script/deploy.sh`

## Contribution guidess

- [issues](https://github.com/innovationacademy-kr/swlabs-helper/issues)를 통해서 버그 리포트나 기능 제안 또는 요청을 할
  수 있습니다.
- 직접 코드 Pull request를 위해 [checkstyle](https://checkstyle.sourceforge.io/)을 통해 코드 컨벤션을 쉽게 맞출 수 있습니다.
  우리 프로젝트의 checkstyle 코드컨벤션 파일은 아래 경로에 위치합니다.
    - `/checkstyle/checkstyle.xml`

## License

42 helper는 [MIT 라이센스](https://github.com/innovationacademy-kr/swlabs-helper/blob/main/LICENSE)를
따릅니다.