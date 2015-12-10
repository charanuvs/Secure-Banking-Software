package edu.asu.securebanking.service;

import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikranth on 10/29/2015.
 */
public class SystemLogService {

    private static Logger LOGGER = Logger.getLogger(SystemLogService.class);

    /**
     * @return logs
     */
    public List<String> getLog() throws AppBusinessException {

        List<String> logs = new ArrayList<>();

        int logSize = AppConstants.MAX_LOG_SIZE;
        int count = 0;
        try {

            File f = new File(AppConstants.LOG_FILE);
            ReversedLinesFileReader reader = new ReversedLinesFileReader(f);
            String l;
            while ((l = reader.readLine()) != null && count < logSize) {
                logs.add(l);
                count++;
            }
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AppBusinessException("Error while reading log file.");
        } catch (Exception e) {
            LOGGER.error(e);
            throw new AppBusinessException("Error while reading log file.");
        }

        return logs;
    }

}
