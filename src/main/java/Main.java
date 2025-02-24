import config.ApplicationConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        try (InputStream stream = Main.class.getResourceAsStream("application.properties")){
            properties.load(stream);
        }

        importPropertiesFromEnvIfExists(properties);

        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationConfig.class);
        appContext.getEnvironment().getPropertySources().addLast(new PropertiesPropertySource("applicationEnvironment", properties));
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);

        Tomcat tomcat = configureTomcat(Integer.parseInt(properties.getProperty("port")), dispatcherServlet);
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();
    }

    private static void importPropertiesFromEnvIfExists(Properties properties){
        String portFromEnv = System.getenv().get("INNER_PORT");
        if (portFromEnv != null) properties.setProperty("port", portFromEnv);
        String source = System.getenv().get("SOURCE");
        if (source != null) properties.setProperty("source", source);
        String openWeatherApiKey = System.getenv().get("OPEN_WEATHER_API_KEY");
        if (openWeatherApiKey != null) properties.setProperty("openWeatherApiKey", openWeatherApiKey);
        String yandexWeatherApiKey = System.getenv().get("YANDEX_WEATHER_API_KEY");
        if (yandexWeatherApiKey != null) properties.setProperty("yandexWeatherApiKey", yandexWeatherApiKey);
        String yandexGeocoderApiKey = System.getenv().get("YANDEX_GEOCODER_API_KEY");
        if (yandexGeocoderApiKey != null) properties.setProperty("yandexGeocoderApiKey", yandexGeocoderApiKey);
    }

    private static Tomcat configureTomcat(int port, DispatcherServlet dispatcherServlet) {
        Tomcat tomcat = new Tomcat();
        final Connector connector = new Connector();
        connector.setPort(port);
        connector.setScheme("http");
        connector.setSecure(false);
        tomcat.setConnector(connector);
        tomcat.setAddDefaultWebXmlToWebapp(false);
        File webAppDir = new File(tomcat.getServer().getCatalinaBase(), "webapps");
        webAppDir.mkdir();
        Context context = tomcat.addWebapp("", webAppDir.getAbsolutePath());
        Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet).setLoadOnStartup(1);
        context.addServletMappingDecoded("/*", "dispatcherServlet");
        return tomcat;
    }
}
