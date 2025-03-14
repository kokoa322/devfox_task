# 게시판 웹 어플리케이션

## 프로젝트 소개

이 프로젝트는 **Java 11**을 기반으로 개발된 게시판 웹 어플리케이션입니다. 사용자는 게시글을 작성하고, 댓글을 달 수 있으며, 해당 게시글에 대한 정보와 댓글을 관리할 수 있습니다. 데이터베이스로는 **H2 (MySQL)**를 사용하고 있습니다.

![스크린샷](https://github.com/user-attachments/assets/51500404-b96f-4762-991a-cf512b2ebc05)

---

## 테이블 구조

### USERS

사용자 정보를 관리하는 테이블입니다. 각 사용자는 고유한 `username`과 `email`을 가지고 있으며, 비밀번호와 생성일자가 기록됩니다.

| 컬럼 이름    | 데이터 타입    | 설명               |
|--------------|----------------|--------------------|
| id           | INT (PK)       | 사용자 고유 ID     |
| username     | VARCHAR (Unique)| 사용자 ID (고유값) |
| password     | VARCHAR        | 비밀번호           |
| email        | VARCHAR (Unique)| 이메일 (고유값)    |
| created_at   | TIMESTAMP      | 생성일자           |

---

### BOARDS

게시글을 관리하는 테이블입니다. 각 게시글은 특정 사용자(`user_id`)와 연결되어 있으며, 제목과 내용이 저장됩니다. 게시글에 대한 생성일자와 수정일자도 기록됩니다.

| 컬럼 이름    | 데이터 타입     | 설명                   |
|--------------|-----------------|------------------------|
| id           | INT (PK)        | 게시글 고유 ID         |
| user_id      | INT (FK)        | 작성자 (USERS 테이블의 ID)|
| title        | VARCHAR         | 게시글 제목            |
| content      | TEXT            | 게시글 내용            |
| created_at   | TIMESTAMP       | 생성일자               |
| updated_at   | TIMESTAMP       | 수정일자               |

---

### COMMENTS

게시글에 대한 댓글을 관리하는 테이블입니다. 각 댓글은 특정 게시글(`board_id`)과 특정 사용자(`user_id`)와 연결되어 있습니다. 댓글 내용과 생성일자가 저장됩니다.

| 컬럼 이름    | 데이터 타입     | 설명                   |
|--------------|-----------------|------------------------|
| id           | INT (PK)        | 댓글 고유 ID           |
| board_id     | INT (FK)        | 해당 게시글 (BOARDS 테이블의 ID)|
| user_id      | INT (FK)        | 작성자 (USERS 테이블의 ID)|
| content      | TEXT            | 댓글 내용              |
| created_at   | TIMESTAMP       | 생성일자               |

---

## 환경

- **Java 11**
- **Spring Framework
- **MyBatis** (SQL 매핑)
- **H2 Database** (MySQL 호환)
- **JSP** (템플릿)

---

## 설치 방법

1. 프로젝트 클론

```bash
git clone https://github.com/yourusername/board-web-application.git
