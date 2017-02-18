package control;

import org.jcommon.encryption.SimpleMD5;

import entity.Usuario;

public class Criptografia {

	public static void criptografia(Usuario u){
		SimpleMD5 md5 = new SimpleMD5(u.getSenha(),"Ch4vegr@nde&G!g4nte5ca&C0mpl1cad4");
		u.setSenha(md5.toHexString());
	}
}
