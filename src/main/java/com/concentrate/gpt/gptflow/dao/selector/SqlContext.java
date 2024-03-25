package com.concentrate.gpt.gptflow.dao.selector;

import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 2017/6/8.
 */
public class SqlContext {
    private static Logger logger = LoggerFactory
            .getLogger(SqlContext.class);
    static XStream xstream = new XStream();
    static{
        loadSql();
    }
    private static final Map<String,String> sqlContextHolder = new ConcurrentHashMap<String,String>();

    public static String getSqlByModule(String module){
        return sqlContextHolder.get(module);
    }

    public static void loadSql(){
        Thread t = new Thread(){
            public void run(){
                File f = new File(SqlContext.class.getClassLoader().getResource("conf/selectorSql/sqlContext.xml").getFile());
                InputStream fis = SqlContext.class.getClassLoader().getResourceAsStream("conf/selectorSql/sqlContext.xml");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bfr = new BufferedReader(isr);
                StringBuffer all = new StringBuffer();
                String bufStr = "";
                try {
                    while((bufStr = bfr.readLine())!=null){
                        all.append(bufStr).append("\\r\\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Map<String,String> deserialMap = (Map<String, String>) xstream.fromXML(all.toString());
                synchronized (sqlContextHolder){
                    sqlContextHolder.clear();
                    sqlContextHolder.putAll(deserialMap);
                }
            }
        };
        t.setName("sqlLoaderThread");
        //t.setDaemon(true);
        t.start();

    }
}
