package th.co.aerothai.callservice.converter;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="projectTypeConverter")
public class ProjectTypeConverter extends EnumConverter{
	public ProjectTypeConverter(){
		super(ProjectTypeConverter.class);
	}
}
