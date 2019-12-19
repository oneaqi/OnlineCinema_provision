package ba.pehli.cinema.utils;

import java.util.Collection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

public class CinemaJasperCompiler {
	
	public static void main(String[] args){
		try {
			JasperCompileManager.compileReportToFile("src/main/resources/reports/catalog.jrxml",
					"src/main/resources/reports/catalog.jasper");
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
