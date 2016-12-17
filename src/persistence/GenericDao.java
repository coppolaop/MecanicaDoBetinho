package persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.Cliente;
import entity.Mecanico;

public class GenericDao<E> {

	Session s;
	Transaction t;
	
	public void create(E obj) throws Exception{
		s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
				s.save(obj);
			t.commit();
		s.close();
	}
	
	public void delete(E obj) throws Exception{
		s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
				s.delete(obj);
			t.commit();
		s.close();
	}
	
	public List<E> findAll(Class<E> entity) throws Exception{
			s = HibernateUtil.getSessionFactory().openSession();
				List<E> lista = s.createCriteria(entity).list();
			s.close();
			return lista;
	} 
	
	public E findById(Integer id,Class<E> entity){
		s = HibernateUtil.getSessionFactory().openSession();
		return (E)s.get(entity, id);
	}
	
	public E findByName(String name,Class<E> entity){
		s = HibernateUtil.getSessionFactory().openSession();
		return (E)s.createSQLQuery("select * from "+entity.getSimpleName()+" where nome='"+name+"'").addEntity(entity).uniqueResult();
	}
	
//	public Usuario get(Usuario u){
//		s = HibernateUtil.getSessionFactory().openSession();
//		try{
//			 List<Usuario> lista = s.createSQLQuery("select u.* from usuario u where username = '"+u.getUsername()+"' and senha = sha1('"+u.getSenha()+"')").addEntity("usuario", Usuario.class).list();
//			 Usuario user = lista.get(0);
//			 return user;
//		}catch(Exception ex){
//			ex.printStackTrace();
//			return null;
//		}
//	}
}