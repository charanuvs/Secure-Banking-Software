package edu.asu.securebanking.controller;

import edu.asu.securebanking.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller

@RequestMapping("/all/download")
public class FileDownloadController {


    @Autowired
    ServletContext context; // Must needed

    private static final int BUFFER_SIZE = 4096;

    /**
     * Path of the file to be downloaded, relative to application's directory
     */
    private String filePath = AppConstants.APPLET_LOCATION;

    // Hardcoded path where all files is present

    /**
     * Method for handling file download request from client
     */
    @RequestMapping(method = RequestMethod.GET)

    public void doDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {

        File file = new File(filePath);
        FileInputStream in = new FileInputStream(file);
        byte[] content = new byte[(int) file.length()];
        in.read(content);
        ServletContext sc = request.getSession().getServletContext();
        String mimetype = sc.getMimeType(file.getName());
        response.reset();
        response.setContentType(mimetype);
        response.setContentLength(content.length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        org.springframework.util.FileCopyUtils.copy(content, response.getOutputStream());

    }
}