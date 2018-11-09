package seoul.democracy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UploadFileInfo {

    private final String filename;
    private final String url;
}
