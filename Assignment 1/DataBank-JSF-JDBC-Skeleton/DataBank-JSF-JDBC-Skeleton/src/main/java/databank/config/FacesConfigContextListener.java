/************************************************************************
 * File: FacesConfigAndServletContextListener.java Course materials (21W) CST8277
 *
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Original name FacesConfigAndServletContextLister.java was too long for github
 */
package databank.config;

import static javax.faces.application.ProjectStage.PROJECT_STAGE_PARAM_NAME;
import static javax.faces.application.ViewHandler.FACELETS_REFRESH_PERIOD_PARAM_NAME;
import static javax.faces.validator.BeanValidator.ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME;

import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.application.ProjectStage;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.WebListener;

/**
 * Programatically replace web.xml setup with this {@link ServletContextListener} <br/>
 * NB: some web.xml elements cannot be programatically replaced, for example &lt;welcome-file-list&gt;
 *
 * <pre>
 * {@code
   <web-app
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://xmlns.jcp.org/xml/ns/javaee"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
    version="4.0">

    <context-param>
      <param-name>javax.faces.PROJECT_STAGE</param-name>
      <param-value>Development</param-value>
    </context-param>
    <context-param>
      <param-name>javax.faces.FACELETS_REFRESH_PERIOD</param-name>
      <param-value>0</param-value>
    </context-param>
    <context-param>
      <param-name>javax.faces.validator.ENABLE_VALIDATE_WHOLE_BEAN</param-name>
      <param-value>true</param-value>
    </context-param>

    <servlet>
      <servlet-name>Faces Servlet</servlet-name>
      <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
      <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
      <servlet-name>Faces Servlet</servlet-name>
      <url-pattern>*.xhtml</url-pattern>
    </servlet-mapping>
 </web-app>
   }
 * </pre>
 *
 * @author mwnorman
 */
@FacesConfig(
		// JSF 2.3 is configured 'out-of-the-box' to be backwards-compatible with 2.2
		// so if you want 2.3 features, you must *explicitly* configure it with the
		// @FacesConfig annotation and Version.JSF_2_3 constant
		version = Version.JSF_2_3)
@WebListener
public class FacesConfigContextListener implements ServletContextListener {
	@Override
	public void contextInitialized( ServletContextEvent sce) {
		ServletContextListener.super.contextInitialized( sce);
		ServletContext sc = sce.getServletContext();

		//Registering the JSF Servlet
		Dynamic facesServlet = sc.addServlet( "Faces Servlet", FacesServlet.class.getName());
		//Specifying the Servlet Mapping
		facesServlet.addMapping( "*.xhtml");
		//Setting Priority, 0 or higher for eager, if negative then it's lazy
		facesServlet.setLoadOnStartup( 1);
		//Context parameters
		sc.setInitParameter( FACELETS_REFRESH_PERIOD_PARAM_NAME, "0");
		sc.setInitParameter( PROJECT_STAGE_PARAM_NAME, ProjectStage.Development.name());
		sc.setInitParameter( ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME, Boolean.TRUE.toString());
	}
}