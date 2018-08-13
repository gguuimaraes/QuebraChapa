package br.com.vitral.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.weld.exceptions.UnsupportedOperationException;

import br.com.vitral.entidade.Usuario.TipoUsuario;
import br.com.vitral.modelo.UsuarioModel;

@WebFilter("/sistema/usuario.xhtml")
public class UsuarioFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpSession httpSession = ((HttpServletRequest) req).getSession();
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
		HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
		if (httpServletRequest.getRequestURI().indexOf("index.xhtml") <= -1) {
			UsuarioModel usuarioModel = (UsuarioModel) httpSession.getAttribute("usuarioAutenticado");
			if (usuarioModel.getTipo() != TipoUsuario.MESTRE) {
				httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/sistema/home.xhtml");
			} else {
				chain.doFilter(req, resp);
			}
		} else {
			chain.doFilter(req, resp);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		//throw new UnsupportedOperationException("UsuarioFilter.init() não foi implementado.");
	}

	public void destroy() {
		//throw new UnsupportedOperationException("UsuarioFilter.destroy() não foi implementado.");
	}
}