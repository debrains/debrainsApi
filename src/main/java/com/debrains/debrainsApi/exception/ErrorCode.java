package com.debrains.debrainsApi.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {

    // COMMON
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "CO01", "잘못된 입력값 입니다."),
    NO_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "CO02", "권한이 없습니다."),
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "CO03", "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "CO04", "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "CO05", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CO06", "서버 에러입니다."),

    // DATETIME
    ENDDATE_BEFORE_STARTDATE(HttpStatus.BAD_REQUEST, "DA01", "종료일이 시작일보다 빠를 수 없습니다."),
    ENDDATE_BEFORE_NOW(HttpStatus.BAD_REQUEST, "DA02", "종료일이 현재보다 빠를 수 없습니다."),
    ENDTIME_BEFORE_STARTTIME(HttpStatus.BAD_REQUEST, "DA03", "종료시간이 시작시간보다 빠를 수 없습니다."),
    NOW_BEFORE_STARTDATE(HttpStatus.BAD_REQUEST, "DA04", "시작일이 현재 날짜보다 늦습니다."),

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "US01", "존재하지 않는 회원입니다."),
    FOUND_NAME(HttpStatus.CONFLICT, "US02", "이미 존재하는 닉네임입니다."),

    // TIL
    CYCLE_KIND_WRONG(HttpStatus.BAD_REQUEST, "TI01", "인증주기가 잘못됐습니다."),
    CYCLE_CNT_WRONG(HttpStatus.BAD_REQUEST, "TI02", "반복횟수가 잘못됐습니다."),
    TIL_EXPIRED(HttpStatus.BAD_REQUEST, "TI03", "만료된 TIL은 수정할 수 없습니다."),
    TIL_NOT_FOUND(HttpStatus.NOT_FOUND, "TI04", "존재하지 않는 TIL입니다."),

    // TIL_CRT
    DENIED_TIL(HttpStatus.NOT_ACCEPTABLE, "TC01", "관리자에 의해 취소된 인증입니다."),
    UNOPENED_TIL(HttpStatus.FORBIDDEN, "TC02", "비공개된 인증입니다."),
    TILCRT_NOT_FOUND(HttpStatus.NOT_FOUND, "TC03", "존재하지 않는 인증입니다."),
    TILCRT_TODAY(HttpStatus.BAD_REQUEST, "TC04", "스터디 인증은 하루에 1번만 등록 할 수 있습니다."),

    // STUDY
    STUDY_TYPE_WRONG(HttpStatus.BAD_REQUEST, "ST01", "스터디 종류가 잘못됐습니다."),
    STUDY_TOTAL_CNT_WRONG(HttpStatus.BAD_REQUEST, "ST02", "모집인원은 1명 이상이여야 합니다."),
    ENDED_STUDY(HttpStatus.BAD_REQUEST, "ST03", "종료된 스터디입니다."),
    DELETED_STUDY(HttpStatus.BAD_REQUEST, "ST04", "종료된 스터디입니다."),

    // SUPPORT
    UNOPENED_CONTENT(HttpStatus.FORBIDDEN, "SU01", "비공개 게시글입니다."),
    NOT_FOUND_CONTENT(HttpStatus.NOT_FOUND, "SU02", "존재하지 않는 게시글입니다."),

    // FILE
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "FI01", "존재하지 않는 파일입니다.");


    private final HttpStatus status;
    private final String code;
    private String message;

    ErrorCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
