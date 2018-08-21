package br.com.vitral.util;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

public class Uteis {
	
	private Uteis() {
	}

	public static EntityManager getEntityManager() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		return (EntityManager) request.getAttribute("entityManager");
	}

	// MOSTRAR MENSAGEM
	public static void message(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage("Alerta", mensagem));
	}

	// MOSTRAR MENSAGEM
	public static void messageWarning(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção:", mensagem));
	}

	// MOSTRAR MENSAGEM
	public static void messageInformation(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensagem));
	}

	public static Date getDataInicio(Date data) {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(data);
		inicio.set(Calendar.MILLISECOND, 0);
		inicio.set(Calendar.SECOND, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.HOUR_OF_DAY, 0);
		if (inicio.get(Calendar.DATE) <= 21) {
			inicio.add(Calendar.MONTH, -1);
		}
		inicio.set(Calendar.DATE, 22);
		return inicio.getTime();
	}

	public static Date getDataFim(Date data) {
		Calendar fim = Calendar.getInstance();
		fim.setTime(getDataInicio(data));
		fim.set(Calendar.MILLISECOND, 999);
		fim.set(Calendar.SECOND, 59);
		fim.set(Calendar.MINUTE, 59);
		fim.set(Calendar.HOUR_OF_DAY, 23);
		fim.set(Calendar.DATE, 21);
		fim.add(Calendar.MONTH, 1);
		return fim.getTime();
	}

	public static Date getDataInicio() {
		return getDataInicio(new Date());
	}

	public static Date getDataFim() {
		return getDataFim(new Date());
	}
}