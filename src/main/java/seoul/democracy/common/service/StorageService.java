package seoul.democracy.common.service;

import egovframework.rte.fdl.property.EgovPropertyService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import seoul.democracy.common.dto.UploadFileInfo;
import seoul.democracy.common.dto.UploadFileType;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class StorageService {

    private final String systemUploadPath;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM");

    @Autowired
    public StorageService(EgovPropertyService propertyService) {
        this.systemUploadPath = propertyService.getString("uploadFilePath");
        log.info("uploadFilePath : [{}]", systemUploadPath);
    }

    public UploadFileInfo store(UploadFileType type, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new NotFoundException("empty file : " + multipartFile.getOriginalFilename());
        }

        /* save file */
        try {
            String filename = UUID.randomUUID().toString().replace("-", "") + "." +
                                  StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

            String uploadPath = LocalDate.now().format(dateTimeFormatter);

            File file = new File(this.systemUploadPath + uploadPath, filename);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

            if (type == UploadFileType.ORIGINAL)
                FileCopyUtils.copy(multipartFile.getBytes(), file);
            else {
                BufferedImage sourceImg = Thumbnails.of(multipartFile.getInputStream()).scale(1).asBufferedImage();
                if (sourceImg == null)
                    throw new BadRequestException("file", "error.file", "이미지 파일이 아닙니다.");

                if (type.getHeight() == 0) {
                    if (sourceImg.getWidth() > type.getWidth()) {
                        Thumbnails.of(sourceImg).width(type.getWidth()).toFile(file);
                    } else {
                        Thumbnails.of(sourceImg).scale(1).toFile(file);
                    }
                } else {
                    Thumbnails.of(sourceImg).size(type.getWidth(), type.getHeight())
                        .crop(Positions.CENTER)
                        .keepAspectRatio(true)
                        .toFile(file);
                }
            }

            return UploadFileInfo.of(multipartFile.getOriginalFilename(), "/files/" + uploadPath + "/" + filename);
        } catch (IOException e) {
            log.info("{}", e);
            throw new BadRequestException("file", "error.file", e.getMessage());
        }
    }
}
