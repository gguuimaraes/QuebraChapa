package br.com.vitral.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.vitral.modelo.TipoVidroModel;

@FacesConverter(value = "tipoVidroConverter", forClass = TipoVidroModel.class)
public class TipoVidroConverter implements Converter {
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
		if (value != null && !value.isEmpty()) {
			return (TipoVidroModel) uiComponent.getAttributes().get(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
		if (value instanceof TipoVidroModel) {
			TipoVidroModel entity = (TipoVidroModel) value;
			if (entity != null && entity instanceof TipoVidroModel && entity.getId() != null) {
				uiComponent.getAttributes().put(entity.getId().toString(), entity);
				return entity.getId().toString();
			}
		}
		return "";
	}
}