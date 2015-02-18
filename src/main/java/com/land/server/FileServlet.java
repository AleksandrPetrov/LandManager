package com.land.server;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.land.server.dao.FileDao;
import com.land.server.dao.JpaDao;
import com.land.server.dao.ObjectDao;
import com.land.server.domain.BlobEntity;
import com.land.server.domain.File;
import com.land.server.security.CurrentUser;

@Controller
@Secured({CurrentUser.ROLE_USER})
public class FileServlet {
    private static final String FSEP = ":fsep;";
    private static final String PSEP = ":psep;";
    private static final int MAX_REQUEST_SIZE = 50 * 1024 * 1024; // 50 Mb

    public static final Logger log = Logger.getLogger(JpaDao.class);

    @Autowired
    private FileDao fileDao;

    @Autowired
    private ObjectDao objectDao;

    private String getOnlyName(String path) {
        try {}
        catch (Exception e) {}
        int i = path.lastIndexOf("\\") + 1;
        return i < 0 ? path : path.substring(i);
    }

    public static Long getLongParameter(HttpServletRequest req, String parametr) {
        Long longParametr = null;
        try {
            longParametr = Long.valueOf(req.getParameter(parametr));
        }
        catch (Exception e) {}
        return longParametr;
    }

    @RequestMapping(value = "/addfile", method = {RequestMethod.POST})
    @Transactional
    public void handleAddFile(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String result = "";
        req.setCharacterEncoding("UTF-8");
        try {
            if (req.getContentLength() <= MAX_REQUEST_SIZE) {
                if (ServletFileUpload.isMultipartContent(req)) {
                    ServletFileUpload upload = new ServletFileUpload();
                    FileItemIterator iterator = upload.getItemIterator(req);
                    while (iterator.hasNext()) {
                        FileItemStream item = iterator.next();
                        if (!item.isFormField()) {
                            Long objectId = getLongParameter(req, "objectId");
                            com.land.server.domain.File file = storeFile(item);

                            if (objectId != null) file.setObject(objectDao.findById(objectId));
                            fileDao.persist(file);

                            result += file.getId() + PSEP + file.getName() + PSEP + file.getSize() + FSEP;
                        }
                    }
                    res.setCharacterEncoding("UTF-8");
                    res.setContentType("text/HTML");
                    res.setStatus(HttpServletResponse.SC_OK);
                    res.getWriter().print(result);
                }
            }
            else {
                res.setCharacterEncoding("UTF-8");
                res.setContentType("text/HTML");
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.getWriter().print("Не удалось сохранить файл. Превышен максимальный размер 50 Мб.");
            }
        }
        catch (Exception e) {
            log.error("Unable to upload file", e);
            res.setCharacterEncoding("UTF-8");
            res.setContentType("text/HTML");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().print("Не удалось сохранить файл");
        }
    }

    private File storeFile(FileItemStream item) throws Exception {
        InputStream input = null;
        try {
            input = item.openStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(input, output);

            BlobEntity blob = new BlobEntity();
            byte[] arr = output.toByteArray();
            blob.setContent(arr);

            File file = new File();
            file.setName(getOnlyName(item.getName()));
            file.setContentType(item.getContentType());
            file.setOwner(CurrentUser.getLogin());
            file.setSize((long) arr.length);
            file.setBlob(blob);

            return file;
        }
        finally {
            IOUtils.closeQuietly(input);
            System.gc();
        }
    }

    @RequestMapping(value = "/getfile/*", method = {RequestMethod.GET})
    public void handleGetFile(@RequestParam("id") Long id, HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        File file = null;
        try {
            file = fileDao.findById(id);
        }
        catch (Exception e) {
            log.error("Unable to find file", e);
        }
        if (file != null) {
            InputStream inputStream = null;
            OutputStream outStream = null;
            try {
                inputStream = fileDao.findBlobByFileId(id).getContentInputStream();
                int fileSize = inputStream.available();

                res.setContentType(file.getContentType());
                res.setContentLength(fileSize);
                res.setHeader("Content-Disposition", "inline;");

                outStream = res.getOutputStream();
                IOUtils.copy(inputStream, outStream);

            }
            catch (Exception ee) {
                log.error("Unable to download file", ee);
            }
            finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outStream);
                System.gc();
            }
        }
        else {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("text/HTML");
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().print("Запрашиваемый файл не найден");
        }
    }
}
