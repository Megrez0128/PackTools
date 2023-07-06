package com.zulong.web.log;

import java.io.FileInputStream;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

public class LoggerManager
{
    private static Logger logger = null;

    public static void init()
    {
        System.out.println("configuring log4j with log4j.xml");

        try
        {
            System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.core.impl.Log4jContextFactory");
            final ConfigurationSource source = new ConfigurationSource(new FileInputStream("log4j.xml"));
            final XmlConfiguration xmlConfiguration = new XmlConfiguration(null, source);
            org.apache.logging.log4j.core.config.Configurator.initialize(xmlConfiguration);
            logger = org.apache.logging.log4j.LogManager.getLogger("root");

            System.out.println("configure log4j with log4j.xml done");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return
     */
    public static Logger logger()
    {
        return logger;
    }
}
