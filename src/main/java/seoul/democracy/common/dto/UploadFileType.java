package seoul.democracy.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UploadFileType {
    PROFILE(320, 320),  // 프로필 용도
    THUMBNAIL(800, 600),  // 썸네일 용도
    THUMBNAIL2(700, 960),  // 썸네일 용도
    EDITOR(1200, 0),     // 글 적는 용도
    SLIDER(1920, 800),     // 슬라이더 용도
    ORIGINAL(0, 0);    // 첨부파일

    private final int width;
    private final int height;
}
