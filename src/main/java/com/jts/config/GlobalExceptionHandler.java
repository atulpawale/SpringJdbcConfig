package com.jts.config;

import com.jts.utils.MailService;
import com.mysql.cj.jdbc.exceptions.MySQLTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLClientInfoException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    MailService mailService;

    @ExceptionHandler({SQLException.class, SQLDataException.class, SQLClientInfoException.class, SQLSyntaxErrorException.class,
            BadSqlGrammarException.class, MySQLTimeoutException.class, DataIntegrityViolationException.class})
    public void handleExceptions(Exception exception, WebRequest webRequest) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        mailService.sendEmail("pawaleatul17@gmail.com", "Error Occurred: "+exception.getMessage(), sw.toString());
        throw exception;
    }
}
