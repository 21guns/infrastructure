package com.guns21.upload.util;

import com.guns21.upload.boot.config.QiNiuConfig;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by chhw on 2016/9/28.
 */
public class QiniuUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuUtil.class);

    //密钥配置
    private static Auth auth = null;
    //创建上传对象
    private static UploadManager uploadManager = null;
    //断点续传
    private static UploadManager uploadManagerRecorder = null;

    private static QiniuUtil qiniuUtil = null;

    private static BucketManager bucketManager = null;

    private QiniuUtil() {
    }

    public static QiniuUtil getInstance() {
        if (null == qiniuUtil) {
            qiniuUtil = new QiniuUtil();
            //密钥配置
            auth = Auth.create(QiNiuConfig.ACCESS_KEY, QiNiuConfig.SECRET_KEY);

            ///////////////////////指定上传的Zone的信息//////////////////
            //第一种方式: 指定具体的要上传的zone
            //注：该具体指定的方式和以下自动识别的方式选择其一即可
            //要上传的空间(bucket)的存储区域为华东时
            // Zone z = Zone.zone0();
            //要上传的空间(bucket)的存储区域为华北时
            // Zone z = Zone.zone1();
            //要上传的空间(bucket)的存储区域为华南时
            // Zone z = Zone.zone2();

            //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
            Zone z = Zone.autoZone();
            Configuration c = new Configuration(z);
            //创建上传对象

            uploadManager = new UploadManager(c);

            //实例化recorder对象
            try {
                Recorder recorder = new FileRecorder(QiNiuConfig.BREAKPOINT_UPLOAD_PATH);
                uploadManagerRecorder = new UploadManager(c, recorder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //实例化一个BucketManager对象
            bucketManager = new BucketManager(auth, new Configuration(z));

        }
        return qiniuUtil;
    }

    /**
     *     简单上传，使用默认策略，只需要设置上传的空间名就可以了
     * @return
     */
    public String getUpToken() {
        return auth.uploadToken(QiNiuConfig.BUCKET_NAME);
    }

    /**
     * 覆盖上传
     * @param key 路径
     * @return
     */
    public String getUpToken(String key) {
        //<bucket>:<key>，表示只允许用户上传指定key的文件。在这种格式下文件默认允许“修改”，已存在同名资源则会被本次覆盖。
        return auth.uploadToken(QiNiuConfig.BUCKET_NAME, key);
    }


    //上传策略中设置persistentOps字段和persistentPipeline字段
//    public String getUpTokenOps(String key) {
//        //获取视频文件格式
//        String videoType = FilenameUtils.getExtension(key);
//        key = IJusaiConfig.WBE_UPLOAD_PATH + key;
//        String _720key = null;
//        String _480key = null;
//        if (VideoFormat.MP4.getName().equals(videoType)) {
//            _720key = key.replace(".mp4", "_720.mp4");
//            _480key = key.replace(".mp4", "_480.mp4");
//        } else if (VideoFormat.MKV.getName().equals(videoType)) {
//            _720key = key.replace(".mkv", "_720.mkv");
//            _480key = key.replace(".mkv", "_480.mkv");
//        }
//        //拼接720和480两种预转码格式
//        String pfops = null;
//        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
//        String _720urlbase64 = UrlSafeBase64.encodeToString(QiNiuConfig.BUCKET_NAME + ":" + _720key);
//        String _480urlbase64 = UrlSafeBase64.encodeToString(QiNiuConfig.BUCKET_NAME + ":" + _480key);
//        if (key.indexOf(VideoResolution._1080P.getName()) != 1) {
//            pfops = QiNiuConfig.VIDEO_FORMAT_HD_FOPS + "|saveas/" + _720urlbase64 + ";" + QiNiuConfig.VIDEO_FORMAT_SD_FOPS + "|saveas/" + _480urlbase64;
//        } else if (key.indexOf(VideoResolution._720P.getName()) != 1) {
//            pfops = QiNiuConfig.VIDEO_FORMAT_SD_FOPS + "|saveas/" + _480urlbase64;
//        }
//
//        return auth.uploadToken(QiNiuConfig.BUCKET_NAME, null, 3600, new StringMap()
//                .putNotEmpty("persistentOps", pfops)
//                .putNotEmpty("persistentPipeline", QiNiuConfig.VIDEO_TRANS_CODE_PIPELINE), true);
//    }


    /**
     * 七牛普通上传文件
     *
     * @param file
     * @param key
     * @throws IOException
     */
    public void upload(byte[] file, String key) throws IOException {
        try {
            //调用put方法上传
            Response res = uploadManager.put(file, key, getUpToken());
            //打印返回的信息
            LOGGER.debug("上传文件，qiniu返回的信息: {}", res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            LOGGER.error("上传文件，qiniu返回的异常:{}", r.bodyString());
            try {
                //响应的文本信息
                LOGGER.debug("上传文件，qiniu返回响应的文本信息: {}", r.bodyString());
            } catch (QiniuException e1) {
            }
        }
    }

    /**
     * 分块上传
     *
     * @param file
     * @param key
     * @throws IOException
     */
    public void uploadByResume(File file, String key) throws IOException {
        try {
            //调用put方法上传
            LOGGER.debug("开始上传文件到七牛");
            Response res = uploadManager.put(file, key, getUpToken());
            //打印返回的信息
            LOGGER.debug("上传文件，qiniu返回的信息: {}", res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            LOGGER.error("上传文件，qiniu返回的异常:{}", r.bodyString());
            try {
                //响应的文本信息
                LOGGER.debug("上传文件，qiniu返回响应的文本信息: {}", r.bodyString());
            } catch (QiniuException e1) {
            }
        }
    }

    /**
     * 覆盖上传
     *
     * @param file
     * @param key
     * @throws IOException
     */
    public void uploadByOverride(File file, String key) throws IOException {
        try {
            Response res = uploadManager.put(file, key, getUpToken(key));
            LOGGER.debug("上传文件，qiniu返回的信息:{}", res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            LOGGER.error("上传文件，qiniu返回的异常:{}", r.bodyString());
            try {
                //响应的文本信息
                LOGGER.debug("上传文件，qiniu返回响应的文本信息:{}", r.bodyString());
            } catch (QiniuException e1) {
            }
        }
    }

    /**
     * 七牛断点上传文件
     *
     * @param file
     * @param key
     * @throws IOException
     */
    public void breakpointUpload(byte[] file, String key) throws IOException {

        try {
            //调用put方法上传
            Response res = uploadManagerRecorder.put(file, key, getUpToken());
            //打印返回的信息
            LOGGER.debug("断点上传文件，qiniu返回的信息:{}", res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            LOGGER.error("断点上传文件，qiniu返回的异常:{}", r.bodyString());
            try {
                //响应的文本信息
                LOGGER.debug("断点上传文件，qiniu返回响应的文本信息:{}", r.bodyString());
            } catch (QiniuException e1) {
            }
        }
    }

    public void delete(String key) {
        try {
            //调用delete方法移动文件
            bucketManager.delete(QiNiuConfig.BUCKET_NAME, key);
        } catch (QiniuException e) {
            //捕获异常信息
            LOGGER.error("删除文件异常,key={} ", key);
            LOGGER.error("删除文件,qiniu返回响应的文本信息:", e);

        }
    }

}
