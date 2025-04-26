# 게시판 프로젝트 (Spring Boot + MyBatis + PostgreSQL)

## 프로젝트 개요

간단한 게시판 기능을 구현한 웹 애플리케이션입니다. 사용자 인증, 게시글/댓글 CRUD, 다중 조건 검색, 추천(vote) 기능 등을 포함합니다.

## 기술 스택

- **Backend**: Spring Boot 3.4.4
- **Database**: PostgreSQL 17
- **DB 접근**: MyBatis
- **템플릿 엔진**: Thymeleaf
- **빌드 도구**: Gradle

## 주요 기능

1. **회원 기능**
    - 회원가입/로그인 (일반)
    - 프로필 이미지 업로드
    - 권한 관리 (USER, ADMIN)

2. **게시글 기능**
    - CRUD (카테고리별 작성)
    - 다중 조건 검색 (제목+내용+작성자+카테고리)
    - 추천(좋아요) 기능

3. **댓글 기능**
    - CRUD
    - 대댓글 (2-depth 계층형)

## 데이터베이스 설계

### 주요 테이블 구조

1. **users**: 사용자 기본 정보 (email, password, nickname 등)
2. **user_images**: 사용자별 프로필 이미지 (1:1)
3. **articles**: 게시글 (제목, 내용, 카테고리 FK 등)
4. **article_categories**: 게시글 카테고리 분류
5. **comments**: 댓글 (부모 댓글 FK로 계층 구현)
6. **article_votes**: 게시글 추천 기록 (사용자별 중복 방지)
