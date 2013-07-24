package th.co.aerothai.callservice.converter;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import th.co.aerothai.callservice.customtype.BudgetType;

@FacesConverter("budgetTypeConverter")
public class BudgetTypeConverter extends EnumConverter{
	public BudgetTypeConverter(){
		super(BudgetType.class);
	}
}
