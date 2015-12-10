package edu.asu.securebanking.view;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.Transaction;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Vikranth on 10/28/2015.
 */
public class AccountExcelView extends AbstractExcelView {

    private static NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        List<Transaction> transactions = (List<Transaction>) model.get("transactions");
        Account account = (Account) model.get("account");
        AppUser appUser = (AppUser) model.get("appUser");

        HSSFSheet sheet = workbook.createSheet("Account Summary");
        HSSFRow row;

        HSSFRow head = sheet.createRow(0);
        head.createCell(0).setCellValue("Transaction Information for Account");
        head.createCell(1).setCellValue(account.getAccountNum());

        HSSFRow balance = sheet.createRow(1);
        balance.createCell(0).setCellValue("Current balance: ");
        balance.createCell(1).setCellValue(account.getBalance().doubleValue());

        if (transactions == null || transactions.size() == 0) {
            row = sheet.createRow(3);
            row.createCell(0).setCellValue("No transactions for this account");
            return;
        }

        row = sheet.createRow(3);

        row.createCell(0).setCellValue("Date");
        row.createCell(1).setCellValue("From Account");
        row.createCell(2).setCellValue("To Account");
        row.createCell(3).setCellValue("Amount");
        row.createCell(4).setCellValue("Transaction Type");
        row.createCell(5).setCellValue("Transaction Status");

        int currentRow = 4;
        for (Transaction transaction : transactions) {
            row = sheet.createRow(currentRow);

            row.createCell(0).setCellValue(DATE_FORMAT.format(transaction.getDate()));

            // from account
            if (transaction.getFromAccount().getUser().getUserId().equals(appUser.getUserId()))
                row.createCell(1).setCellValue(transaction.getFromAccount().getAccountNum() + " (Your account)");
            else
                row.createCell(1).setCellValue(transaction.getFromAccount().getUser().getName());

            // to account
            if (transaction.getToAccount().getUser().getUserId().equals(appUser.getUserId()))
                row.createCell(2).setCellValue(transaction.getToAccount().getAccountNum() + " (Your account)");
            else
                row.createCell(2).setCellValue(transaction.getToAccount().getUser().getName());

            // amount
            row.createCell(3).setCellValue(NUMBER_FORMAT.format(transaction.getAmount()));

            // transaction type
            row.createCell(4).setCellValue(transaction.getTransactionType());

            // transaction status
            row.createCell(5).setCellValue(transaction.getStatus());

            currentRow++;
        }
        //
    }
}
