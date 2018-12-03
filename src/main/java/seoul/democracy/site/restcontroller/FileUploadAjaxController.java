package seoul.democracy.site.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import seoul.democracy.common.dto.UploadFileInfo;
import seoul.democracy.common.dto.UploadFileType;
import seoul.democracy.common.service.StorageService;

@RestController
@RequestMapping("/ajax/mypage/files")
public class FileUploadAjaxController {

    private final StorageService storageService;

    @Autowired
    public FileUploadAjaxController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public UploadFileInfo fileUpload(@RequestParam(value = "type", defaultValue = "PROFILE") UploadFileType type, @RequestParam("file") MultipartFile file) {
        return storageService.store(type, file);
    }
}
