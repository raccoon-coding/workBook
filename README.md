# 🧠 GPT 문제 생성 서비스

> 사용자가 입력한 과목과 목차 정보를 기반으로 GPT를 활용해 **맞춤형 문제 및 해설을 자동 생성**하는 교육용 서비스입니다.

---

## 📌 프로젝트 개요

본 서비스는 중·고등학생을 주요 대상으로 하여, 특정 과목의 학습 단원(목차)을 입력하면 OpenAI GPT API를 통해 해당 주제에 대한 문제와 해설을 생성해주는 기능을 제공합니다. 교사나 학생이 직접 문제를 만들 필요 없이 **자동으로 문제를 생성**하여 학습 효율을 높이는 데 목적이 있습니다.

---

## 🚀 주요 기능

- ✅ 과목 및 단원(목차) 입력 기능
- ✅ GPT API를 통한 문제 및 해설 자동 생성
- ✅ 생성된 문제/해설을 사용자별로 저장 및 조회
- ✅ 파일(PDF 등) 저장 및 관리
- ✅ 사용자 디렉토리 자동 생성 및 정리

---

## 🛠️ 사용 기술 스택

| 분류       | 기술                                |
|------------|-------------------------------------|
| 언어       | Java 17                             |
| 프레임워크 | Spring Boot 3.x                     |
| 빌드 도구  | Gradle                              |
| 데이터베이스 | MongoDB                            |
| 테스트     | JUnit5, Mockito                     |
| 배포       | Docker, Docker Compose              |
| API        | OpenAI GPT API                      |

---

## 🔒 보안 및 환경설정 주의사항

본 프로젝트는 보안을 위해 아래와 같은 파일들을 Git에 업로드하지 않습니다:

- `application.yml`
- `.env` (Docker 환경 변수 파일)

### 🔐 환경변수 예시

`application.yml` 파일 예시:
```yaml
spring:
  data:
    mongodb:
      uri: ${MONGO_URI}

openai:
  api-key: ${OPENAI_API_KEY}
```
.env 파일 예시:
```text
MONGO_URI=mongodb://username:password@localhost:27017/dbname
OPENAI_API_KEY=your-openai-key-here
```
실제 서비스 실행 시에는 .env 또는 환경변수를 직접 주입하거나, CI/CD 파이프라인에서 별도로 관리해주세요.

🐳 Docker 실행 예시
```
docker-compose up --build
```

