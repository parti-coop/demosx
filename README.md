# 민주주의 서울 오픈 소스

## 프로젝트 빌드

자바 및 maven 환경이 설치되어 있어야 합니다.

```
mvn package
```

빌드 시 target폴더에 war 파일이 생성됩니다.

## docker 실행 방법
1. 프로젝트 빌드를 한다. 
2. 생성된 war 파일을 ROOT.war 파일로 이름을 변경한다.
3. docker/webapps 경로에 복사한다. 
4. docker 경로로 이동 후 `docker-compose up`
