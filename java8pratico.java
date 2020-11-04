import java.util.stream.*;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiFunction;


class Usuario {
	private String nome;
	private int pontos;
	private boolean moderador;

public Usuario(){}




public Usuario(String nome, int pontos) {
	this.pontos = pontos;
	this.nome = nome;
	this.moderador = false;
}



public Usuario(String nome) {
	this.nome = nome;
}

public String getNome() {
	return nome;
}

public int getPontos() {
	return pontos;
}

public void tornaModerador() {
	this.moderador = true;
}

public boolean isModerador() {
	return moderador;
}

public String toString() {
	return "Usuario " + nome + ", Pontos: " + pontos + ", Moderador: " + moderador;
}

}

class Capitulo2 {
	public static void main(String ... args){
		Usuario user1 = new Usuario("Paulo Silveira", 150);
		Usuario user2 = new Usuario("Rodrigo Turini", 120);
		Usuario user3 = new Usuario("Guilherme Silveira", 190);
		
		List<Usuario> usuarios = Arrays.asList(user1, user2, user3); //cria uma lista imutavel
		
		System.out.println("enhnaced for tradicional");
		for(Usuario u : usuarios){
			System.out.println(u.getNome());
		}

		Mostrador mostrador = new Mostrador();
		
		System.out.println("\n passando um objeto instanciado");		
		usuarios.forEach(//metodo de List
			mostrador); //forEach recebe um Consumer
		
		//utilizando classes anonimas
		System.out.println("\n c/ classes anonimas");
		usuarios.forEach(new Consumer<Usuario>() {
			public void accept(Usuario u){
				System.out.println(u.getNome());
			}
		});

		System.out.println("\n c/ lambda");		
		usuarios.forEach(u -> System.out.println(u.getNome()));	//tudo inferido em tempo de compilação

		usuarios.forEach(u -> u.tornaModerador());	//tudo inferido em tempo de compilação			

	}
}

//Consumer interface com apenas 1 método abstrato: requisito p/ o compilador consiga traduzi-la p/ uma expressão lambda
class Mostrador implements Consumer<Usuario> { //consumir é realizar alguma tarefa q faça sentido
	public void accept(Usuario u){
		System.out.println(u.getNome());
	}
}

class RunnableTest{

	public static void main(String [] args){

		Runnable r = new Runnable(){	//implementando classe anonima
			public void run(){//interface Runnable  tem apenas um método abstrato run()
				for (int i = 0; i <= 1000; i++){
					System.out.println(i);
				}
			}
		};

		//new Thread(r).start();
		
		//apenas c/ lamda
		new Thread(() -> {for (int i = 0; i <= 1000; i++){
					System.out.println(i);
				}}
		).start();


	}

}

@FunctionalInterface
interface Validador<T> {
	boolean valida(T t);

	//boolean outroMetodo(T t); não permite compilar mais de 1 método abstrato qdo anotado

	//pode possuir default methods

}

class ValidadorTest{

	public static void main(String [] args){
		
		Validador<String> validadorCEP = new Validador<String>() {
			public boolean valida(String valor) {
				return valor.matches("[0-9]{5}-[0-9]{3}");
			}
		};

		System.out.println(validadorCEP.valida("01307-001"));
		System.out.println(validadorCEP.valida("teste"));

		String valor = "01307-001";

		String match = "[0-9]{5}-[0-9]{3}"; //expressão lambda pode acessar variaveis locais q não sejam finais desde q a variavel não altere mais o valor (efetivamente final)
		
		Validador<String> validadorCEPComLambda = cep -> cep.matches(match);		

		System.out.println(validadorCEPComLambda.valida(valor));		

		/*match = "[0-9]{5}-[0-9]{3}-"; variavel usada na expressão lambda com valor alterado não permite mais compilação */

	}
}

class Capitulo4 {
	public static void main(String ... args){
		Usuario user1 = new Usuario("Paulo Silveira", 150);
		Usuario user2 = new Usuario("Rodrigo Turini", 120);
		Usuario user3 = new Usuario("Guilherme Silveira", 190);
		
		List<Usuario> usuarios = Arrays.asList(user1, user2, user3); //lista imutavel

		Consumer<Usuario> mostraMensagem = u -> System.out.println("antes de imprimir os nomes");
		Consumer<Usuario> imprimeNome = u -> System.out.println(u.getNome()); 
		usuarios.forEach(mostraMensagem.
			andThen(imprimeNome)); //andThen (default method) compoe instancias de Consumer p/ q sejam executadas sequencialmente

		//Predicate testa objetos de determinado tipo e deveolve true se a condição for satisfeita
		Predicate<Usuario> predicado = new Predicate<Usuario>() {
			public boolean test(Usuario u) {
				return u.getPontos() > 160;
			}
		}; //classes anonimas precisam desse ; no final

		List<Usuario> usuarios2 = new ArrayList<>();//criando uma lista mutavel c/ possibilidade de remoção
		usuarios2.addAll(usuarios); //adiconando os elementos de usuarios em usuarios2
		
		//removeIf() remove todos os elementos da Collection q retornarem true p/ o argumento Predicate
		usuarios2.removeIf( u -> u.getPontos() > 160);
		usuarios2.forEach(imprimeNome);
	}
}

class Capitulo5{

	public static void main(String ... args){

		
		Comparator<Usuario> comparator = new Comparator<Usuario>() {
			public int compare(Usuario u1, Usuario u2) {
				return u1.getNome().compareTo(u2.getNome());
			}
		};

		Usuario user1 = new Usuario("Paulo Silveira", 150);
		Usuario user2 = new Usuario("Rodrigo Turini", 120);
		Usuario user3 = new Usuario("Guilherme Silveira", 190);
		
		List<Usuario> usuarios = Arrays.asList(user1, user2, user3); //lista imutavel

		List<Usuario> usuarios2 = new ArrayList<Usuario>();

		usuarios2 = usuarios;

		Collections.sort(usuarios, comparator);

		usuarios.forEach( u -> System.out.println(u.getNome()));

		usuarios2.sort( (u1,u2) -> u1.getNome().compareTo(u2.getNome())); //a propria list pode ordenar com um Comparador

		usuarios2.forEach( u -> System.out.println(u.getNome()));

		usuarios.sort(Comparator.comparing(u -> u.getNome())); //comparing(): metodo static da interface Comparator. Retorna a String do nome como critério de comparação

		List<String> palavras = Arrays.asList("Casa do Código", "Alura", "Caelum");
		
		palavras.forEach( palavra ->  System.out.println(palavra));
		
		palavras.sort(Comparator.naturalOrder());

		palavras.forEach( palavra ->  System.out.println(palavra));

		usuarios.sort(Comparator.comparing(u -> u.getPontos())); //ordena  pontos como critério de comparação (menor p/ o maior)

		usuarios.forEach( u -> System.out.println(u.getNome()));		

	}

}

class Capitulo6{
	public static void main(String ... args){

		
		Comparator<Usuario> comparator = new Comparator<Usuario>() {
			public int compare(Usuario u1, Usuario u2) {
				return u1.getNome().compareTo(u2.getNome());
			}
		};

		Usuario user1 = new Usuario("Paulo Silveira", 150);
		Usuario user2 = new Usuario("Rodrigo Turini", 120);
		Usuario user3 = new Usuario("Guilherme Silveira", 190);
		


		List<Usuario> usuarios = Arrays.asList(user1, user2, user3); //lista imutavel

		usuarios.forEach(u -> System.out.println(u.getNome() + " : Moderador: "  + u.isModerador()));		

		usuarios.forEach(Usuario :: tornaModerador); //usando method reference

		usuarios.forEach(u -> System.out.println(u.getNome() + " : Moderador: "  + u.isModerador()));		

		usuarios.sort(Comparator.comparingInt(Usuario::getPontos).thenComparing(Usuario::getNome)); //compondo a ordenação com thenComparing 

		usuarios.forEach(u -> System.out.println(u.getNome()));				

		usuarios.sort(Comparator.nullsLast(Comparator.comparing(Usuario::getNome))); //todos os usuarios nulos ficaram no fim

		usuarios.sort(Comparator.comparing(Usuario::getPontos).reversed());//ordena os usuarios por pontos de ordem reversa

		usuarios.forEach(u -> System.out.println(u.getNome()));		

		Usuario rodrigo = new Usuario("Rodrigo Turini", 50);
		Runnable bloco = rodrigo::tornaModerador; //referenciando o método de rodrigo e não de qualquer objeto do tipo usuário
		bloco.run();

		usuarios.forEach(System.out::println); //com toString de Usuario sobreescrito

		//fabrica de usuarios
		Supplier<Usuario> criadorDeUsuarios = Usuario::new; //chama construtor default
		Usuario novo = criadorDeUsuarios.get(); //objeto do tipo Usuario instanciado

		Function<String, Usuario> criadorDeUsuariosComArgumentoNome = Usuario::new; 

		BiFunction<String, Integer, Usuario> criadorDeUsuariosComArgumentos = Usuario::new;
		Usuario user4 = criadorDeUsuariosComArgumentos.apply("Jefferson", 300);
				
		//usuarios.forEach(System.out::println); //com toString de Usuario sobreescrito
		
		
		BiFunction<Integer, Integer,Integer> valorMax  = Math::max ;// method reference com metodos static. Recebe dois inteiros e devolve um inteiro.
		System.out.println(valorMax.apply(9,7));
	
}
}

class Capitulo7{

	public static void main (String [] args){

		Usuario user1 = new Usuario("Paulo Silveira", 150);
		Usuario user2 = new Usuario("Rodrigo Turini", 120);
		Usuario user3 = new Usuario("Guilherme Silveira", 190);
		
		BiFunction<String, Integer, Usuario> criadorDeUsuariosComArgumentos = Usuario::new;
		Usuario user4 = criadorDeUsuariosComArgumentos.apply("Jefferson", 300);


		List<Usuario> usuarios = Arrays.asList(user1, user2, user3, user4); //lista imutavel

		usuarios.sort(Comparator.comparing(Usuario::getPontos).reversed()); //ordena a lista de usuarios por pontos do maior p/ menor
		usuarios
			.subList(0,2) //filtro os 3 primeiros usuarios 
			.forEach(Usuario::tornaModerador); //torna esses usuarios moderadores

		//usuarios.forEach(System.out::println); //com toString de Usuario sobreescrito	

		Stream<Usuario> stream = usuarios.stream(); //stream possui filter

		stream.filter( u -> u.getPontos() > 150); //filter um Predicate - 

		//usuarios.forEach(System.out::println); //nao altera os elementos do stream original

		//usuarios.stream().filter( u -> u.getPontos() > 150).forEach(System.out::println); //elementos filtrados

		List<Usuario> usuarios2 = Arrays.asList(user1, user2, user3, user4); //lista imutavel
	
		usuarios2.stream().filter(u -> u.getPontos() > 150).forEach(Usuario :: tornaModerador); //elementos filtrados tornam moderadores

		usuarios2.forEach(System.out::println);						


	

	}


}
