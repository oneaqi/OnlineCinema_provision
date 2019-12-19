package ba.pehli.cinema.utils;

import java.util.Date;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

/**
 * Registration of custom property editors in Spring
 * @author almir
 *
 */
public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar{

	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {
	}
	
}
