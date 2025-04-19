package science.workbook.dto.api;

public enum ApiServerMessage implements ApiMessage {
    회원가입_성공("회원가입에 성공했습니다.\n 로그인 해주세요", 200),
    유저정보_성공("유저 정보 조회에 성공했습니다.", 200),
    회원탈퇴_성공("회원탈퇴에 성공했습니다. \n 그동안 저희 서비스를 이용해주셔서 감사합니다.", 200),
    새로운_비밀번호_생성("새로운 비밀번호가 해당하는 이메일에 발송되었습니다.\n새로운 비밀번호로 로그인해주세요.", 200),
    요청_성공("Success Request \n", 200),
    비밀번호_변경_성공("비밀번호 변경 완료했습니다. 다시 로그인해주세요.", 200),
    로그인_성공("로그인 성공했습니다.", 200),
    토큰_재발급_성공("토큰 재발급 성공했습니다.", 200),
    이메일_확인_성공("이메일 코드 확인 되었습니다. 로그인해주세요!!", 200),
    이메일_존재_성공("가입한 이메일입니다. 해당 비밀번호로 로그인 해주세요.", 200),
    과목_리스트("과목 리스트입니다. 선택해주세요.", 200),
    과목_추가_성공("새로운 과목이 추가되었습니다", 200),
    과목_삭제_성공("과목 삭제 성공하였습니다.", 200),
    목차_리스트("목차 리스트입니다. 선택해주세요", 200),
    목차_생성_성공("새로운 목차가 추가되었습니다.", 200),
    목차_삭제_성공("목차 삭제 성공하였습니다.", 200)
    ;

    private final String message;
    private final Integer code;

    ApiServerMessage(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
