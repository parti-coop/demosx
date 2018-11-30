package seoul.democracy.admin.restcontroller;

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
@RequestMapping("/admin/ajax/files")
public class AdminFileUploadAjaxController {

    private final StorageService storageService;

    @Autowired
    public AdminFileUploadAjaxController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public UploadFileInfo fileUpload(@RequestParam(value = "type", defaultValue = "EDITOR") UploadFileType type, @RequestParam("file") MultipartFile file) {
        return storageService.store(type, file);
    }
}
