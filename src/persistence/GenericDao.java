package persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
	
	public void update(E obj) throws Exception{
		s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
				s.update(obj);
			t.commit();
		s.close();
	}
	
	public List<E> findAll(Class<E> entity) throws Exception{
		s = HibernateUtil.getSessionFactory().openSession();
			s.beginTransaction();
		        Criteria criteria = this.s.createCriteria(entity);
		        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		        List<E> lista = criteria.list();
		        s.getTransaction().commit();
	        s.close();
        return lista;
	} 
	
	public E findById(Integer id,Class<E> entity){
		s = HibernateUtil.getSessionFactory().openSession();
			E e = (E)s.get(entity, id);
		s.close();
		return e;
	}
	
	public E findByName(String name,Class<E> entity){
		s = HibernateUtil.getSessionFactory().openSession();
			E e = (E)s.createSQLQuery("select * from "+entity.getSimpleName()+" where nome='"+name+"'").addEntity(entity).uniqueResult();
		s.close();
		return e;
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