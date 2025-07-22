# ⚾ MY BASEBALL ✪ ALL STAR SERVER

<img height="300" alt="image" src="https://github.com/user-attachments/assets/f900557c-ece1-4002-8ac1-be7a33cd2e12" />
<img height="300" alt="image" src="https://github.com/user-attachments/assets/b3c88be0-c040-4878-8823-af9475879f84" />


---
## 📎 프로젝트 링크

- **서비스 주소**: [https://www.myallstar.my](https://www.myallstar.my)

---

## 🛠️ 기술 스택

| 계층           | 기술                                      |
|----------------|-------------------------------------------|
| Language       | Java 17                                   |
| Framework      | Spring Boot 3, Spring MVC, Spring Data JPA|
| Database       | MySQL                                     |
| ORM            | Hibernate                                 |
| Build Tool     | Gradle                                    |
| Infrastructure | AWS EC2, Nginx, MySQL                     |
| Monitoring     | Prometheus, Grafana, Loki                 |
| Logging        | Logback                                   |
| Version Control| Git, GitHub, GitHub Actions               |
| Test           | Junit, Rest5, RestAssured, H2             |
| Others         | Selenium (데이터 크롤링), RESTful API 설계 |

---

## 📌 주요 기능 정리

| 기능 구분       | 설명                                                  | 메서드 | 엔드포인트                       | 
|----------------|-------------------------------------------------------|--------|----------------------------------|
| 랜덤 팀 생성    | 무작위로 포지션별 선수를 배치하여 12명 팀 구성                 | GET    | `/teams?mode=random`            | 
| 내 팀 저장      | 선택한 선수들로 팀을 구성하고 고유 URL 생성           | POST   | `/teams`                        |
| 팀 정보 조회    | 팀 UUID를 통해 공유된 팀 정보 조회                    | GET    | `/teams/{team-uuid}`           |
| 솔로 경기 진행  | 구성한 두 팀에 대한 경기 시뮬레이션 진행  | POST   | `/plays/solo`                  | 
| 친구와 경기 진행| 친구가 내 팀과의 대결을 요청하여 경기 시뮬레이션 진행| POST   | `/plays/friend`                | 
| 경기 결과 확인  | 팀 ID를 기준으로 해당 팀의 경기 결과(전적) 조회       | POST   | `/play-results/{team-id}`      | 
| 선수 정보 요청  | 전체 선수 목록 제공      | GET    | `/players`                     | 

---

## 👥 구성

| 기획 및 개발 | 기획 및 개발 | 
| :-: | :-: |
| <img src="https://avatars.githubusercontent.com/u/86762272?v=4" width="150"> | <img src="https://avatars.githubusercontent.com/u/87298145?v=4" width="150"> |
 | [신지송](https://github.com/shin-jisong) | [주보경](https://github.com/jupyter471) |


> 💡 본 프로젝트는 상업적 목적이 전혀 없는 **비영리 개인 프로젝트**입니다.

