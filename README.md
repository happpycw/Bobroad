# Bobroad


<p align="center">
  <img src="https://github.com/user-attachments/assets/88eb18aa-b7e7-4f8c-9472-ca87d43560ef" width="300"/>
  <img src="https://github.com/user-attachments/assets/c9d786ca-d939-415e-a608-98d299dc7f53" width="300"/>
</p>

### 👥 팀원 소개

| 박현도 | 안세희 | 이인선 | 김찬우 |
|:------:|:------:|:------:|:------:|
| ![박현도](https://github.com/hyundo0328.png) | ![안세희](https://github.com/ash-be.png) | ![이인선](https://github.com/Inseoni.png) | ![김찬우](https://github.com/happpycw.png) |
| [@hyundo0328](https://github.com/hyundo0328) | [@ash-be](https://github.com/ash-be) | [@Inseoni](https://github.com/Inseoni) | [@happpycw](https://github.com/happpycw) |

## 💁‍♂️ 코드 컨벤션
스타일 가이드
https://www.notion.so/20f15a01205481778ececdeee9546ed8

### 🏷️ Git 커밋 태그 설명

| 태그 | 설명 |
|------|------|
| ✨ `feat` | 새로운 기능 추가 |
| 🐛 `fix` | 버그 수정 |
| 📝 `docs` | 문서 수정 (README 등) |
| 🎨 `style` | 코드 포맷팅, 세미콜론 누락, 코드 변경 없음 |
| ♻️ `refactor` | 코드 리팩토링 (기능 변화 없이 구조 개선) |
| ✅ `test` | 테스트 코드 추가, 기존 테스트 리팩토링 |
| 🔀 `merge` | 브랜치 병합 (예: `main` ← `feature/login`) |
| 🎨 `design` | 뷰 디자인 변경 |

# ✈️ 프로젝트 소개
직장인의 평생고민 점심 뭐먹지? 회식 어디서 하지?
업무에 바쁜데 반복되는 점심 메뉴 선택, 회식 식당 선택, 비즈니스 미팅 식당 선택...
바쁜 회사 생활 속 식당을 고르기 어려운 직장인에게 구체적 상황에 기반하여, 식당을 추천해주고,
단체 회식 등 여러 사람의 의견이 필요한 상황에서는 모임 및 투표 기능을 통해 모두가 만족할 수 있는
식당을 정할수 있도록 도와줍니다 !

## 📌 주요 기능

### 👥 모임
- 모임 생성 및 참여
- 모임내 식당 투표 기능
- 더치페이 여부
- 모임 내 초대받은 유저의 권한관리

### 🍽️ 식당 검색
- 식당 검색
- 큐레이팅 (설문조사 기반) 식당 추천
- AI (설문조사, 검색어 기반) 식당 추천
- 회원의 북마크 추가 기능
- 북마크 랭킹

 ### 🔐 사용자 인증
- 회원가입 / 로그인 / 로그아웃
- 마이페이지 / 회원정보 수정 / 설문조사 수정
- JWT , Oauth2 로그인
- SMTP 서버 분리 

### ⚙️ 관리자
- 사용자 정보 (북마크, 설문조사) 조회 
- 모임 조회 및 관리
- 큐레이션 추가
- 큐레이션 조회 및 식당 관리

### 🛠️ 기술 스택

| 분류 | 기술 |
|------|------|
| **Language** | ![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white) ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white) |
| **Backend Framework** | ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) |
| **ORM** | ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-59666C?style=for-the-badge&logo=spring&logoColor=white) |
| **Template Engine** | ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white) |
| **Database** | ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) |
| **AI** | ![Gemini](https://img.shields.io/badge/Gemini-4285F4?style=for-the-badge&logo=google&logoColor=white) |
| **협업 도구** | ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) ![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white) ![Trello](https://img.shields.io/badge/Trello-0052CC?style=for-the-badge&logo=trello&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) ![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white) |

## 📌 시스템 구성도

![스크린샷 2025-06-18 164109](https://github.com/user-attachments/assets/d21e6c98-43ff-4d1d-aaa4-e4afc6f37f7c)

## 🗂️ APIs
<details>
<summary>👉 API 바로보기</summary>

| 기능 | 메서드 | URL |
|------|--------|-----|
| Main 화면 | GET | / |
| 검색어 입력 화면 | GET | /search |
| 큐레이션 추천 화면 이동 | GET | /curation |
| LLM 추천 화면 이동(질문 입력 화면) | GET | /recommend |
| 나의 모임 리스트 조회 화면 이동 | GET | /meetings |
| 북마크 리스트 조회 화면 이동 | GET | /bookmarks |
| 북마크 랭킹 리스트 조회 화면 이동 | GET | /ranking |
| 로그인 페이지 이동 | GET | /user/signin |
| 로그아웃 | POST | /user/logout |
| 마이페이지 이동 | GET | /mypage |
| 큐레이션 관리 화면 이동 | GET | /admin/curation |
| 설문조사 화면 출력 | GET | /surveys |
| 설문조사 등록 | POST | /surveys |
| 검색 결과 화면 | POST | /search |
| 검색 결과 화면 | GET | /search/result |
| 식당 상세정보 | GET | /restaurant/{restaurantId} |
| 식당 위치 보기 (Pin) → 식당 상세정보와 함께 조회 | GET | /restaurant/{restaurantId}/location |
| 식당 북마킹 | POST | api/bookmarks/toggle/{restaurantId} |
| 식당 북마킹 해제 | DELETE | api/bookmarks/toggle/{restaurantId} |
| 유저 회원가입 페이지 이동 | GET | /user/signup |
| 중복 유저 검사 | GET | /api/user/exists/{id} |
| 회원가입 | POST | /user/signup |
| 유효성 검사 | POST | /user/signin |
| 로그아웃 | POST | /mypage/logout |
| 회원탈퇴 | PATCH | /api/users/{id} |
| 회원정보 수정 화면 이동 | GET | /mypage/edit-info/{id} |
| 회원정보 수정 | POST | /mypage/edit-info/{id} |
| 선호목록 수정 화면 이동 | GET | /mypage/edit-prefer/{id} |
| 선호목록 수정 | POST | /mypage/edit-prefer/{id} |
| 관리자 회원가입 페이지 이동 | GET | /admin/signup |
| 중복 유저 검사 | GET | /api/admin/exists/{id} |
| 회원가입 | POST | /admin/signup |
| 사용자 정보 출력 | GET | /admin/users |
| 사용자 선호 목록 더보기 | GET | api/admin/users/prefer/{userid} |
| 북마크 더보기 | GET | api/admin/users/bookmark/{userid} |
| 사용자 검색기능 | GET | api/admin/search/users |
| 큐레이션 리스트 | GET | /admin/curation/list |
| 큐레이션 추가 화면 이동 | GET | /admin/curation/register |
| 큐레이션 추가 | POST | /admin/curation/register |
| 큐레이션 비활성화 | PATCH | /admin/curation/{curationId} |
| 큐레이션 검색기능 | GET | /api/admin/search/curation |
| 모임 관리 화면 이동 | GET | /admin/meetings |
| 모임 멤버 더보기 | GET | /api/admin/meetingmember/{meetingId} |
| 모임 검색기능 | GET | /api/admin/search/meeting |
| 모임 삭제기능 | DELETE | /api/admin/{meetingId} |
| 모임 상세 화면 이동 (구현 X) | GET | /admin/meetings/{meetingId} |
| 질문 입력 | POST | /recommend |
| LLM 추천 리스트 조회 | GET | /recommendList |
| 모임 생성 화면 이동 | GET | /meetings/regist |
| 모임 생성 | POST | /meetings/regist |
| 모임 초대 알림 화면 팝업 | GET | /meetings/modal/alarm-invite.html |
| 모임 초대 알림 수락 or 거절 | POST | /meetings/modal/alarm-invite.html |
| 투표 알림 화면 팝업 | GET | /meetings/modal/alarm-vote.html |
| 모임 상세 화면 이동 | GET | /meetings/detail |
| 모임 탈퇴 | POST | /meetings/detail |
| 모임 멤버리스트 화면 이동 | GET | /meetings/modal/meeting-member-list.html |
| 모임 초대 화면 이동 | GET | /meetings/modal/meeting-invite.html |
| 모임 초대하기 | POST | /meetings/modal/meeting-invite.html |
| 투표 등록화면 이동 | GET | /meetings/vote-regist |
| 투표 등록하기 | POST | /meetings/vote-regist |
| 모임별 멤버 화면 이동 | GET | /meetings/modal/member-list.html |
| 투표 만들기 화면 | GET | /meetings/vote-regist |
| 투표 만들기 | POST | /meetings/vote-regist |
| 투표하기(화면 조회) | GET | /meetings/vote |
| 투표하기(투표 행위) | POST | /meetings/vote |
| 투표 결과 화면 | GET | /meetings/vote-result |
| 멤버 북마크 리스트 조회 | GET | /users/{userId}/bookmarks |

</details>

## 📌 페르소나 서비스 조사

![스크린샷 2025-06-18 164002](https://github.com/user-attachments/assets/aeb1f589-bf06-4ae6-b284-9baea508e128)

## 📌 UI 플로우 차트

![스크린샷 2025-06-18 164155](https://github.com/user-attachments/assets/7e8fe395-dad5-40c2-9b98-f8d23a8b6c7c)

## 📌 화면 설계서

![스크린샷 2025-06-18 173207](https://github.com/user-attachments/assets/cd786d5c-2c02-4e77-ae24-64d14b47ee3d)

## 📌 ERD

![스크린샷 2025-06-18 164309](https://github.com/user-attachments/assets/7014f845-af5d-41bd-a2cb-ad6101ed2e6f)

-----------------------------------------------------------------------------------------------------------------------

## 📌 메인화면

![스크린샷 2025-06-18 174543](https://github.com/user-attachments/assets/ffbaa7c5-fca7-4d07-a914-718dc051e547)

## 📌 밥로드 검색

![스크린샷 2025-06-18 165708](https://github.com/user-attachments/assets/9dcdd428-fcbf-482c-87f2-a785b0c21f54)

## 📌 AI (식당)검색 

![스크린샷 2025-06-18 172127](https://github.com/user-attachments/assets/ddd08e3a-29ea-4272-9eaa-0748962a62f0)

## 📌 식당 검색 결과

![스크린샷 2025-06-18 173746](https://github.com/user-attachments/assets/6a1f32ec-0b99-4f69-ba19-ebe3eb6269db)

## 📌 식당 상세 화면

![스크린샷 2025-06-18 173845](https://github.com/user-attachments/assets/4c8622cb-fa37-4ef8-8b25-e3d06b1bc03c)

## 📌 큐레이팅

![스크린샷 2025-06-18 165747](https://github.com/user-attachments/assets/45ccd552-77e1-4e65-8bb9-cb5ffc154bd5)

## 📌 로그인 

![스크린샷 2025-06-18 165855](https://github.com/user-attachments/assets/e3be6978-03dc-4368-9737-160ae49ae330)

## 📌설문조사

![스크린샷 2025-06-19 012508](https://github.com/user-attachments/assets/d3a3c468-ce62-4cd4-bb2e-4a97329ad3a9)

## 📌 마이페이지

![스크린샷 2025-06-18 171638](https://github.com/user-attachments/assets/b4831822-bd23-4ba7-9112-5c0435899e25)

## 📌 모임

![스크린샷 2025-06-18 170104](https://github.com/user-attachments/assets/fbce6880-90a6-415d-9307-8a2c4c296e31)

## 📌 모임 투표 생성

![스크린샷 2025-06-18 171333](https://github.com/user-attachments/assets/ac41a933-d188-40fd-b94f-d775ae0708ac)

## 📌 모임 실시간 알림

![스크린샷 2025-06-18 171540](https://github.com/user-attachments/assets/98deef32-5c78-4156-b3d2-4c5bb789ad93)

## 📌 나의 북마크

![스크린샷 2025-06-18 170209](https://github.com/user-attachments/assets/46c9bcb7-ded2-441f-a1ee-e931afda6cbf)

## 📌 북마크 랭킹

![스크린샷 2025-06-18 170242](https://github.com/user-attachments/assets/a6a0e54a-b854-4aa9-85f9-c30fcf7aab32)

## 📌 관리자 (큐레이션)

![스크린샷 2025-06-18 172003](https://github.com/user-attachments/assets/6fe045c5-55ee-4ea2-9250-fd5d8f1b345c)

## 📌 관리자 (모임)

![스크린샷 2025-06-26 185358](https://github.com/user-attachments/assets/a53486d9-ee95-4492-9aca-d063eb770e97)


## 📌 관리자 (사용자)

![스크린샷 2025-06-26 185416](https://github.com/user-attachments/assets/0eb4a68d-4e45-48dd-b02c-aa47e2ea7cd4)





