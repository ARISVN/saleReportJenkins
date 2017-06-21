package vn.com.aris.jenkinPlugin;

import hudson.Extension;
import hudson.Functions;
import hudson.model.RootAction;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import jenkins.model.ModelObjectWithContextMenu;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;

import vn.com.aris.JdbcConnector;
import vn.com.aris.demo.DemoConfig;
import vn.com.aris.dto.AppleChartDTO;
import vn.com.aris.utils.GSUtil;
import vn.com.aris.utils.ReadCSV;

@Extension
public class Root implements RootAction, ModelObjectWithContextMenu {

	public String getMyString() {
		return "Hello Jenkins!";
	}

	@Override
	public String getIconFileName() {
		return "/plugin/jenkinPlugin/myicon.ico";
	}

	@Override
	public String getDisplayName() {
		return "Sales Report";
	}

	@Override
	public String getUrlName() {
		return "sale-report";
	}

	public AbstractAction getDynamic(String name) {
		System.out.println("BBBBBBBBBBBBBB");
		List<AbstractAction> allAction = AbstractAction.all();
		for (AbstractAction ui : allAction) {
			System.out.println(ui.getUrl());
			if (ui.getUrl().equals(name)) {
				return ui;
			}
		}
		return null;
	}

	@WebMethod(name = { "" })
	public void doDefault(StaplerRequest request, StaplerResponse response) {
		try {

			response.sendRedirect("application-management");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AbstractAction getGotoMainPage() {
		AbstractAction ab = new ApplicationManagement();
		return ab;
	}

	public String getReport() {
		String fileName = JdbcConnector.getLastestFileName(0);
		return GSUtil.getDataReport(fileName);
	}

	public AppleChartDTO getAppleReport() {
		String fileName = JdbcConnector.getLastestFileName(1);
		return ReadCSV.getDataFromTxt(DemoConfig.PATH_TO_APPLE + "\\"
				+ fileName, DemoConfig.PATH_TO_APPLE + "appleTemp.txt");
	}

	public String getResourcePath() {
		return Functions.getResourcePath();
		// "<script language=\"javascript\" type=\"text/javascript\""
		// + "src=\""+Functions.getResourcePath()
		// +"/jqplot/jquery.min.js\"></script>"
		// + "<script language=\"javascript\" type=\"text/javascript\""
		// + "src=\""+Functions.getResourcePath()
		// +"/jqplot/jquery.jqplot.min.js\"></script>"
		// + "<script type=\"text/javascript\""
		// + "src=\""+Functions.getResourcePath()
		// +"/jqplot/plugins/jqplot.highlighter.js\"></script>"
		// + "<script type=\"text/javascript\""
		// + "src=\""+Functions.getResourcePath()
		// +"/jqplot/plugins/jqplot.cursor.js\"></script>"
		// + "<script type=\"text/javascript\""
		// + "src=\""+Functions.getResourcePath()
		// +"/jqplot/plugins/jqplot.dateAxisRenderer.js\"></script>"
		// + "<script type=\"text/javascript\""
		// + "src=\""+Functions.getResourcePath()
		// +"/jqplot/plugins/jqplot.pieRenderer.min.js\"></script>"
		// + "<link rel=\"stylesheet\" type=\"text/css\""
		// + "href=\""+Functions.getResourcePath()
		// +"/jqplot/jquery.jqplot.css\" />"
		// + "<script type=\"text/javascript\">";
	}

	@Override
	public ContextMenu doContextMenu(StaplerRequest request,
			StaplerResponse response) throws Exception {
		return new ContextMenu().addAll(AbstractAction.all());
	}

	// @RequirePOST
	@WebMethod(name = { "report" })
	public void doStart(@QueryParameter String name,
			@QueryParameter String age, StaplerRequest request,
			StaplerResponse response) {
		// start();
		System.out.println(name);
		System.out.println(age);
		try {
			response.forward(this, "start", request);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return HttpResponses.ok();
	}

	@WebMethod(name = { "index" })
	public void doIndex(StaplerRequest request, StaplerResponse response) {
		try {
			response.forward(this, "application-management", request);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
