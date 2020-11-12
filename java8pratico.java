import java.util.stream.*;
import java.util.stream.IntStream; 
import java.util.stream.Stream; 
import java.lang.Math.*;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.IntSupplier;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.Optional;
import java.util.Iterator;
import java.time.*;
import java.time.format.DateTimeFormatter;


import static java.util.stream.Collectors.*;


class Capitulo10{

	public static void main (String[] args){

		//plus minus Months Years
		LocalDate mesQueVem = LocalDate.now().plusMonths(1); //pega a data atual e soma 1 mês
		String s1 = "mesQueVem: " + mesQueVem;
			
		

		LocalTime agora = LocalTime.now();
		String s2 = "agora: " + agora;

		LocalDate hoje = LocalDate.now();
		String s3 = "hoje: " + hoje;

		LocalDateTime hojeAoMeioDia =  hoje.atTime(12,0);
		String s4 = "hojeAoMeioDia:  " + hojeAoMeioDia;

		String s5 = "hojeAgoraCom_atTime: " + hoje.atTime(agora);

		ZonedDateTime zone = hojeAoMeioDia.atZone(ZoneId.of("America/Sao_Paulo"));


		List<String> tempos = Arrays.asList(s1, s2, s3, s4, s5, 
			"dataEspecificada: " +  LocalDate.of(2020,11,12) //of
			, "alterando ano com with: " + LocalDate.now().withYear(1989) //with
			, "pegando o ano: " + LocalDate.now().withYear(1989).getYear() //get
			, "teste booleano com is : " + LocalDate.now().withYear(1989).isBefore(LocalDate.now()) //is
			, "dia do mes : " + MonthDay.now().getDayOfMonth() //get
			, "mes do ano : " + YearMonth.now().getMonth() 
			, "enum de mes dezembro + 1  : " + Month.DECEMBER.plus(1)  
			, "formatacao c/ ISO_LOCAL_TIME: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)
			, "formatacao c/ ISO_LOCAL_DATE: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
			, "formatacao c/ ofPattern: "  + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
			, " : "  

			);



//		List<LocalDate> periodos = Arrays.asList(mesQueVem, hoje); 

//		List<LocalDateTime> duracoes = Arrays.asList( agora, hojeAoMeioDia); 

		tempos.stream().forEach(System.out :: println);

		//periodos.stream().forEach(System.out :: println);


		//duracoes.stream().forEach(System.out :: println);



	}


}





class Capitulo8{

	public static void main(String [] args){

		Usuario user1 = new Usuario("Paulo Silveira", 150);
		Usuario user2 = new Usuario("Rodrigo Turini", 120);
		Usuario user3 = new Usuario("Guilherme Silveira", 190);
		
		List<Usuario> usuarios = Arrays.asList(user1, user2, user3); //cria uma lista imutavel


		List<Usuario> filtradosOrdenados = usuarios.stream() //pipeline de operações encadeadas. lazy: executa somente qdo necessário
			//operacoes intermediarias
			.filter(u -> u.getPontos() > 100) //filtra os usuarios com mais de 100 pontos - devolve um stream ainda nao filtrado mas marcado
			.sorted(Comparator.comparing(Usuario::getNome)) //ordena pelo nome usando method reference - devolve um stream ainda nao ordenado mas marcado
			//operacao terminal invocada: o stream agora é filtrado e ornado - pipeline será executado
			.collect(Collectors.toList()); //coleta o stream p/ tranformar numa nova lista

		Optional<Usuario> usuarioOptional = usuarios.stream()
			.filter(u -> u.getPontos() > 100)
			.findAny(); //retorna um optional. Operação terminal

		usuarios.stream()
			.filter(u -> u.getPontos() > 100)
			.peek(System.out::println) //pedido p/ q o stream execute a tarefa toda vez q processar um elemento
			.findAny(); 

		usuarios.stream()
			.sorted(Comparator.comparing(Usuario::getNome)) //método intermediário stateful. Podem precisar processar todo o stream mesmo q a operação termninal não demande isso.
			.peek(System.out::println)
			.findAny();

		usuarios.forEach(System.out :: println);

		System.out.println("\n\nFiltrados Ordenados");

		filtradosOrdenados.forEach(System.out :: println);

		//operações de redução: utilizam os elementos de um stream p/ retornar um vlr final
		int total = usuarios.stream()
			.mapToInt(Usuario::getPontos)
			.sum(); //redução - todos trabalham c/ Optional menos sum() e count()

		System.out.println("\n\nTotal de Pontos" + total);

		//explicitar a operação de redução
		int valorInicial = 0;
		IntBinaryOperator operacao = (a,b) -> a + b;

		int total2 = usuarios.stream()
			.mapToInt(Usuario::getPontos)
			.reduce(valorInicial, operacao);

		System.out.println("\n\nTotal de Pontos" + total2);

		int total3 = usuarios.stream()
			.mapToInt(Usuario::getPontos)
			.reduce(0, Integer::sum); //utilizando o method reference

		//importante conhecer reduce p/ aplicar em operações q não existem em stream
		int multiplicacao = usuarios.stream()
			.mapToInt(Usuario::getPontos)
			.reduce(1, (a,b) -> a * b);

		//percorrer um stream utilizando Iterator - p/ modificar objetos dentro do stream
		Iterator<Usuario> i = usuarios.stream().iterator();
		
		System.out.println("\npercorrendo stream com Iterator");
		usuarios.stream().iterator()
			.forEachRemaining(System.out::println);

		usuarios.stream().iterator()
			.forEachRemaining(Usuario :: tornaModerador);

		System.out.println("\npercorrendo stream com Iterator");
		usuarios.stream().iterator()
			.forEachRemaining(System.out::println);

		//testar predicados sem filtrar a lista
		System.out.println("\nStream possui moderador: " + usuarios.stream()
			.anyMatch(Usuario::isModerador));

		//stream infinito
		Random random = new Random(0);
		Supplier<Integer> supplier = () -> random.nextInt(); //regra p/ criar os obj dentro desse stream - lista "infinita de numeros aleatórios"
		Stream<Integer> stream = Stream.generate(supplier); //lazy: só serão gerados a medida q serão necessários

		//int valor = stream.sum(); nunca terminara de executar

			

		
	}



}


class Fibonacci implements IntSupplier {
	private int anterior = 0;
	private int proximo = 1;

	public int getAsInt() {
		proximo = proximo + anterior;
		anterior = proximo - anterior;
		return anterior;
	}
}



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

		//List<Usuario> maisQue100 = new ArrayList<>(); //nova lista
		/*
		usuarios.stream()
			.filter(u -> u.getPontos() > 100) //filtra
			.forEach(u -> maisQue100.add(u)); //adiciona numa nova lista						
		*/
		
			
		/*usuarios.stream()
			.filter(u -> u.getPontos() > 100)
			.forEach(maisQue100::add);*/

		//adicionando os elementos filtrados numa nova lista
		List<Usuario> maisQue100 = usuarios.stream()
		.filter(u -> u.getPontos() > 100)
		.collect(toList()); //toList método static da interface Collectors

		//utilizando map: transformação na lista sem variaveis intermediárias
		List<Integer> pontos = usuarios.stream()
			.map(Usuario::getPontos)
			.collect(toList());

		pontos.forEach(System.out::println);

		//retorna media s/ autoboxing
		double pontuacaoMedia = usuarios.stream()
			.mapToInt(Usuario::getPontos)
			.average()
			.getAsDouble();
		
		//uilizando Optional
		 double media = usuarios.stream()
			.mapToInt(Usuario::getPontos)
			.average() //retorna OptionalDouble
			.orElse(0.0); //se a lista for vazia, a media retorna é 0.0
			//.orElseThrow(IllegalStateException::new); //lança uma exception: recebe Supplier
			//.ifPresent(valor -> janela.atualiza(valor)); //se existe valor: recebe Consumer
		
			

	}


}
