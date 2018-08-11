package br.com.vitral.filtro;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(servletNames = { "Faces Servlet" })
public class JPAFilter implements Filter {

	private EntityManagerFactory emFactory;
	private static final String PERSISTENCE_UNIT_NAME = "unit_app";

	public void destroy() {
		this.emFactory.close();
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		EntityManager em = this.emFactory.createEntityManager();
		req.setAttribute("entityManager", em);
		em.getTransaction().begin();
		chain.doFilter(req, resp);

		try {
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		this.emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

}