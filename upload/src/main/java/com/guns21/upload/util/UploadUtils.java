package com.guns21.upload.util;

import com.guns21.common.util.DateUtils;
import com.guns21.common.uuid.ID;
import com.guns21.support.boot.config.WebConfig;
import com.guns21.support.util.WebUtils;
import com.guns21.upload.Constants;
import com.guns21.upload.boot.config.QiNiuConfig;
import com.guns21.upload.boot.config.UploadConfig;
import com.guns21.upload.provider.Attachment;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * Created by ljun on 15/4/28.
 */
public class UploadUtils {
    private static final Logger logger = LoggerFactory.getLogger(UploadUtils.class);

    public static String getFilePath() {
        return UploadConfig.UPLOAD_ROOT_PATH;
    }

    public static String getHtmlDirectory() {
        return "html/";
    }

    public static String getImageDirectory() {
        return "images/";
    }

    public static String getHeaderDirectory() {
        return "headers/";
    }

    public static String getVideoDirectory() {
        return "videos/";
    }


    public static Attachment uploadImage(MultipartFile file, HttpServletRequest request) throws IOException {
        if (!isImage(file.getOriginalFilename())) {
            throw new IllegalArgumentException("not image");
        }
        return uploadFile(file, request, getImageDirectory(), false, "image");
    }

    public static Attachment uploadHeaderImage(MultipartFile file, HttpServletRequest request) throws IOException {
        return uploadFile(file, request, getHeaderDirectory(), false, "header");
    }

    public static Attachment uploadVideo(MultipartFile file, HttpServletRequest request) throws IOException {
        return uploadFile(file, request, getVideoDirectory(), false, "video");
    }

    public static Attachment uploadFile(MultipartFile file, HttpServletRequest request, String saveDir, boolean thumb, String attachType) throws IOException {
        Attachment attachment = Attachment.newAttachment();

        attachment.setSize(file.getSize() + "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        attachment.setExtension(ext);
        String uuidFN = ID.get();
        attachment.setSaveName(uuidFN + "." + ext);
        String savePath = saveDir + getDateDir();
        attachment.setSavePath(savePath);
        attachment.setName(new String(file.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8"));
        attachment.setType(request.getContentType());
        attachment.setAttachType(attachType);

        //1.存储本地
        String path = getFilePath() + attachment.getSavePath();
        File destination = new File(path, attachment.getSaveName());
        FileUtils.copyInputStreamToFile(file.getInputStream(), destination);

        //缩略图，文件名：saveName_uploadJpgThumbnailMaxPixel_uploadJpgThumbnailMaxPixel.png
        if (thumb) {
            File sourceFile = new File(path, attachment.getSaveName());
            uuidFN = uuidFN + "_" + UploadConfig.UPLOAD_IMG_THUMBNAIL_MAX_PIXEL + "_" + UploadConfig.UPLOAD_IMG_THUMBNAIL_MAX_PIXEL + "." + ext;
            saveThumb(new FileInputStream(sourceFile), new File(path, uuidFN));
        }
        //2.七牛
        if ("video".equals(attachType)) {
            //大文件使用页面端上传
//            QiniuUtil.getInstance().uploadByResume(destination, getQiniuFileKey(attachment));
        } else {
//            QiniuUtil.getInstance().upload(file.getBytes(), getQiniuFileKey(attachment));
        }

        //文件内容hash值
//        try {
//            attachment.setHash(getHash(attachment.getSavePath()+attachment.getSaveName(), "MD5"));
//        } catch (Exception e) {
//            logger.error("计算HASH值错误",e);
//        }

        return attachment;
    }

    /* --------------------------------------------------- html  --------------------------------------------------- */

    public static Attachment uploadHtmlFile(String content, String savePathExists) throws IOException {
        if (Objects.isNull(content)) {
            return null;
        }
        Attachment attachment = Attachment.newAttachment();
        attachment.setSize(content.length() + "");
        attachment.setExtension("html");
        attachment.setAttachType("html");
        if (Objects.nonNull(savePathExists) && !"undefined".equalsIgnoreCase(savePathExists)) {
            attachment.setSaveName(FilenameUtils.getName(savePathExists));
            attachment.setSavePath(FilenameUtils.getPath(savePathExists));
        } else {
            attachment.setSaveName(ID.get() + "." + attachment.getExtension());
            String savePath = getHtmlDirectory() + getDateDir();
            attachment.setSavePath(savePath);
        }
        attachment.setName(attachment.getSaveName());

        //1.存储本地
        String path = getFilePath() + attachment.getSavePath();
        File destination = new File(path, attachment.getSaveName());
        FileUtils.writeStringToFile(destination, content, "UTF-8");
        //文件内容hash值
//        try {
//            attachment.setHash(getHash(attachment.getSavePath()+attachment.getSaveName(), "MD5"));
//        } catch (Exception e) {
//            logger.error("计算HASH值错误",e);
//        }

        //2.七牛
//        QiniuUtil.getInstance().uploadByOverride(destination, getQiniuFileKey(attachment));

        return attachment;
    }

    public static Attachment uploadOriginHtmlFile(String content, String savePathExists) throws IOException {
        Attachment attachment = UploadUtils.uploadHtmlFile(content, toOriginHtmlFileName(savePathExists));
        return attachment;
    }

    public static String toOriginHtmlFileName(String path) {
        String dir = FilenameUtils.getPath(path);
        String baseName = FilenameUtils.getBaseName(path);
        return dir + baseName + Constants.HTML_FILE_ORIGIN_POSTFIX + ".html";
    }

    public static String uploadHtmlFileReturnUrl(String content, String savePathExists) throws IOException {
        Attachment attachment = UploadUtils.uploadHtmlFile(content, savePathExists);
        if (Objects.isNull(attachment)) {
            return null;
        }
        return WebUtils.buildRelativeUrlWithFile(attachment.getSavePath(), attachment.getSaveName());
    }

    public static String getHtmlSaveKey() {
        return getHtmlDirectory() + getDateDir() + ID.get() + ".html";
    }

    /**
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return;
        }
        String root = getFilePath() + filePath;
        File file = new File(root);
        file.deleteOnExit();

        //// TODO: 2016/11/8 qiniu throw java.lang.UnsupportedOperationException: null
//        QiniuUtil.getInstance().delete(filePath);
    }


    public static String getFileContent(String filePath) throws IOException {
        if (Objects.isNull(filePath)) {
            return null;
        }
        try {
            return FileUtils.readFileToString(new File(getFilePath() + filePath));
        } catch (IOException e) {
            logger.error("file exception", e);
            return e.getLocalizedMessage();
        }
    }

    /**
     * 文件下载
     *
     * @param filePath
     * @param out
     * @throws Exception
     */
    public static void downloadFile(String filePath, OutputStream out) throws Exception {
        String root = getFilePath() + filePath;
        File file = new File(root);
        if (file.exists()) {
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            OutputStream toClient = new BufferedOutputStream(out);
            toClient.write(buffer);
        }
    }


    /**
     * cdn存储刷新缓存
     * 例如，如果 http://7xt44n.com2.z0.glb.qiniucdn.com/qiniu-picture5.jpg 这个图片资源没有更新，
     * 可以在该 URL 后面加上参数的形式来让 CDN 强制刷新：http://7xt44n.com2.z0.glb.qiniucdn.com/qiniu-picture5.jpg?v=20130910，
     * CDN 拿到这个 URL 后会强制回七牛的存储中取回最新的资源
     *
     * @param url
     * @return
     */
    public static String buildWebUrlWithVersion(String url, long version) {
        return WebUtils.buildWebUrlWithFile(url + "?v=" + version);
    }

    public static String buildWebUrlWithPxiel(String url, String pxiel) {
        return WebUtils.buildWebUrlWithFile(url + "?" + QiNiuConfig.IMAGE_ZOOM_PIXEL + pxiel);
    }

    /* --------------------------------------------------- private ---------------------------------------------------  */

    private static boolean isImage(String fileName) {
        String ext = FilenameUtils.getExtension(fileName);
        if (!StringUtils.hasText(ext)) {
            return false;
        }
        ext = ext.toLowerCase();
        if ("jpg".equals(ext) || "png".equals(ext) || "bmp".equals(ext) || "jpeg".equals(ext)) {
            return true;
        }
        return false;
    }

    private static void saveThumb(InputStream inputStream, File thumbPath) {
        //save thumb
        try {
            Thumbnails.of(inputStream)
                    .size(UploadConfig.UPLOAD_IMG_THUMBNAIL_MAX_PIXEL, UploadConfig.UPLOAD_IMG_THUMBNAIL_MAX_PIXEL)
                    .toFile(thumbPath);
            IOUtils.closeQuietly(inputStream);
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param fileName
     * @param hashType "MD5"，"SHA1"，"SHA-256"，"SHA-384"，"SHA-512"
     * @return
     * @throws Exception
     */
    private static String getHashFile(File fileName, String hashType)
            throws Exception {
        InputStream fis = new FileInputStream(fileName);
        byte[] buffer = new byte[1024];
        MessageDigest md5 = MessageDigest.getInstance(hashType);
        for (int numRead = 0; (numRead = fis.read(buffer)) > 0; ) {
            md5.update(buffer, 0, numRead);
        }
        fis.close();
        return Hex.encodeHexString(md5.digest());
    }

    /**
     * @param content content
     * @param hashType "MD5"，"SHA1"，"SHA-256"，"SHA-384"，"SHA-512"
     * @return
     * @throws Exception
     */
    private static String getHash(String content, String hashType)
            throws Exception {

        MessageDigest md5 = MessageDigest.getInstance(hashType);
        md5.update(content.getBytes("UTF-8"));
        return Hex.encodeHexString(md5.digest());
    }

    private static String getDateDir() {
        return DateUtils.format(DateUtils.newDate(), "yyyy/MM/dd") + "/";
    }

    private static String getQiniuFileKey(Attachment attachment) {
        if (StringUtils.isEmpty(attachment.getSavePath()) || StringUtils.isEmpty(attachment.getSaveName())) {
            throw new IllegalArgumentException();
        }
        return WebConfig.WBE_UPLOAD_PATH +
//                (attachment.getSavePath().startsWith("/")? attachment.getSavePath().substring(1):attachment.getSavePath())
                attachment.getSavePath()
                + attachment.getSaveName();
    }

}

