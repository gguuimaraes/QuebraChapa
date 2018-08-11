package br.com.vitral.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.SetorModel;

@FacesConverter(value = "funcionarioConverter", forClass = FuncionarioModel.class)
public class FuncionarioConverter implements Converter {
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
		if (value != null && !value.isEmpty()) {
			return (FuncionarioModel) uiComponent.getAttributes().get(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
		if (value instanceof FuncionarioModel) {
			FuncionarioModel entity = (FuncionarioModel) value;
			if (entity != null && entity instanceof FuncionarioModel && entity.getId() != 0) {
				uiComponent.getAttributes().put(String.valueOf(entity.getId()), entity);
				return String.valueOf(entity.getId());
			}
		}
		return "";
	}
}