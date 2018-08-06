package br.com.vitral.util;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

public class Uteis {

	public static EntityManager JpaEntityManager() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		return (EntityManager) request.getAttribute("entityManager");
	}

	// MOSTRAR MENSAGEM
	public static void Mensagem(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage("Alerta", mensagem));
	}

	// MOSTRAR MENSAGEM
	public static void MensagemAtencao(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção:", mensagem));
	}

	// MOSTRAR MENSAGEM
	public static void MensagemInfo(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensagem));
	}

	public static Date getDataInicio(Date data) {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(data);
		inicio.set(Calendar.MILLISECOND, 0);
		inicio.set(Calendar.SECOND, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.HOUR, 12);
		if (inicio.get(Calendar.DATE) < 22) {
			inicio.add(Calendar.MONTH, -1);
		}
		inicio.set(Calendar.DATE, 22);
		// System.out.println("Data Inicio = " + new SimpleDateFormat("dd/MM/yyyy
		// HH:mm:ss.SSS").format(inicio.getTime()));
		return inicio.getTime();
	}

	public static Date getDataFim(Date data) {
		Calendar fim = Calendar.getInstance();
		fim.setTime(getDataInicio(data));
		fim.set(Calendar.MILLISECOND, 999);
		fim.set(Calendar.SECOND, 59);
		fim.set(Calendar.MINUTE, 59);
		fim.set(Calendar.HOUR, 23);
		fim.set(Calendar.DATE, 21);
		fim.add(Calendar.MONTH, 1);
		// System.out.println("Data Fim = " + new SimpleDateFormat("dd/MM/yyyy
		// HH:mm:ss.SSS").format(fim.getTime()));
		return fim.getTime();
	}

	public static Date getDataInicio() {
		return getDataInicio(new Date());
	}

	public static Date getDataFim() {
		return getDataFim(new Date());
	}
}