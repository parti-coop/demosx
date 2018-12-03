package seoul.democracy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ResultRedirectInfo {

    final private String msg;

    final private String url;
}
