package com.guns21.upload.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.guns21.domain.result.light.Result;
import com.guns21.support.controller.AdminBaseController;
import com.guns21.support.util.WebUtils;
import com.guns21.upload.Constants;
import com.guns21.upload.provider.Attachment;
import com.guns21.upload.util.UploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @功能描述:文件上传下载删除公共控制器
 * @创建日期:2016-01-21
 */
@RestController
@RequestMapping("/api/upload/v1")
public class UploadController extends AdminBaseController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    /**
     * 上传文件
     *
     * @param files
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List list = Lists.newArrayList();
        for (MultipartFile fileData : files) {
            Map map = new HashMap();
            Attachment attachment = UploadUtils.uploadImage(fileData, request);
            String buildWebUrlWithFile = WebUtils.buildWebUrlWithFile(attachment.getSavePath(), attachment.getSaveName());
            map.put("url", buildWebUrlWithFile);
            String buildRelativeUrlWithFile = WebUtils.buildRelativeUrlWithFile(attachment.getSavePath(), attachment.getSaveName());
            map.put("rUrl", buildRelativeUrlWithFile);
            list.add(map);
        }
        return Result.success(list);
    }

    /**
     * 上传头像
     *
     * @param files
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/header", method = RequestMethod.POST)
    public Result uploadHeaderImage(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List list = Lists.newArrayList();
        for (MultipartFile fileData : files) {
            Map map = new HashMap();
            Attachment attachment = UploadUtils.uploadHeaderImage(fileData, request);
            String buildWebUrlWithFile = WebUtils.buildWebUrlWithFile(attachment.getSavePath(), attachment.getSaveName());
            map.put("url", buildWebUrlWithFile);
            String buildRelativeUrlWithFile = WebUtils.buildRelativeUrlWithFile(attachment.getSavePath(), attachment.getSaveName());
            map.put("rUrl", buildRelativeUrlWithFile);
            list.add(map);
        }
        return Result.success(list);
    }

    /**
     * 大文件上传支持
     *
     * @param files
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/large", method = RequestMethod.POST)
    public Result uploadFile(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) throws Exception {
        List list = Lists.newArrayList();
        Map<String, String> map = Maps.newHashMap();
        //循环获取file数组中得文件
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];

                //视频本地保存
                Attachment attachment = UploadUtils.uploadVideo(file, request);

                //返回对象
                DecimalFormat df = new DecimalFormat("######0.00");
                try {
                    String buildRelativeUrlWithFile = WebUtils.buildRelativeUrlWithFile(attachment.getSavePath(), attachment.getSaveName());
                    map.put("url", buildRelativeUrlWithFile);
                    map.put("realName", attachment.getName());
                    map.put("size", df.format(file.getSize() / Constants.ONE_M));
                    map.put("state", "SUCCESS");
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("state", "Fail");
                }
                list.add(map);
            }
        }
        logger.info("上传返回参数" + map);
        return Result.success(list);
    }

    /**
     * 富文本编辑器上传文件
     *
     * @param files
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/editor", method = RequestMethod.POST)
    public void uploadForEditor(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (MultipartFile fileData : files) {
            Attachment attachment = UploadUtils.uploadImage(fileData, request);
            String buildWebUrlWithFile = WebUtils.buildWebUrlWithFile(attachment.getSavePath(), attachment.getSaveName());
            response.getWriter().write(buildWebUrlWithFile);
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestParam String filePath) throws Exception {
        UploadUtils.deleteFile(filePath);
        return Result.success();
    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePath = request.getParameter("filePath");
        String fileNme = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        this.resetDownloadFileName(response, fileNme);
        OutputStream out = response.getOutputStream();
        UploadUtils.downloadFile(filePath, out);
        out.flush();
        out.close();
    }

    public void resetDownloadFileName(HttpServletResponse response, String fileName) throws Exception {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso-8859-1"));
        response.setContentType("application/octet-stream");
    }
}
