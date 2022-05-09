# Debrains
> 개발자를 위한 자기개발 스터디 커뮤니티

![Badge](https://img.shields.io/github/workflow/status/debrains/debrainsApi/deploy-production)
![Badge](https://img.shields.io/badge/Version-1.0.0-1177AA?style=flat-square)

{로고}

## 목차
- [서비스 소개](#서비스-소개)
- [기능](#기능)
- [기술스택](#기술스택)
- [아키텍처](#아키텍처)
    - [배포](#배포)
- [API명세서](#api명세서)
- [업데이트 내역](#업데이트-내역)
- [Contact Us](#contact-us)

## 서비스 소개
<strong>끊임없이 자기개발하는 모든 개발자를 위한 스터디 커뮤니티 입니다.</strong>

혼자 공부만 하기 심심했다면 기록을 남겨 인증해보세요!  
혼자 공부만 하기 지루했다면 같이 스터디를 해보세요!

개발자들의 두뇌 하나 하나가 모여 같이 성장해요

## 기능
1. TIL - 공부인증
    - 공부 목표를 설정하고 인증해보세요  
      {사진}

2. 스터디 (개발중)
    - 여러 개발자들과 같이 목표달성을 위해 공부해보세요

## 기술스택
|분야|사용 기술|
|:--:|:--:|
| FrontEnd | React, JavaScript |
| BackEnd        | JAVA, Spring Framework(Security, Hateoas, Rest Docs), JPA, QueryDSL     |
| Database       | MariaDB             |
| Cloud Services | AWS EC2, RDS, S3, CloudFront, Route 53 |
| CI / CD | Github Actions, AWS CodeDeploy |
| VCS | Git, Github |
| TOOL           | Jira, Confluence, Slack |

## 아키텍처
![image](https://user-images.githubusercontent.com/48157259/166197453-91e028e5-2b95-4df1-9326-f97e2df480d0.png)
- Frond-End : AWS S3
- Back-End : AWS EC2

### 배포
![image](https://user-images.githubusercontent.com/48157259/166197506-cb69b02b-99c6-4869-aadf-46a31905180a.png)
- CI : Github Actions / CD : AWS CodeDeploy
- `develop` 브랜치 : 스테이징서버 / `master` 브랜치 : 운영서버


## API명세서
[API명세서 이동](https://api.debrain.co.kr/docs/index.html)

## 업데이트 내역
- 1.0.0
    - TIL 추가

## Contact Us
[![Gmail Badge](https://img.shields.io/badge/Gmail-d14836?style=flat-square&logo=Gmail&logoColor=white&link=mailto:debrains.team@gmail.com)](mailto:debrains.team@gmail.com)
[![Tech Blog Badge](http://img.shields.io/badge/Blog-7E22CE?style=flat-square&logo=talend&link=https://blog.debrain.co.kr/)](https://blog.debrain.co.kr/)