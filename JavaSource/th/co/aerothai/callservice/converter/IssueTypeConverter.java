package th.co.aerothai.callservice.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import org.jboss.aop.advice.annotation.Args;

import th.co.aerothai.callservice.customtype.IssueType;

@FacesConverter("issueTypeConverter")
public class IssueTypeConverter extends EnumConverter{
	public IssueTypeConverter(){
		super(IssueType.class);
	}
}
