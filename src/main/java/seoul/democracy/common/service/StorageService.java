package seoul.democracy.common.service;

import org.imgscalr.Scalr;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import seoul.democracy.common.dto.UploadFileInfo;
import seoul.democracy.common.dto.UploadFileType;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class StorageService {

    private final Resource uploadPath = new ClassPathResource("../../");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("/yyyy/MM");

    public UploadFileInfo store(UploadFileType type, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new NotFoundException("empty file : " + multipartFile.getOriginalFilename());
        }

        /* save file */
        try {
            /* make normalized uuid filename */
            String normalizedFilename = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);
            normalizedFilename = normalizedFilename.replace(" ", "_");
            String filename = UUID.randomUUID().toString().replace("-", "") + "_" + normalizedFilename;

            String uploadPath = "/files" + LocalDate.now().format(dateTimeFormatter);

            File file = new File(this.uploadPath.getFile().getPath() + uploadPath, filename);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

            if (type == UploadFileType.ORIGINAL)
                FileCopyUtils.copy(multipartFile.getBytes(), file);
            else
                resizeImage(multipartFile.getInputStream(), file, type);

            return UploadFileInfo.of(multipartFile.getOriginalFilename(), uploadPath + "/" + filename);
        } catch (IOException e) {
            throw new BadRequestException("file", "error.file", e.getMessage());
        }
    }

    private void resizeImage(InputStream inputStream, File file, UploadFileType type) throws IOException {
        BufferedImage sourceImg = ImageIO.read(inputStream);

        if (sourceImg == null) {
            throw new BadRequestException("file", "error.file", "이미지 파일이 아닙니다.");
        }

        int thumbnailWidth = type.getWidth();
        int thumbnailHeight = type.getHeight();

        BufferedImage dest;
        if (thumbnailHeight == 0) { // 가로만 맞추는 경우
            if (sourceImg.getWidth() < type.getWidth()) {
                dest = sourceImg;
            } else {
                dest = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, thumbnailWidth);
            }
        } else if (sourceImg.getWidth() * thumbnailHeight > sourceImg.getHeight() * thumbnailWidth) {   // width > height - 가로가 길어서 가로를 잘라서 조절하는 경우
            BufferedImage resizeImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, thumbnailWidth, thumbnailHeight);
            dest = Scalr.crop(resizeImg, (resizeImg.getWidth() - thumbnailWidth) / 2, 0, thumbnailWidth, thumbnailHeight);
        } else {    // 세로가 길어서 세로를 잘라서 조절하는 경우
            BufferedImage resizeImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, thumbnailWidth, thumbnailHeight);
            dest = Scalr.crop(resizeImg, 0, (resizeImg.getHeight() - thumbnailHeight) / 2, thumbnailWidth, thumbnailHeight);
        }

        ImageIO.write(dest, StringUtils.getFilenameExtension(file.getName()), file);
    }
}
